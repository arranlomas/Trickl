package com.schiwfty.tex.views.main.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.schiwfty.tex.R
import com.schiwfty.tex.views.main.fragments.all.mvp.AllFragment
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity(), MainContract.View {
    val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.setup(this, this)

        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.main_content, AllFragment.newInstance())
        transaction.commit()
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
