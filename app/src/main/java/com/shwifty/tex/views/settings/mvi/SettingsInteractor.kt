package com.shwifty.tex.views.settings.mvi

import android.content.Context
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.utils.composeIo
import com.shwifty.tex.views.base.mvi.BaseMviInteractor
import com.shwifty.tex.views.settings.mvp.SettingsContract
import java.io.File

/**
 * Created by arran on 7/05/2017.
 */
class SettingsInteractor(private val reducer: SettingsContract.Reducer, private val preferencesRepository: IPreferenceRepository)
    : BaseMviInteractor<SettingsViewState, SettingsIntents, SettingsContract.Reducer>(reducer), SettingsContract.Interactor {

    override fun publishEvent(event: SettingsIntents) {
        when (event) {
            is SettingsIntents.UpdateWorkingDirectory -> handleChangeDirectory(event, saveWorkingDirectory)
                    .composeIo()
                    .subscribe(BaseSubscriber<ChangeWorkingDirectoryResult>({
                        when (it) {
                            is ChangeWorkingDirectoryResult.CannotChange -> reducer.reduce(SettingsIntents.UpdateWorkingDirectoryShowError(it.error))
                            is ChangeWorkingDirectoryResult.Changed -> super.publishEvent(SettingsIntents.RestartClient())
                        }
                    }, {
                        reducer.reduce(SettingsIntents.UpdateWorkingDirectoryShowErrorString(it.localizedMessage))
                    }, {
                        reducer.reduce(SettingsIntents.UpdateWorkingDirectoryShowLoading(it))
                    }))
                    .addSubscription()
            else -> super.publishEvent(event)
        }
    }

    val saveWorkingDirectory: (Context, File) -> Unit = { context, file ->
        preferencesRepository.saveWorkingDirectoryPreference(context, file)
                .subscribe(BaseSubscriber<Boolean>(
                        onNextAction = {},
                        onErrorAction = { reducer.reduce(SettingsIntents.UpdateWorkingDirectoryShowErrorString(it.localizedMessage)) },
                        onLoadingEvent = { reducer.reduce(SettingsIntents.UpdateWorkingDirectoryShowLoading(it)) }))
                .addSubscription()
    }

}