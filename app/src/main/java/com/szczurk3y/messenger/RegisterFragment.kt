package com.szczurk3y.messenger

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterFragment : Fragment() {
    lateinit var user: User
    lateinit var registerView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val submit: Button = view!!.findViewById(R.id.registerButton)
        submit.setOnClickListener {
            val username: String = this.registerUsername.text.toString()
            val email: String = this.registerEmail.text.toString()
            val password: String = this.registerPassword.text.toString()
            user = User(username, email, password)
            AsyncTaskHandleJson().execute()
        }
        registerView = view
        return view
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskHandleJson : AsyncTask<String, String, String>() {
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
                    res = "error"
                    Toast.makeText(registerView.context, res, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        res = Gson().toJson(response.body())
                        Toast.makeText(registerView.context, res, Toast.LENGTH_LONG).show()
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
