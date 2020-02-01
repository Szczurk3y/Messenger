@file:Suppress("DEPRECATION")

package Fragments

import Activities.UserContentActivity
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
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val submit: Button = view.findViewById(R.id.loginButton)

        submit.setOnClickListener {
            val username = this.username_editText.text.toString()
            val password = this.password_editText.text.toString()
            PerformLogin(User(username, password)).execute()
        }

        return view
    }

    @Suppress("DEPRECATION")
    @SuppressLint("StaticFieldLeak")
    private inner class PerformLogin(val tempUser: User) : AsyncTask<String, String, String>() {
        lateinit var dialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ProgressDialog(this@LoginFragment.context)
            dialog.setMessage("Logging")
            dialog.setCancelable(false)
            dialog.show()
        }
        override fun doInBackground(vararg p0: String?): String {
            Thread.sleep(1000)
            val call: Call<LoginServerResponse> = ServiceBuilder().getInstance()
                .getService()
                .login(tempUser)

            call.enqueue(object : Callback<LoginServerResponse> {
                override fun onFailure(call: Call<LoginServerResponse>, t: Throwable) {
                    Toast.makeText(this@LoginFragment.context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<LoginServerResponse>,
                    response: Response<LoginServerResponse>
                ) {
                    try {
                        val res = response.body()!!
                        Toast.makeText(this@LoginFragment.context, res.message, Toast.LENGTH_LONG).show()
                        if (res.isLogged) {
                            MainActivity.runUserContent(tempUser, res.token)
                        }

                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                }
            })

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (dialog.isShowing()) {
                dialog.dismiss()
            }
        }
    }


}
