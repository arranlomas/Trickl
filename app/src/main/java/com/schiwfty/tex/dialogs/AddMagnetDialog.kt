package com.schiwfty.tex.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.tex.R
import android.content.DialogInterface



/**
 * Created by arran on 10/05/2017.
 */

class AddMagnetDialog: DialogFragment(){

    companion object{
        fun newInstance(): AddMagnetDialog{
            return AddMagnetDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val b = AlertDialog.Builder(activity)
                .setTitle("Paste magnet link here")
                .setPositiveButton("OK",
                        { dialog, whichButton ->
                            // do something...
                        }
                )
                .setNegativeButton("Cancel",
                        { dialog, whichButton -> dialog.dismiss() }
                )

        val inflater = activity.layoutInflater
        val v = inflater?.inflate(R.layout.dialog_frag_add_magnet, null) ?: throw IllegalStateException("View should not be null!")
        b.setView(v)
        return b.create()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
