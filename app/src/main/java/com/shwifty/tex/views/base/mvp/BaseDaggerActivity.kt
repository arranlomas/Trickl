package com.shwifty.tex.views.base.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.shwifty.tex.utils.onCreateSetThemeAndCallSuper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import es.dmoral.toasty.Toasty
import javax.inject.Inject

/**
 * Created by arran on 11/07/2017.
 */
open class BaseDaggerActivity : AppCompatActivity(), BaseContract.MvpView, HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateSetThemeAndCallSuper { super.onCreate(savedInstanceState) }
//                setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_‌​BAR);
    }

    override fun showError(msg: String) {
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showError(stringId: Int) {
        Toasty.error(this, getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(this, getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Info at ${this.javaClass.name}", getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(this, getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Succes at ${this.javaClass.name}", getString(stringId))
    }

    override fun setLoading(loading: Boolean) {
        //do nothing, override in other activities if you want to use
    }
}