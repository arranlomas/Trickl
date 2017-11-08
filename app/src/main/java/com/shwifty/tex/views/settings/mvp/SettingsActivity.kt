package com.shwifty.tex.views.settings.mvp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.schiwfty.kotlinfilebrowser.FileBrowserActivity
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.utils.validateOnActivityResult
import com.shwifty.tex.views.base.mvi.BaseMviActivity
import com.shwifty.tex.views.settings.di.DaggerSettingsComponent
import com.shwifty.tex.views.settings.state.SettingsViewEvent
import com.shwifty.tex.views.settings.state.SettingsViewState
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File
import javax.inject.Inject

class SettingsActivity : BaseMviActivity() {
    private val RC_SELECT_FILE = 303

    @Inject
    lateinit var presenter: SettingsContract.Presenter

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        DaggerSettingsComponent.builder().repositoryComponent(Trickl.repositoryComponent).build().inject(this)

        setSupportActionBar(settingsToolbar)
        supportActionBar?.title = getString(R.string.settings_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        settingsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        presenter.getViewStateStream().subscribeToEventStream { render(it) }
        render(presenter.getInitialState())
    }

    private fun render(state: SettingsViewState) {
        workingDirectoryField.text = state.currentWorkingDirectory.absolutePath

        if (state.selectNewWorkingDirectory) FileBrowserActivity.startActivity(this, RC_SELECT_FILE, Confluence.workingDir)
        workingDirectoryRootLayout.setOnClickListener {
            presenter.publishEvent(SettingsViewEvent.SelectNewWorkingDirectory(true))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data.validateOnActivityResult(requestCode, RC_SELECT_FILE, resultCode, Activity.RESULT_OK, {
            presenter.publishEvent(SettingsViewEvent.SelectNewWorkingDirectory(false))
            val file = it.getSerializable(FileBrowserActivity.ARG_FILE_RESULT) as File
            presenter.publishEvent(SettingsViewEvent.UpdateWorkingDirectory(this, file))
        })
    }
}
