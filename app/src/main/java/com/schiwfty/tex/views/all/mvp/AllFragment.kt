package com.schiwfty.tex.views.all.mvp

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.tex.R
import com.schiwfty.tex.views.all.list.AllAdapter
import com.schiwfty.tex.views.main.mvp.AllPresenter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_all.*

/**
 * Created by arran on 17/04/2017.
 */
class AllFragment private constructor() : Fragment(), AllContract.View {
    lateinit var presenter: AllContract.Presenter
    lateinit var adapter: AllAdapter

    companion object {
        fun newInstance(): AllFragment {
            val allFragment = AllFragment()
            val args = Bundle()
            allFragment.arguments = args
            return allFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AllPresenter()
        presenter.setup(activity, this)
        adapter = AllAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.frag_all, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        all_recycler_view.setHasFixedSize(true)
        all_recycler_view.layoutManager = LinearLayoutManager(activity)
        all_recycler_view.adapter = adapter
    }

    override fun showError(stringId: Int) {
        Toasty.error(activity, getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(activity, getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(activity, getString(stringId))
    }
}