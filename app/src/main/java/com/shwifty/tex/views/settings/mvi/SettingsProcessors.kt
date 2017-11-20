package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.utils.ValidateChangeWorkingDirectoryResult
import com.shwifty.tex.utils.composeIo
import com.shwifty.tex.utils.validateWorkingDirectoryCanBeChanged
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 19/11/2017.
 */
val reducer = BiFunction <SettingsViewState, SettingsResult, SettingsViewState> {
    previousState: SettingsViewState, result: SettingsResult ->
    when (result) {
        is SettingsResult.UpdateWorkingDirectoryInFlight -> previousState.copy(isLoading = true)
        is SettingsResult.UpdateWorkingDirectorySuccess -> previousState.copy(isLoading = false, currentWorkingDirectory = result.newFile)
        is SettingsResult.UpdateWorkingDirectoryError -> previousState.copy(isLoading = false, errorString = result.error.localizedMessage)
        SettingsResult.RestartApp -> previousState.copy(restart = true)
        is SettingsResult.LoadWorkingDirectorySuccess -> previousState.copy(isLoading = false, currentWorkingDirectory = result.newFile)
        is SettingsResult.LoadWorkingDirectoryError -> previousState.copy(isLoading = false, errorString = result.error.localizedMessage)
    }
}

fun actionFromIntent(intent: SettingsIntents): SettingsActions = when (intent) {
    is SettingsIntents.NewWorkingDirectorySelected -> SettingsActions.ClearErrorsAndUpdateWorkingDirectory(intent.context, intent.previousDirectory, intent.newDirectory, intent.moveFiles)
    is SettingsIntents.RestartApp -> SettingsActions.RestartApp
    is SettingsIntents.InitialIntent -> SettingsActions.LoadWorkingDirectory(intent.context)
}

fun settingsActionProcessor(preferencesRepository: IPreferenceRepository): ObservableTransformer<SettingsActions, SettingsResult> =
        ObservableTransformer { action: Observable<SettingsActions> ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(SettingsActions.ClearErrorsAndUpdateWorkingDirectory::class.java).compose(updateWorkingDirectoryProcessor(preferencesRepository)),
                        shared.ofType(SettingsActions.RestartApp::class.java).compose(restartAppProcessor),
                        shared.ofType(SettingsActions.LoadWorkingDirectory::class.java).compose(loadWorkingDirectoryProcessor(preferencesRepository))
                )
            }
        }

val restartAppProcessor = ObservableTransformer<SettingsActions.RestartApp, SettingsResult> { action: Observable<SettingsActions.RestartApp> ->
    action.switchMap {
        Observable.just(SettingsResult.RestartApp)
    }
}

fun loadWorkingDirectoryProcessor(preferencesRepository: IPreferenceRepository) = ObservableTransformer { actions: Observable<SettingsActions.LoadWorkingDirectory> ->
    actions.switchMap { (context) ->
        preferencesRepository.getWorkingDirectoryPreference(context)
                .map { SettingsResult.LoadWorkingDirectorySuccess(it) as SettingsResult }
                .onErrorReturn { SettingsResult.LoadWorkingDirectoryError(it) }
                .composeIo()
                .startWith(SettingsResult.UpdateWorkingDirectoryInFlight)
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
                .onErrorReturn { SettingsResult.UpdateWorkingDirectoryError(it) }
                .composeIo()
                .startWith(SettingsResult.UpdateWorkingDirectoryInFlight)
    }
}