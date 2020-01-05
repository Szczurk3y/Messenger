package com.szczurk3y.messenger

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.Color.rgb
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterFragment : Fragment() {
    lateinit var user: RegisterUser
    lateinit var registerView: View

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val submit: Button = view!!.findViewById(R.id.registerButton)
        val switch: Switch = view.findViewById(R.id.registerSwitch)
        val switchText: TextView = view.findViewById(R.id.terms)

        var checked: Boolean = false

        switchText.text = "You are not accepting our terms"

        submit.setOnClickListener {
            if (checked) {
                val username: String = this.registerUsername.text.toString()
                val email: String = this.registerEmail.text.toString()
                val password: String = this.registerPassword.text.toString()
                user = RegisterUser(username, email, password)
                AsyncTaskHandleJson().execute()
            } else {
                Toast.makeText(view.context, "Accept our terms bitch", Toast.LENGTH_LONG).show()
            }
        }
        switch.setOnClickListener {
            when(checked) {
                true -> {
                    switchText.setTextColor(Color.GRAY)
                    switchText.text = "You are not accepting our terms"
                    checked = false
                }
                false -> {
                    switchText.setTextColor(rgb(51, 204, 51))
                    switchText.text = "You are accepting our terms"
                    checked = true
                }
            }
        }
        registerView = view
        return view
    }

    @SuppressLint("StaticFieldLeak")
    private inner class AsyncTaskHandleJson : AsyncTask<String, String, String>() {
        lateinit var pDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(this@RegisterFragment.context)
            pDialog.setMessage("Registering")
            pDialog.setCancelable(false)
            pDialog.show()
        }
        override fun doInBackground(vararg p0: String?): String {
            Thread.sleep(2000)
            var res = ""
            val call: Call<ResponseBody> = RetrofitClient
                .getInstance()
                .aPi
                .register(user)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(registerView.context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        res = response.body()!!.string()
                        Toast.makeText(registerView.context, res, Toast.LENGTH_LONG).show()
                        val username = registerView.findViewById<EditText>(R.id.registerUsername)
                        val email = registerView.findViewById<EditText>(R.id.registerEmail)
                        val password = registerView.findViewById<EditText>(R.id.registerPassword)
                        username.setText("")
                        username.clearFocus()
                        email.setText("")
                        email.clearFocus()
                        password.setText("")
                        password.clearFocus()

                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                }
            })

            return res
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (pDialog.isShowing()) {
                pDialog.dismiss()
            }
        }
    }
}
