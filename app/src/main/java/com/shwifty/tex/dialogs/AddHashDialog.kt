package com.shwifty.tex.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.EditText
import com.pawegio.kandroid.find
import com.pawegio.kandroid.textWatcher
import com.shwifty.tex.R
import com.shwifty.tex.views.main.MainEventHandler


/**
 * Created by arran on 10/05/2017.
 */

class AddHashDialog : DialogFragment() {
    private var hashText = ""

    companion object {
        fun newInstance(): AddHashDialog {
            return AddHashDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val b = AlertDialog.Builder(activity)
                .setTitle("Paste torrent hash here")
                .setPositiveButton("OK",
                        { _, _ ->
                            MainEventHandler.addHash(hashText)
                        }
                )
                .setNegativeButton("Cancel",
                        { dialog, _ -> dialog.dismiss() }
                )

        val inflater = activity.layoutInflater
        val v = inflater?.inflate(R.layout.dialog_frag_add_hash, null) ?: throw IllegalStateException("View should not be null!")
        b.setView(v)
        val editText = v.find<EditText>(R.id.addHashDialogEditText)
        editText.textWatcher {
            afterTextChanged { text -> hashText = text.toString() }
        }
        return b.create()
    }
}
