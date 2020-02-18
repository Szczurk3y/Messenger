package Fragments

import Activities.UserContentActivity
import Dialogs.AlertDialog
import Dialogs.BasicConfirmDialog
import Dialogs.ConfirmPasswordDialog
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
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
import androidx.core.app.ActivityCompat

import android.provider.MediaStore
import android.widget.*
import com.szczurk3y.messenger.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class UserProfileFragment : Fragment() {

    companion object {
        const val PERMISSION_CODE_READ: Int = 999
        const val IMAGE_PICK_CODE: Int = 997 // UwU
        @SuppressLint("StaticFieldLeak")
        var bitmapImage: Bitmap? = null
    }

    private var uri: Uri? = null
    private var inputStream: InputStream? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user__profile, container, false)
        view.findViewById<TextView>(R.id.usernameDisplay).text = UserContentActivity.user.username
        if (ActivityCompat.checkSelfPermission(activity!!.application, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE_READ
            )
        }

        if (bitmapImage != null) {
            val imageView = view?.findViewById<ImageView>(R.id.imageView)!!
            imageView.setImageBitmap(bitmapImage)
        }


        val uploadInternalImageButton = view.findViewById(R.id.uploadInternalImage) as Button
        uploadInternalImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            try {
                startActivityForResult(intent, IMAGE_PICK_CODE)
            } catch (err: ActivityNotFoundException) {
                err.printStackTrace()
            }
        }

        val setDefaultImage = view.findViewById(R.id.setDefaultImage) as Button
        setDefaultImage.setOnClickListener {
            val dialog = BasicConfirmDialog("Are you sure?")
            dialog.show(fragmentManager, "Confirmation")
            dialog.onConfirmResult = { _, newValue ->
                if (newValue == "Confirm") {
                    val imageView = view?.findViewById<ImageView>(R.id.imageView)
                    imageView?.setImageResource(R.mipmap.profile_image)
                    inputStream = null
                }
            }
        }

        val submitButton = view.findViewById(R.id.submitButton) as Button
        submitButton.setOnClickListener {
            val usernameEditText = view.findViewById<EditText>(R.id.enterNewUsername)
            val enterPasswordEditText = view.findViewById<EditText>(R.id.enterNewPassword)
            val repeatPasswordEditText = view.findViewById<EditText>(R.id.repeatNewPassword)

            var username = usernameEditText.text.toString()
            var password = repeatPasswordEditText.text.toString()

            if (enterPasswordEditText.text.toString() != repeatPasswordEditText.text.toString()) {
                enterPasswordEditText.error = "The entered passwords do not match"
                repeatPasswordEditText.error = "The entered passwords do not match"
            } else {
                if (username.isEmpty() && password.isEmpty() && inputStream == null) {
                    val dialog = AlertDialog("Nothing to update")
                    dialog.show(fragmentManager, "Unlucky :(")
                } else {
                    val confirmPasswordDialog = ConfirmPasswordDialog()
                    confirmPasswordDialog.show(fragmentManager, "Confirm password")
                    confirmPasswordDialog.onPasswordResult = { oldValue, newValue ->
                        if (oldValue != newValue) {
                            Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                        } else if (oldValue == newValue) {
                            if (username.isEmpty()) username = UserContentActivity.user.username
                            if (password.isEmpty()) password = UserContentActivity.user.password
                            UpdateProfile(
                                imageBytes = getBytes(inputStream),
                                tempUsername = username,
                                tempPassword = password,
                                tempEmail = UserContentActivity.user.email
                            ).execute()
                        }
                    }
                }
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_PICK_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    assert(data != null)
                    uri = data?.data!!
                    inputStream = context?.contentResolver?.openInputStream(uri!!)
                    bitmapImage = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                    val imageView = view?.findViewById<ImageView>(R.id.imageView)!!
                    imageView.setImageBitmap(bitmapImage)
                } catch (err: IOException) {
                    err.printStackTrace()
                }
            }
        }
    }

    fun getBytes(tempInputStream: InputStream?) : ByteArray? {
        tempInputStream?.let {
            val byteBuffer = ByteArrayOutputStream()
            val buff = ByteArray(1024)
            var len = tempInputStream.read(buff)
            while (len != -1) {
                byteBuffer.write(buff, 0, len)
                len = tempInputStream.read(buff)
            }
            return byteBuffer.toByteArray()
        }
        return null
    }


    @Suppress("DEPRECATION")
    @SuppressLint("StaticFieldLeak")
    inner class UpdateProfile(
        val imageBytes: ByteArray?,
        val tempUsername: String?,
        val tempPassword: String?,
        val tempEmail: String
    ) : AsyncTask<String, String, String>() {

        lateinit var dialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ProgressDialog(context)
            dialog.setTitle("Updating...")
            dialog.setCancelable(false)
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.max = 100
            dialog.show()
        }

        override fun doInBackground(vararg p0: String?): String {
            Thread.sleep(1000)

            val requestFile: RequestBody? = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes!!)

            val newImage: MultipartBody.Part? = MultipartBody.Part.createFormData("image", "${tempUsername}.jpg", requestFile!!)
            val newUsername: MultipartBody.Part? = MultipartBody.Part.createFormData("username", tempUsername!!)
            val newPassword: MultipartBody.Part? = MultipartBody.Part.createFormData("password", tempPassword!!)
            val email: MultipartBody.Part = MultipartBody.Part.createFormData("email", tempEmail)

            val call = ServiceBuilder().getInstance().getService().updateProfile(
                UserContentActivity.token,
                newImage,
                newUsername,
                newPassword,
                email
            )

            call.enqueue(object : Callback<UpdatedUserServerResponse> {
                override fun onFailure(call: Call<UpdatedUserServerResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UpdatedUserServerResponse>,
                    response: Response<UpdatedUserServerResponse>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        res?.let {
                            if (res.isUpdated) {
                                val updatedUser = User(tempUsername, tempEmail, tempPassword)
                                UserContentActivity.user = updatedUser
                                view?.findViewById<TextView>(R.id.usernameDisplay)?.setText(tempUsername)
                                view?.findViewById<EditText>(R.id.enterNewUsername)?.setText("")
                                view?.findViewById<EditText>(R.id.enterNewPassword)?.setText("")
                                view?.findViewById<EditText>(R.id.repeatNewPassword)?.setText("")
                            }
                            Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Some server-side error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            })

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }
}
