package com.shwifty.tex.utils

import android.app.Activity
import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.shwifty.tex.R

/**
 * Created by arran on 20/10/2017.
 */
fun getStyledDrawerItem(@StringRes stringRes: Int, identifier: Long = -1): PrimaryDrawerItem {
    return PrimaryDrawerItem().withIdentifier(identifier)
            .withName(stringRes)
            .withSelectedTextColorRes(R.color.white).withSelectedColorRes(R.color.colorPrimary)
            .withTextColorRes(R.color.white)
}

fun getDrawer(activity: Activity, toolbar: Toolbar): Drawer {
    return DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withSliderBackgroundColorRes(R.color.colorPrimary)
            .build()
}

fun Drawer.setItemClick(onClick: (Long) -> Unit) {
    onDrawerItemClickListener = Drawer.OnDrawerItemClickListener { view, position, drawerItem ->
        onClick.invoke(drawerItem.identifier)
        true
    }
}
