package com.shwifty.tex.views.settings.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.views.base.BaseActivity
import com.shwifty.tex.views.settings.di.DaggerSettingsComponent
import com.shwifty.tex.views.settings.state.SettingsReducer
import com.shwifty.tex.views.settings.state.SettingsViewState
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : BaseActivity(), SettingsContract.View {

    @Inject
    lateinit var presenter: SettingsContract.Presenter

    private val reducer = SettingsReducer()

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        DaggerSettingsComponent.builder().networkComponent(Trickl.networkComponent).build().inject(this)
        presenter.attachView(this)

        setSupportActionBar(settingsToolbar)
        supportActionBar?.title = getString(R.string.settings_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        settingsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        render(reducer.getState())
        reducer.getViewStateChangeStream().subscribe { render(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun render(state: SettingsViewState) {
        workingDirectoryField.text = state.currentWorkingDirectory.absolutePath
        workingDirectoryRootLayout.setOnClickListener {
            Log.v("Change WD", "CLICKED")
        }
    }
}
