package com.schiwfty.tex.views.main.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.schiwfty.tex.R
import com.schiwfty.tex.views.addtorrent.AddTorrentActivity
import com.schiwfty.tex.views.all.mvp.AllFragment
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity(), MainContract.View {


    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter()
        presenter.setup(this, this)

        val hash = "49d97805ec30a3a417b06d85e916633f620e62dd"

        showAddTorrentActivity(hash)
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

    override fun showAllFragment() {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.main_content, AllFragment.newInstance())
        transaction.commit()
    }

    override fun showAddTorrentActivity(hash: String) {
        val addTorrentIntent = Intent(this, AddTorrentActivity::class.java)
        addTorrentIntent.putExtra(AddTorrentActivity.ARG_TORRENT_HASH, hash)
        startActivity(addTorrentIntent)
    }


}
