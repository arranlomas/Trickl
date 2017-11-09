package com.shwifty.tex.views.settings.mvp

import android.content.Context
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.base.mvi.BaseMviPresenter
import com.shwifty.tex.views.settings.state.SettingsViewEvent
import com.shwifty.tex.views.settings.state.SettingsViewState
import java.io.File

/**
 * Created by arran on 7/05/2017.
 */
class SettingsPresenter(private val reducer: SettingsContract.Reducer, private val preferencesRepository: IPreferenceRepository)
    : BaseMviPresenter<SettingsViewState, SettingsViewEvent, SettingsContract.Reducer>(reducer), SettingsContract.Presenter {

    /**
     * This is where we manage side effects like network requests and writing to disk
     * Also where we can do any business logic
     */
    override fun publishEvent(event: SettingsViewEvent) {
        super.publishEvent(event)
        when (event) {
            is SettingsViewEvent.UpdateWorkingDirectory -> saveWorkingDirectory(event.context, event.newDirectory)
        }
    }

    private fun saveWorkingDirectory(context: Context, file: File) {
        preferencesRepository.saveWorkingDirectoryPreference(context, file)
                .subscribe(object : BaseSubscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        reducer.reduce(SettingsViewEvent.WorkingDirectoryUpdated(true))
                    }
                })
    }

}