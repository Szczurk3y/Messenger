package com.szczurk3y.messenger.Fragments

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
import com.szczurk3y.messenger.R
import com.szczurk3y.messenger.RegisterUser
import com.szczurk3y.messenger.RetrofitClient
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterFragment : Fragment() {
    lateinit var user: RegisterUser
    lateinit var registerView: View
    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var switch: Switch
    lateinit var switchTextView: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registerView = inflater.inflate(R.layout.fragment_register, container, false)

        switch = registerView.findViewById(R.id.registerSwitch)
        username = registerView.findViewById(R.id.registerUsername)
        email = registerView.findViewById(R.id.registerEmail)
        password = registerView.findViewById(R.id.registerPassword)
        switch = registerView.findViewById(R.id.registerSwitch)
        switchTextView = registerView.findViewById(R.id.terms)
        switchTextView.text = getString(R.string.switch_off)

        val submit: Button = registerView.findViewById(R.id.registerButton)
        submit.setOnClickListener {
            if (switch.isChecked) {
                user = RegisterUser(username.text.toString(), email.text.toString(), password.text.toString())
                AsyncTaskHandleJson().execute()
            } else {
                Toast.makeText(registerView.context, "Accept the terms bitch", Toast.LENGTH_LONG).show()
            }
        }

        switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                when(isChecked) {
                    true -> {
                        switchTextView.setTextColor(rgb(51, 204, 51))
                        switchTextView.text = getString(R.string.switch_on)
                    }
                    false -> {
                        switchTextView.setTextColor(Color.GRAY)
                        switchTextView.text = getString(R.string.switch_off)
                    }
                }
            }
        })

        return registerView
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
            val call: Call<ResponseBody> = RetrofitClient.getInstance()
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
                        username.setText("")
                        username.clearFocus()
                        email.setText("")
                        email.clearFocus()
                        password.setText("")
                        password.clearFocus()
                        switch.isChecked = false

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
