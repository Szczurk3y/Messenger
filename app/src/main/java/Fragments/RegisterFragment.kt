package Fragments

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
import com.szczurk3y.messenger.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterFragment : Fragment() {
    lateinit var user: User
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
                user = User(username.text.toString(), email.text.toString(), password.text.toString())
                Registration().execute()
            } else {
                Toast.makeText(registerView.context, "Accept our terms bitch", Toast.LENGTH_LONG).show()
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
    private inner class Registration : AsyncTask<String, String, String>() {
        lateinit var pDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(this@RegisterFragment.context)
            pDialog.setMessage("Registering")
            pDialog.setCancelable(false)
            pDialog.show()
        }
        override fun doInBackground(vararg p0: String?): String {
            Thread.sleep(1000)
            val call: Call<RegistrationServerResponse> = ServiceBuilder().getInstance()
                .getService()
                .register(user)

            call.enqueue(object : Callback<RegistrationServerResponse> {
                override fun onFailure(call: Call<RegistrationServerResponse>, t: Throwable) {
                    Toast.makeText(registerView.context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<RegistrationServerResponse>,
                    response: Response<RegistrationServerResponse>
                ) {
                    try {
                        val res = response.body()!!
                        Toast.makeText(registerView.context, res.message, Toast.LENGTH_LONG).show()
                        if (res.isRegistered) {
                            username.setText("")
                            username.clearFocus()
                            email.setText("")
                            email.clearFocus()
                            password.setText("")
                            password.clearFocus()
                            switch.isChecked = false
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
            if (pDialog.isShowing) {
                pDialog.dismiss()
            }
        }
    }
}
