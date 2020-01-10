package com.szczurk3y.messenger.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.szczurk3y.messenger.LoginUser
import com.szczurk3y.messenger.MainActivity
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.RetrofitClient
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginFragment : Fragment() {
    lateinit var user: LoginUser
    lateinit var loginView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val submit: Button = view.findViewById(R.id.loginButton)

        submit.setOnClickListener {
            val username = this.username_editText.text.toString()
            val password = this.password_editText.text.toString()
            user = LoginUser(username, password)
            AsyncTaskHandleJson().execute()
        }

        loginView = view
        return view
    }

    @SuppressLint("StaticFieldLeak")
    private inner class AsyncTaskHandleJson : AsyncTask<String, String, String>() {
        lateinit var dialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ProgressDialog(this@LoginFragment.context)
            dialog.setMessage("Logging")
            dialog.setCancelable(false)
            dialog.show()
        }
        override fun doInBackground(vararg p0: String?): String {
            Thread.sleep(2000)
            var res = ""
            val call: Call<ResponseBody> = RetrofitClient.getInstance()
                .aPi
                .login(user)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(loginView.context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        res = response.body()!!.string()
                        Toast.makeText(loginView.context, res, Toast.LENGTH_LONG).show()
                        if (res == "You are now logged in!") {
                            MainActivity.runUserContent()
                        }

                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                }
            })

            return res
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (dialog.isShowing()) {
                dialog.dismiss()
            }
        }
    }
}
