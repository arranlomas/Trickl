package com.shwifty.tex.views.settings.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.shwifty.tex.R
import com.shwifty.tex.views.base.BaseActivity
import com.shwifty.tex.views.settings.state.SettingsReducer
import com.shwifty.tex.views.settings.state.SettingsViewState
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), SettingsContract.View {

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
        render(reducer.getState())
        reducer.getViewStateChangeStream().subscribe { render(it) }
    }

    private fun render(state: SettingsViewState) {
        working_directory.text = state.currentWorkingDirectory.absolutePath
    }
}
