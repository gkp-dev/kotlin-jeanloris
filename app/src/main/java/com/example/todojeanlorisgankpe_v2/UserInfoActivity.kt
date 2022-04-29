package com.example.todojeanlorisgankpe_v2

import android.Manifest
import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.todojeanlorisgankpe_v2.network.Api
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.permissions.RequestAccess
import com.google.modernstorage.permissions.StoragePermissions
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // this est le bitmap dans ce contexte
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    private val getPhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        var imageView = findViewById<ImageView>(R.id.image_view)
        imageView.load(bitmap)
        lifecycleScope.launch {
            Api.userWebService.updateAvatar(bitmap!!.toRequestBody())
        }

    }

    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .setAction("Open Settings") {
                val intent = Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivity(intent)
            }
            .show()
    }

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) {
                getPhoto.launch()
            }
            else{
                showMessage("You don't allow us the application to take picture")
            }
        }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)

        if(isAlreadyAccepted){
            getPhoto.launch()
        }
        else if (isExplanationNeeded){
            showMessage("You need to allow the camera permission")
        }
        else{
            requestCamera.launch(camPermission)
        }

    }

    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = fileBody
        )
    }

    // register
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // au retour de la galerie on fera quasiment pareil qu'au retour de la caméra mais avec une URI àla place du bitmap
        lifecycleScope.launch {

            var imageView = findViewById<ImageView>(R.id.image_view)
            imageView.load(uri) {
                error(R.drawable.ic_launcher_background)
            }
            Api.userWebService.updateAvatar(uri!!.toRequestBody())
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()
            var imageView = findViewById<ImageView>(R.id.image_view)
            imageView.load(userInfo?.avatar) {
                error(R.drawable.ic_launcher_background)
            }
        }

        // upload Picture
        var uploadPictureView = findViewById<Button>(R.id.upload_image_button)
        uploadPictureView?.setOnClickListener {
            galleryLauncher.launch("image/*")
        }




        // Take Picture
        var takePictureView = findViewById<Button>(R.id.take_picture_button)
        takePictureView?.setOnClickListener {
            launchCameraWithPermission()
        }



        // launcher pour la permission d'accès au stockage
        val requestReadAccess = registerForActivityResult(RequestAccess()) { hasAccess ->
            if (hasAccess) {
               // Open Gallery
            } else {
                showMessage("Don't have access to the gallery")
            }
        }

        fun openGallery() {
            requestReadAccess.launch(
                RequestAccess.Args(
                    action = StoragePermissions.Action.READ,
                    types = listOf(StoragePermissions.FileType.Image),
                    createdBy = StoragePermissions.CreatedBy.AllApps
                )
            )
        }


    }


}