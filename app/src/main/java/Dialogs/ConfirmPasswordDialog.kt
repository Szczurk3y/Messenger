package Dialogs

import Activities.UserContentActivity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.szczurk3y.messenger.R
import java.io.IOException
import java.lang.IllegalStateException

class ConfirmPasswordDialog : DialogFragment() {

    lateinit var password: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_confirmpassword, null)
        fetchViews(view)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(view)
                .setPositiveButton(R.string.submit, DialogInterface.OnClickListener { dialog, id ->
                    try {
                        var message = "Confirmed!"
                        if (password.text.toString() != UserContentActivity.user.password)
                            message = "Wrong password!"
                        Toast.makeText(it.applicationContext, message, Toast.LENGTH_SHORT).show()
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    getDialog().cancel()
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun fetchViews(view: View) {
        password = view.findViewById(R.id.password)
    }
}