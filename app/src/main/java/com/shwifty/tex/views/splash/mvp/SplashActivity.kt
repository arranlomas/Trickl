package com.shwifty.tex.views.splash.mvp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.shwifty.tex.R
import com.shwifty.tex.views.base.mvp.BaseActivity
import com.shwifty.tex.views.main.mvp.MainActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import es.dmoral.toasty.Toasty


class SplashActivity : BaseActivity(), SplashContract.View {

    val presenter = SplashPresenter()
    lateinit var rxPermissions: RxPermissions

    companion object {
        const val TAG_MAGNET_FROM_INTENT = "arg_magnet_from_intent"
        const val TAG_TORRENT_FILE_FROM_INTENT = "arg_torrent_file_from_intent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        rxPermissions = RxPermissions(this)
        presenter.attachView(this)
        presenter.handleIntent(intent, contentResolver)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onStart() {
        super.onStart()
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({
                    if (it != null && it) {
                        presenter.startConfluenceDaemon(this)
                    } else {
                        showError(R.string.write_permissions_error)
                    }
                }, {
                    it.printStackTrace()
                })
    }

    override fun progressToMain() {
        finish()
        intent = Intent(this, MainActivity::class.java)
        if (presenter.magnet != null) intent.putExtra(TAG_MAGNET_FROM_INTENT, presenter.magnet)
        if (presenter.torrentFile != null) intent.putExtra(TAG_TORRENT_FILE_FROM_INTENT, presenter.torrentFile)
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
