package com.schiwfty.tex.realm

import io.realm.RealmObject



/**
 * Created by arran on 16/05/2017.
 */
open class RealmString(
        var value: String? = null
) : RealmObject()