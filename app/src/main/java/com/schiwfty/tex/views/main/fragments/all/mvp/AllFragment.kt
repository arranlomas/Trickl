package com.schiwfty.tex.views.main.fragments.all.mvp

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.tex.R

/**
 * Created by arran on 17/04/2017.
 */
class AllFragment private constructor(): Fragment() {

    companion object{
        fun newInstance(): AllFragment{
            val allFragment = AllFragment()
            val args = Bundle()
            allFragment.arguments = args
            return allFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.frag_all, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}