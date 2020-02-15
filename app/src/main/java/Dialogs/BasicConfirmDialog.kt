package Dialogs

import Activities.UserContentActivity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import kotlin.properties.Delegates

class BasicConfirmDialog(val message: String) : DialogFragment() {

    private lateinit var password: EditText

    var result: String by Delegates.observable("Cancel") { _, oldValue, newValue ->
        onConfirmResult?.invoke(oldValue, newValue)
    }

    var onConfirmResult: ((String, String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setPositiveButton("Confirm") { dialog, id ->
                    result = "Confirm"
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.dismiss()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}