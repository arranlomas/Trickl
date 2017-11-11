package com.shwifty.tex.views.settings.mvp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.schiwfty.kotlinfilebrowser.FileBrowserActivity
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.MyApplication
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
    lateinit var interactor: SettingsContract.Interactor

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

        workingDirectoryRootLayout.setOnClickListener {
            Trickl.dialogManager.showChangeWorkingDirectoryRestartRequired(this, {
                FileBrowserActivity.startActivity(this, RC_SELECT_FILE, true, Confluence.workingDir)
            })
        }

        interactor.getViewStateStream().subscribeToEventStream { render(it) }
        render(interactor.getInitialState())
    }

    private fun render(state: SettingsViewState) {
        workingDirectoryField.text = state.currentWorkingDirectory.absolutePath
        if (state.restartApp) (this.application as MyApplication).restart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data.validateOnActivityResult(requestCode, RC_SELECT_FILE, resultCode, Activity.RESULT_OK, {
            val file = it.getSerializable(FileBrowserActivity.ARG_FILE_RESULT) as File
            Trickl.dialogManager.showChangeWorkingDirectoryDialog(this, Confluence.workingDir, file, {
                previousDirectory, newDirectory ->
                interactor.publishEvent(SettingsViewEvent.UpdateWorkingDirectory(this, previousDirectory, newDirectory, moveFiles = true))
            }, { previousDirectory, newDirectory ->
                interactor.publishEvent(SettingsViewEvent.UpdateWorkingDirectory(this, previousDirectory, newDirectory, moveFiles = false))
            })
        })
    }
}
