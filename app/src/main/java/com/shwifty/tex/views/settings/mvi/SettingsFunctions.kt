package com.shwifty.tex.views.settings.mvi

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentPostProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.shwifty.tex.models.AppTheme
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.utils.ValidateChangeWorkingDirectoryResult
import com.shwifty.tex.utils.validateWorkingDirectoryCanBeChanged
import io.reactivex.Observable
import io.reactivex.functions.Function3
import java.io.File

/**
 * Created by arran on 19/11/2017.
 */
val settingsReducer = KontentReducer { result: SettingsResult, previousState: SettingsViewState ->
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

        is SettingsResult.UpdateWorkingDirectoryInFlight -> previousState.copy(workingDirectoryLoading = true, workingDirectoryErrorString = null)
        is SettingsResult.UpdateWorkingDirectorySuccess -> previousState.copy(workingDirectoryLoading = false, workingDirectoryErrorString = null, currentWorkingDirectory = result.newFile)
        is SettingsResult.UpdateWorkingDirectoryError -> previousState.copy(workingDirectoryLoading = false, workingDirectoryErrorString = result.error.localizedMessage)

        is SettingsResult.ToggleWifiOnlyInFlight -> previousState.copy(wifiOnlyLoading = true, wifiOnlyErrorString = null, wifiOnly = false)
        is SettingsResult.ToggleWifiOnlySuccess -> previousState.copy(wifiOnlyLoading = false, wifiOnlyErrorString = null, wifiOnly = result.wifiOnly)
        is SettingsResult.ToggleWifiOnlyError -> previousState.copy(wifiOnlyLoading = false, wifiOnlyErrorString = result.error.localizedMessage, wifiOnly = null)

        is SettingsResult.ChangeThemeInFlight -> previousState.copy(themeLoading = true, themeErrorString = null, theme = null)
        is SettingsResult.ChangeThemeSuccess -> previousState.copy(themeLoading = false, themeErrorString = null, theme = result.theme)
        is SettingsResult.ChangeThemeError -> previousState.copy(themeLoading = false, themeErrorString = result.error.localizedMessage, theme = null)
    }
}

fun settingsActionProcessor(preferencesRepository: IPreferenceRepository) = KontentMasterProcessor<SettingsActions, SettingsResult> { action: Observable<SettingsActions> ->
    Observable.merge(observables(action, preferencesRepository))
}

private fun observables(shared: Observable<SettingsActions>, preferencesRepository: IPreferenceRepository): List<Observable<SettingsResult>> {
    return listOf<Observable<SettingsResult>>(
            shared.ofType(SettingsActions.LoadPreferencesForFirstTime::class.java).compose(loadPreferencesProcessor(preferencesRepository)),
            shared.ofType(SettingsActions.UpdateWorkingDirectory::class.java).compose(updateWorkingDirectoryProcessor(preferencesRepository)),
            shared.ofType(SettingsActions.UpdateWifiOnly::class.java).compose(updateWifiOnlyProcessor(preferencesRepository)),
            shared.ofType(SettingsActions.ChangeTheme::class.java).compose(changeThemeProcessor(preferencesRepository)))
}

fun loadPreferencesProcessor(preferencesRepository: IPreferenceRepository) = KontentActionProcessor<SettingsActions.LoadPreferencesForFirstTime, SettingsResult, Triple<File, Boolean, AppTheme>>(
        action = { action ->
            Observable.zip(preferencesRepository.getWorkingDirectoryPreference(action.context),
                    preferencesRepository.getWifiOnlyPreference(action.context),
                    preferencesRepository.getThemPreference(action.context), Function3<File, Boolean, AppTheme, Triple<File, Boolean, AppTheme>> { file, wifiOnly, theme ->
                Triple(file, wifiOnly, theme)
            })
        },
        success = { (workingDirectory, wifiOnly, theme) ->
            SettingsResult.LoadSettingsSuccess(workingDirectory, wifiOnly, theme)
        },
        error = {
            SettingsResult.LoadSettingsError(it)
        },
        loading = SettingsResult.LoadSettingsinFlight
)

//fun updateWifiOnlyProcessor(preferencesRepository: IPreferenceRepository) = ObservableTransformer { actions: Observable<SettingsActions.UpdateWifiOnly> ->
//    actions.switchMap { action ->
//        preferencesRepository.saveWifiOnlyPreference(action.context, action.selected)
//                .map { SettingsResult.ToggleWifiOnlySuccess(action.selected) as SettingsResult }
//                .onErrorReturn { SettingsResult.ToggleWifiOnlyError(it) }
//                .composeIo()
//                .startWith(SettingsResult.ToggleWifiOnlyInFlight)
//    }
//}
fun updateWifiOnlyProcessor(preferencesRepository: IPreferenceRepository) = KontentActionProcessor<SettingsActions.UpdateWifiOnly, SettingsResult, Boolean>(
        action = { action ->
            preferencesRepository.saveWifiOnlyPreference(action.context, action.selected)
        },
        success = {
            SettingsResult.ToggleWifiOnlySuccess(it)
        },
        error = {
            SettingsResult.ToggleWifiOnlyError(it)
        },
        loading = SettingsResult.ToggleWifiOnlyInFlight
)

fun updateWorkingDirectoryProcessor(preferencesRepository: IPreferenceRepository) = KontentActionProcessor<SettingsActions.UpdateWorkingDirectory, SettingsResult, File>(
        action = { action ->
            preferencesRepository.getWorkingDirectoryPreference(action.context)
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
                        action.newDirectory
                    }
        },
        success = { SettingsResult.UpdateWorkingDirectorySuccess(it) },
        error = { SettingsResult.UpdateWorkingDirectoryError(it) },
        loading = SettingsResult.UpdateWorkingDirectoryInFlight
)

fun changeThemeProcessor(preferencesRepository: IPreferenceRepository) = KontentActionProcessor<SettingsActions.ChangeTheme, SettingsResult, AppTheme>(
        action = { action ->
            preferencesRepository.saveThemePreference(action.context, action.theme)
        },
        success = { SettingsResult.ChangeThemeSuccess(it) },
        error = { SettingsResult.ChangeThemeError(it) },
        loading = SettingsResult.ChangeThemeInFlight
)

fun postProcessor() = KontentPostProcessor<SettingsViewState> {
    var settingsChanged = false
    if (it.originalWifiOnly != it.wifiOnly) settingsChanged = true
    if (it.originalTheme != it.theme) settingsChanged = true
    if (it.originalWorkingDirectory != it.currentWorkingDirectory) settingsChanged = true
    it.copy(settingsChanged = settingsChanged)
}