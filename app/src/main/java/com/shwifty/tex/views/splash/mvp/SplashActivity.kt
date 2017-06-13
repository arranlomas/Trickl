package com.shwifty.tex.views.splash.mvp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shwifty.tex.R
import com.shwifty.tex.views.main.mvp.MainActivity
import com.tbruyelle.rxpermissions.RxPermissions
import es.dmoral.toasty.Toasty


class SplashActivity : AppCompatActivity(), SplashContract.View {

    val presenter = SplashPresenter()
    lateinit var rxPermissions: RxPermissions

    companion object{
        val TAG_MAGNET_FROM_INTENT = "arg_magnet_from_intent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        rxPermissions = RxPermissions(this)
        presenter.setup(this, this)
        presenter.handleIntent(intent)
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
        if(presenter.magnet!=null) intent.putExtra(TAG_MAGNET_FROM_INTENT, presenter.magnet)
        startActivity(intent)
    }

    override fun showError(stringId: Int) {
        Toasty.error(this, getString(stringId)).show()
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(this, getString(stringId)).show()
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(this, getString(stringId)).show()
    }

}
