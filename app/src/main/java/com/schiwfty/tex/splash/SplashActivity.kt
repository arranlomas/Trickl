package com.schiwfty.tex.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.schiwfty.tex.R
import com.schiwfty.tex.main.MainActivity
import es.dmoral.toasty.Toasty


class SplashActivity : AppCompatActivity(), SplashContract.View {

    val presenter = SplashPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter.setup(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.setClienctAddress()
        presenter.setupConfluenceAsset(this)
    }

    override fun progressToMain() {
        intent = Intent(this, MainActivity::class.java)
        finish()
    }

    override fun showError(stringId: Int) {
        Toasty.error(this, getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(this, getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(this, getString(stringId))
    }

}
