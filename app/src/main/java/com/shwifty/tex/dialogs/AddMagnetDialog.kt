package com.shwifty.tex.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.EditText
import com.pawegio.kandroid.find
import com.pawegio.kandroid.textWatcher
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.main.mvp.MainContract


/**
 * Created by arran on 10/05/2017.
 */

class AddMagnetDialog: DialogFragment(){
    private var magnetText = ""

    lateinit var mainPresenter: MainContract.Presenter

    companion object{
        fun newInstance(): AddMagnetDialog{
            return AddMagnetDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mainPresenter = TricklComponent.mainComponent.getMainPresenter()

        val b = AlertDialog.Builder(activity)
                .setTitle("Paste magnet link here")
                .setPositiveButton("OK",
                        { _, _ ->
                           mainPresenter.showAddTorrentActivity(magnet = magnetText)
                        }
                )
                .setNegativeButton("Cancel",
                        { dialog, _ -> dialog.dismiss() }
                )

        val inflater = activity.layoutInflater
        val v = inflater?.inflate(R.layout.dialog_frag_add_magnet, null) ?: throw IllegalStateException("View should not be null!")
        b.setView(v)
        val editText = v.find<EditText>(R.id.addMagnetDialogEditText)
        magnetText = editText.text.toString()
        editText.textWatcher {
            afterTextChanged { text -> magnetText = text.toString()}
        }
        return b.create()
    }

}
