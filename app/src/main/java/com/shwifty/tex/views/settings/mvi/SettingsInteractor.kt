package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.utils.ValidateChangeWorkingDirectoryResult
import com.shwifty.tex.utils.composeIo
import com.shwifty.tex.utils.validateWorkingDirectoryCanBeChanged
import com.shwifty.tex.views.settings.mvp.SettingsContract
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 7/05/2017.
 */
class SettingsInteractor(private val preferencesRepository: IPreferenceRepository) : SettingsContract.Interactor {

    override fun attachView(intents: Observable<SettingsIntents>): Observable<SettingsViewState> {
        return intents
                .map { intent -> actionFromIntent(intent) }
                .compose(settingsActionProcessor)
                .scan(SettingsViewState.default(), reducer)
    }

    fun actionFromIntent(intent: SettingsIntents): SettingsActions = when (intent) {
        is SettingsIntents.NewWorkingDirectorySelected -> SettingsActions.ClearErrorsAndUpdateWorkingDirectory(intent.context, intent.previousDirectory, intent.newDirectory, intent.moveFiles)
        is SettingsIntents.RestartApp -> SettingsActions.RestartApp
    }


    val settingsActionProcessor: ObservableTransformer<SettingsActions, SettingsResult> =
            ObservableTransformer { action: Observable<SettingsActions> ->
                action.publish { shared ->
                    Observable.merge(
                            shared.ofType(SettingsActions.ClearErrorsAndUpdateWorkingDirectory::class.java).compose(updateWorkingDirectoryProcessor),
                            shared.ofType(SettingsActions.RestartApp::class.java).compose(restartAppProcessor)
                    )
                }
            }

    val restartAppProcessor = ObservableTransformer<SettingsActions.RestartApp, SettingsResult> { action: Observable<SettingsActions.RestartApp> ->
        action.switchMap {
            Observable.just(SettingsResult.RestartApp)
        }
    }

    val updateWorkingDirectoryProcessor = ObservableTransformer<SettingsActions.ClearErrorsAndUpdateWorkingDirectory, SettingsResult> { actions: Observable<SettingsActions.ClearErrorsAndUpdateWorkingDirectory> ->
        actions.flatMap { action ->
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

    val reducer = BiFunction <SettingsViewState, SettingsResult, SettingsViewState> {
        previousState: SettingsViewState, result: SettingsResult ->
        when (result) {
            is SettingsResult.UpdateWorkingDirectoryInFlight -> previousState.copy(workingDirectoryState = previousState.workingDirectoryState.copy(isLoading = true))
            is SettingsResult.UpdateWorkingDirectorySuccess -> previousState.copy(workingDirectoryState = previousState.workingDirectoryState.copy(isLoading = false, currentWorkingDirectory = result.newFile))
            is SettingsResult.UpdateWorkingDirectoryError -> previousState.copy(workingDirectoryState = previousState.workingDirectoryState.copy(isLoading = false, errorString = result.error.localizedMessage))
            SettingsResult.RestartApp -> previousState.copy(restartAppState = previousState.restartAppState.copy(restart = true))
        }
    }
}

