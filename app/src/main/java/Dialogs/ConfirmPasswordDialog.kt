package Dialogs

import Activities.UserContentActivity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.szczurk3y.messenger.R
import java.io.IOException
import java.lang.IllegalStateException
import kotlin.properties.Delegates

class ConfirmPasswordDialog : DialogFragment() {
    private lateinit var password: EditText

    var passwordResult: String by Delegates.observable(UserContentActivity.user.password) { _, oldValue, newValue ->
        onPasswordResult?.invoke(oldValue, newValue)
    }

    var onPasswordResult: ((String, String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_confirmpassword, null)
        fetchViews(view)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(view)
                .setPositiveButton(R.string.submit) { dialog, id ->
                    passwordResult = password.text.toString()
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    getDialog().cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun fetchViews(view: View) {
        password = view.findViewById(R.id.password)
    }

}