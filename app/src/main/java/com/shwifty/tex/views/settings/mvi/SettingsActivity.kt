package com.shwifty.tex.views.settings.mvi

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.schiwfty.kotlinfilebrowser.FileBrowserActivity
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.models.AppTheme
import com.shwifty.tex.utils.createObservable
import com.shwifty.tex.utils.setVisible
import com.shwifty.tex.utils.validateOnActivityResult
import com.shwifty.tex.views.base.mvi.BaseDaggerMviActivity
import es.dmoral.toasty.Toasty
import io.reactivex.Emitter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File
import javax.inject.Inject


class SettingsActivity : BaseDaggerMviActivity<SettingsActions, SettingsResult, SettingsViewState>() {
    private val RC_SELECT_FILE = 303

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dialogManager: IDialogManager

    private lateinit var newWorkingDirecotyrEmiter: Emitter<SettingsActions.UpdateWorkingDirectory>

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)
        super.setup(viewModel, {
            Toasty.error(this, it.localizedMessage).show()
        })

        setContentView(R.layout.activity_settings)
        setSupportActionBar(settingsToolbar)
        supportActionBar?.title = getString(R.string.settings_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        workingDirectoryRootLayout.setOnClickListener {
            FileBrowserActivity.startActivity(this, RC_SELECT_FILE, true)
        }

        restartButton.setOnClickListener {
            (application as MyApplication).restart()
        }
        super.attachActions(actions(), SettingsActions.LoadPreferencesForFirstTime::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data.validateOnActivityResult(requestCode, RC_SELECT_FILE, resultCode, Activity.RESULT_OK, {
            val file = it.getSerializable(FileBrowserActivity.ARG_FILE_RESULT) as File
            dialogManager.showChangeWorkingDirectoryDialog(this, file, { newDirectory ->
                newWorkingDirecotyrEmiter.onNext(SettingsActions.UpdateWorkingDirectory(this, newDirectory, true))
            }, { newDirectory ->
                newWorkingDirecotyrEmiter.onNext(SettingsActions.UpdateWorkingDirectory(this, newDirectory, false))
            })
        })
    }

    private fun initialAction(): Observable<SettingsActions.LoadPreferencesForFirstTime> {
        return Observable.just(SettingsActions.LoadPreferencesForFirstTime(this))
    }

    private fun updateWorkingDirectoryAction(): Observable<SettingsActions.UpdateWorkingDirectory> = createObservable { newWorkingDirecotyrEmiter = it }

    //TODO
//    private fun toggleWifiOnlyIntent(): Observable<SettingsIntents.ToggleWifiOnly> =
//            RxCompoundButton.checkedChanges(wifiOnlySwich)
//                    .map { isChecked: Boolean -> SettingsIntents.ToggleWifiOnly(this, isChecked) }

    private fun changeThemeAction(): Observable<SettingsActions.ChangeTheme> = RxRadioGroup.checkedChanges(themeRadioGroup)
            .map {
                when (it) {
                    R.id.radioThemeDark -> SettingsActions.ChangeTheme(this, AppTheme.DARK)
                    R.id.radioThemeLight -> SettingsActions.ChangeTheme(this, AppTheme.LIGHT)
                    else -> {
                        Log.v("Error", "Error changing theme")
                        SettingsActions.ChangeTheme(this, AppTheme.DARK)
                    }
                }
            }

    private fun actions() = Observable.merge(listOf(
            initialAction(),
            updateWorkingDirectoryAction(),
//            toggleWifiOnlyIntent(),  TODO
            changeThemeAction()))

    override fun render(state: SettingsViewState) {
        state.currentWorkingDirectory?.absolutePath?.let { workingDirectoryField.text = it }
        workingDirectorySpinner.setVisible(state.workingDirectoryLoading)

        state.workingDirectoryErrorString?.let {
            workingDirectoryError.setVisible(true)
            workingDirectoryError.text = state.workingDirectoryErrorString
        } ?: workingDirectoryError.setVisible(false)

        state.theme?.let {
            when (it) {
                AppTheme.LIGHT -> themeRadioGroup.check(R.id.radioThemeLight)
                AppTheme.DARK -> themeRadioGroup.check(R.id.radioThemeDark)
            }
        }

        snackbarRestart.setVisible(state.settingsChanged)
    }

    override fun onBackPressed() {
        if (viewModel.getLastState().settingsChanged)
            dialogManager.showSettingExitDialog(this, onRestart = {
                (application as MyApplication).restart()
            })
        else super.onBackPressed()
    }
}