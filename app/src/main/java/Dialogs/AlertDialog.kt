package Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class AlertDialog(val message: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        this.dismiss()
                    })
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

}