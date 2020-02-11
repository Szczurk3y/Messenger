package Fragments

import Activities.UserContentActivity
import Dialogs.ConfirmPasswordDialog
import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat

import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import com.szczurk3y.messenger.*
import kotlinx.android.synthetic.main.fragment_user__profile.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserProfileFragment : Fragment() {

    companion object {
        const val PERMISSION_CODE_READ: Int = 999
        const val IMAGE_PICK_CODE: Int = 997 // UwU
    }

    private lateinit var uri: Uri
    private lateinit var bitmapImage: Bitmap
    lateinit var newUsername: EditText
    lateinit var newPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user__profile, container, false)

        newUsername = view.findViewById(R.id.enterNewUsername) as EditText
        newPassword = view.findViewById(R.id.enterNewPassword) as EditText

        if (ActivityCompat.checkSelfPermission(activity!!.application, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE_READ
            )
        }

        val uploadExternalImageButton = view.findViewById(R.id.uploadExternalImage) as Button
        uploadExternalImageButton.setOnClickListener {
            pickImageFromGallery(null)
        }

        val uploadInternalImageButton = view.findViewById(R.id.uploadInternalImage) as Button
        uploadInternalImageButton.setOnClickListener {
            pickImageFromGallery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }

        val submitButton = view.findViewById(R.id.submitButton) as Button
        submitButton.setOnClickListener {
            if (newUsername.text.toString() != "" || newPassword.text.toString() != "") {
                val confirmPasswordDialog = ConfirmPasswordDialog()
                confirmPasswordDialog.show(fragmentManager, "confirmation")
                confirmPasswordDialog.onPasswordResult = { oldValue, newValue ->
                    if(oldValue == newValue) {
                        Toast.makeText(context, "Confirmed!", Toast.LENGTH_SHORT).show()
                        Update(username = newUsername.text.toString(), password = newPassword.text.toString(), image = bitmapImage).execute()
                    } else {
                        Toast.makeText(context, "Wrong Password!", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                newUsername.error = "Username must contain at least 3 characters..."
                Toast.makeText(context, "Nothing to update...", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun pickImageFromGallery(contentUri: Uri?) {
        val intent = Intent(Intent.ACTION_PICK, contentUri)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data!!
            bitmapImage = MediaStore.Images.Media.getBitmap(activity?.application?.contentResolver, uri)
            imageView.setImageBitmap(bitmapImage)
        }
    }

    inner class Update(
        val username: String = UserContentActivity.user.username,
        val password: String = UserContentActivity.user.password,
        val email: String = UserContentActivity.user.email,
        val image: Bitmap) : AsyncTask<String, String, String>() {

        lateinit var pDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(this@UserProfileFragment.context)
            pDialog.setMessage("Registering")
            pDialog.setCancelable(true)
            pDialog.show()
        }

        override fun doInBackground(vararg p0: String?): String {
            Thread.sleep(1000)
            val updatedUser = UpdatedUser(username = username, email = email, password = password, image = image)
            val call = ServiceBuilder().getInstance().getService().updateProfile(
                UserContentActivity.token,
                updatedUser
            )

            call.enqueue(object : Callback<UpdatedUser> {
                override fun onFailure(call: Call<UpdatedUser>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UpdatedUser>,
                    response: Response<UpdatedUser>
                ) {
                    val res = response.body()!!
                    Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                    val newUser = RegisterUser(username = res.username, email = UserContentActivity.user.email, password = res.password)
                    UserContentActivity.user = newUser
                }

            })

            return "done"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (pDialog.isShowing) {
                pDialog.dismiss()
            }
        }

    }

}
