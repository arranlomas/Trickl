package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.utils.ValidateChangeWorkingDirectoryResult
import com.shwifty.tex.utils.composeIo
import com.shwifty.tex.utils.validateWorkingDirectoryCanBeChanged
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import java.io.File


/**
 * Created by arran on 19/11/2017.
 */
val reducer = BiFunction <SettingsViewState, SettingsResult, SettingsViewState> {
    previousState: SettingsViewState, result: SettingsResult ->
    when (result) {
        is SettingsResult.UpdateWorkingDirectoryInFlight -> previousState.copy(isLoading = true)
        is SettingsResult.UpdateWorkingDirectorySuccess -> previousState.copy(isLoading = false, currentWorkingDirectory = result.newFile, restart = true)
        is SettingsResult.UpdateWorkingDirectoryError -> previousState.copy(isLoading = false, workingDirectoryErrorString = result.error.localizedMessage)
        SettingsResult.RestartApp -> previousState.copy(restart = true)
        is SettingsResult.LoadSettingsSuccess -> previousState.copy(isLoading = false, currentWorkingDirectory = result.workingDirectory, wifiOnly = result.wifiOnly)
        is SettingsResult.LoadSettingsError -> previousState.copy(isLoading = false, loadSettingsErrorString = result.error.localizedMessage)
        SettingsResult.ToggleWifiOnlyInFlight -> previousState.copy(isLoading = true)
        is SettingsResult.ToggleWifiOnlySuccess -> previousState.copy(isLoading = false, wifiOnly = result.selected)
        is SettingsResult.ToggleWifiOnlyError -> previousState.copy(isLoading = false, wifiOnlyErrorString = result.error.localizedMessage)
    }
}

fun actionFromIntent(intent: SettingsIntents): SettingsActions = when (intent) {
    is SettingsIntents.NewWorkingDirectorySelected -> SettingsActions.ClearErrorsAndUpdateWorkingDirectory(intent.context, intent.previousDirectory, intent.newDirectory, intent.moveFiles)
    is SettingsIntents.RestartApp -> SettingsActions.RestartApp
    is SettingsIntents.InitialIntent -> SettingsActions.LoadPreferences(intent.context)
    is SettingsIntents.ToggleWifiOnly -> SettingsActions.UpdateWifiOnly(intent.context, intent.selected)
}

fun settingsActionProcessor(preferencesRepository: IPreferenceRepository): ObservableTransformer<SettingsActions, SettingsResult> =
        ObservableTransformer { action: Observable<SettingsActions> ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(SettingsActions.LoadPreferences::class.java).compose(loadPrederencesProcessor(preferencesRepository)),
                        shared.ofType(SettingsActions.ClearErrorsAndUpdateWorkingDirectory::class.java).compose(updateWorkingDirectoryProcessor(preferencesRepository)),
                        shared.ofType(SettingsActions.RestartApp::class.java).compose(restartAppProcessor),
                        shared.ofType(SettingsActions.UpdateWifiOnly::class.java).compose(updateWifiOnlyProcessor(preferencesRepository))
                )
            }
        }

val restartAppProcessor = ObservableTransformer<SettingsActions.RestartApp, SettingsResult> { action: Observable<SettingsActions.RestartApp> ->
    action.switchMap {
        Observable.just(SettingsResult.RestartApp)
    }
}

fun loadPrederencesProcessor(preferencesRepository: IPreferenceRepository) = ObservableTransformer { actions: Observable<SettingsActions.LoadPreferences> ->
    actions.switchMap { (context) ->
        Observable.zip(preferencesRepository.getWorkingDirectoryPreference(context), preferencesRepository.getWifiOnlyPreference(context), BiFunction<File, Boolean, SettingsResult> { t1, t2 ->
            SettingsResult.LoadSettingsSuccess(t1, t2)
        })
                .onErrorReturn { SettingsResult.LoadSettingsError(it) }
                .composeIo()
                .startWith(SettingsResult.UpdateWorkingDirectoryInFlight)
    }
}

fun updateWifiOnlyProcessor(preferencesRepository: IPreferenceRepository) = ObservableTransformer { actions: Observable<SettingsActions.UpdateWifiOnly> ->
    actions.switchMap { action ->
        preferencesRepository.saveWifiOnlyPreference(action.context, action.selected)
                .map { SettingsResult.ToggleWifiOnlySuccess(action.selected) as SettingsResult }
                .onErrorReturn { SettingsResult.ToggleWifiOnlyError(it) }
                .composeIo()
                .startWith(SettingsResult.ToggleWifiOnlyInFlight)
    }
}

fun updateWorkingDirectoryProcessor(preferencesRepository: IPreferenceRepository) = ObservableTransformer { actions: Observable<SettingsActions.ClearErrorsAndUpdateWorkingDirectory> ->
    actions.switchMap { action ->
        Observable.just(action.newDirectory.validateWorkingDirectoryCanBeChanged(action.previousDirectory))
                .map { isValid ->
                    if (isValid is ValidateChangeWorkingDirectoryResult.Error) {
                        throw IllegalArgumentException(action.context.getString(isValid.messageRes))
                    }
                }
                .map { preferencesRepository.saveWorkingDirectoryPreference(action.context, action.newDirectory) }
                .map {
                    if (action.moveFiles) {
                        action.previousDirectory.copyRecursively(action.newDirectory, overwrite = true)
                        action.previousDirectory.deleteRecursively()
                    }
                }
                .map { SettingsResult.UpdateWorkingDirectorySuccess(action.newDirectory) as SettingsResult }
                .onErrorResumeNext(Observable.empty())
                .onErrorReturn { SettingsResult.UpdateWorkingDirectoryError(it) }
                .composeIo()
                .startWith(SettingsResult.UpdateWorkingDirectoryInFlight)
    }
}