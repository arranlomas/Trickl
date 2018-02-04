package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.Trickl
import com.shwifty.tex.models.AppTheme
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.utils.ValidateChangeWorkingDirectoryResult
import com.shwifty.tex.utils.composeIo
import com.shwifty.tex.utils.validateWorkingDirectoryCanBeChanged
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import java.io.File

/**
 * Created by arran on 19/11/2017.
 */
val settingsReducer = BiFunction { previousState: SettingsViewState, result: SettingsResult ->
    when (result) {
        is SettingsResult.LoadSettingsSuccess -> previousState.copy(
                originalTheme = result.theme, originalWifiOnly = result.wifiOnly, originalWorkingDirectory = result.workingDirectory,
                workingDirectoryLoading = false, workingDirectoryErrorString = null, currentWorkingDirectory = result.workingDirectory,
                wifiOnlyLoading = false, wifiOnlyErrorString = null, wifiOnly = result.wifiOnly,
                themeLoading = false, themeErrorString = null, theme = result.theme)

        is SettingsResult.LoadSettingsinFlight -> previousState.copy(
                workingDirectoryLoading = true, workingDirectoryErrorString = null, currentWorkingDirectory = null,
                wifiOnlyLoading = true, wifiOnlyErrorString = null, wifiOnly = false,
                themeLoading = true, themeErrorString = null, theme = null)

        is SettingsResult.LoadSettingsError -> previousState.copy(
                workingDirectoryLoading = false, workingDirectoryErrorString = result.error.localizedMessage, currentWorkingDirectory = null,
                wifiOnlyLoading = false, wifiOnlyErrorString = result.error.localizedMessage, wifiOnly = false,
                themeLoading = false, themeErrorString = result.error.localizedMessage, theme = null)

        is SettingsResult.UpdateworkingDirectoryInFlight -> previousState.copy(workingDirectoryLoading = true, workingDirectoryErrorString = null, currentWorkingDirectory = null)
        is SettingsResult.UpdateWorkingDirectorySuccess -> previousState.copy(workingDirectoryLoading = false, workingDirectoryErrorString = null, currentWorkingDirectory = result.newFile)
        is SettingsResult.UpdateWorkingDirectoryError -> previousState.copy(workingDirectoryLoading = false, workingDirectoryErrorString = result.error.localizedMessage, currentWorkingDirectory = null)

        is SettingsResult.ToggleWifiOnlyInFlight -> previousState.copy(wifiOnlyLoading = true, wifiOnlyErrorString = null, wifiOnly = false)
        is SettingsResult.ToggleWifiOnlySuccess -> previousState.copy(wifiOnlyLoading = false, wifiOnlyErrorString = null, wifiOnly = result.wifiOnly)
        is SettingsResult.ToggleWifiOnlyError -> previousState.copy(wifiOnlyLoading = false, wifiOnlyErrorString = result.error.localizedMessage, wifiOnly = null)

        is SettingsResult.ToggleChangeThemeInFlight -> previousState.copy(themeLoading = true, themeErrorString = null, theme = null)
        is SettingsResult.ToggleChangeThemeSuccess -> previousState.copy(themeLoading = false, themeErrorString = null, theme = result.theme)
        is SettingsResult.ToggleChangeThemeError -> previousState.copy(themeLoading = false, themeErrorString = result.error.localizedMessage, theme = null)
    }
}

fun actionFromIntent(intent: SettingsIntents): SettingsActions = when (intent) {
    is SettingsIntents.NewWorkingDirectorySelected -> SettingsActions.ClearErrorsAndUpdateWorkingDirectory(intent.context, intent.newDirectory, intent.moveFiles)
    is SettingsIntents.InitialIntent -> SettingsActions.LoadPreferencesForFirstTime(intent.context)
    is SettingsIntents.ToggleWifiOnly -> SettingsActions.UpdateWifiOnly(intent.context, intent.selected)
    is SettingsIntents.ChangeTheme -> SettingsActions.ChangeTheme(intent.context, intent.newTheme)
}

fun settingsActionProcessor(preferencesRepository: IPreferenceRepository): ObservableTransformer<SettingsActions, SettingsResult> =
        ObservableTransformer { action: Observable<SettingsActions> ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(SettingsActions.LoadPreferencesForFirstTime::class.java).compose(loadPreferencesProcessor(preferencesRepository)),
                        shared.ofType(SettingsActions.ClearErrorsAndUpdateWorkingDirectory::class.java).compose(updateWorkingDirectoryProcessor(preferencesRepository)),
                        shared.ofType(SettingsActions.UpdateWifiOnly::class.java).compose(updateWifiOnlyProcessor(preferencesRepository)),
                        shared.ofType(SettingsActions.ChangeTheme::class.java).compose(changeThemeProcessor(preferencesRepository))
                )
            }
        }

fun loadPreferencesProcessor(preferencesRepository: IPreferenceRepository) = ObservableTransformer { actions: Observable<SettingsActions.LoadPreferencesForFirstTime> ->
    actions.switchMap { (context) ->
        Observable.zip(preferencesRepository.getWorkingDirectoryPreference(context),
                preferencesRepository.getWifiOnlyPreference(context),
                preferencesRepository.getThemPreference(context), Function3<File, Boolean, AppTheme, SettingsResult> { file, wifiOnly, theme ->
            SettingsResult.LoadSettingsSuccess(file, wifiOnly, theme)
        })
                .onErrorReturn { SettingsResult.LoadSettingsError(it) }
                .composeIo()
                .startWith(SettingsResult.LoadSettingsinFlight)
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

fun updateWorkingDirectoryProcessor(preferencesRepository: IPreferenceRepository):
        ObservableTransformer<SettingsActions.ClearErrorsAndUpdateWorkingDirectory, SettingsResult> = ObservableTransformer { actions: Observable<SettingsActions.ClearErrorsAndUpdateWorkingDirectory> ->
    actions.switchMap { action ->
        Trickl.repositoryComponent.getPreferencesRepository().getWorkingDirectoryPreference(action.context)
                .map { previousDirectory -> Pair(previousDirectory, action.newDirectory.validateWorkingDirectoryCanBeChanged(previousDirectory)) }
                .map { (previousDirectory, isValid) ->
                    if (isValid is ValidateChangeWorkingDirectoryResult.Error) {
                        throw IllegalArgumentException(action.context.getString(isValid.messageRes))
                    }
                    preferencesRepository.saveWorkingDirectoryPreference(action.context, action.newDirectory)
                    if (action.moveFiles) {
                        previousDirectory.copyRecursively(action.newDirectory, overwrite = true)
                        previousDirectory.deleteRecursively()
                    }
                }
                .map { SettingsResult.UpdateWorkingDirectorySuccess(action.newDirectory) as SettingsResult }
                .onErrorReturn { SettingsResult.UpdateWorkingDirectoryError(it) }
                .composeIo()
                .startWith(SettingsResult.UpdateworkingDirectoryInFlight)

    }
}

fun changeThemeProcessor(preferencesRepository: IPreferenceRepository) = ObservableTransformer { actions: Observable<SettingsActions.ChangeTheme> ->
    actions.switchMap { action ->
        preferencesRepository.saveThemePreference(action.context, action.theme)
                .map { SettingsResult.ToggleChangeThemeSuccess(action.theme) as SettingsResult }
                .onErrorReturn { SettingsResult.ToggleChangeThemeError(it) }
                .composeIo()
                .startWith(SettingsResult.ToggleChangeThemeInFlight)
    }
}