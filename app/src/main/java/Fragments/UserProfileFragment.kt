package Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat

import com.szczurk3y.messenger.R
import android.provider.MediaStore
import androidx.core.graphics.drawable.toIcon
import kotlinx.android.synthetic.main.fragment_user__profile.*
import okhttp3.internal.Internal


class UserProfileFragment : Fragment() {

    companion object {
        const val PERMISSION_CODE_READ: Int = 999
        const val IMAGE_PICK_CODE: Int = 997 // UwU
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user__profile, container, false)

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
        return view
    }

    private fun pickImageFromGallery(contentUri: Uri?) {
        val intent = Intent(Intent.ACTION_PICK, contentUri)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val returnUri: Uri = data?.data!!
            val bitmapImage = MediaStore.Images.Media.getBitmap(activity?.application?.contentResolver, returnUri)
            imageView.setImageBitmap(bitmapImage)
        }
    }

}
