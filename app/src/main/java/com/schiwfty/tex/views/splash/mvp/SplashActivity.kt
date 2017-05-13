package com.schiwfty.tex.views.splash.mvp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.schiwfty.tex.R
import com.schiwfty.tex.views.main.mvp.MainActivity
import com.tbruyelle.rxpermissions.RxPermissions
import es.dmoral.toasty.Toasty


class SplashActivity : AppCompatActivity(), SplashContract.View {

    val presenter = SplashPresenter()
    lateinit var rxPermissions: RxPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        rxPermissions = RxPermissions(this)
        presenter.setup(this, this)
    }

    override fun onStart() {
        super.onStart()
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe ({
                    if (it!=null) {
                        presenter.startConfluenceDaemon(this)
                    } else {
                        showError(R.string.write_permissions_error)
                    }
                },{
                    it.printStackTrace()
                })
    }

    override fun progressToMain() {
        finish()
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
