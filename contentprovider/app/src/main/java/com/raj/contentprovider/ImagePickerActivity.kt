package com.raj.contentprovider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.raj.contentprovider.ui.theme.ContentProviderTheme

class ImagePickerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentProviderTheme {
                ImageWithButton()
            }
        }
    }

    companion object {

        fun show(fromActivity: Activity) {
            Intent(fromActivity, ImagePickerActivity::class.java).also {
                fromActivity.startActivity(it)
            }
        }
    }
}

@Composable
fun ImageWithButton() {
    var imageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }
    // ActivityResultContracts.GetContent() or ActivityResultContracts.PickVisualMedia() - for single image
    //ActivityResultContracts.GetContent() k sath resultContract.launch("image/*") - for single image

    val managedActivityResultLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { pickedUri: Uri? ->
                //It is single image picker
                imageUri = pickedUri
            }
        )//Will launch the Gallery


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUri?.let { uri ->
            //Two way to load image single Uri
            // 1. AsyncImage
            // 2. Use rememberAsyncImagePainter in Image
            AsyncImage(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(fraction = .3f),
                model = uri, //We can pass Uri or Url Any?
                contentScale = ContentScale.Crop,
                contentDescription = "picked-img"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(fraction = .5f),
                contentScale = ContentScale.Crop,
                //rememberAsyncImagePainter is part of coil library
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "picked-img"
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                managedActivityResultLauncher.launch(
                    //This PickVisualMediaRequest is same for single image or multiple image with ImageOnly
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                //Launch the Image Picker:
                //resultContract.launch("image/*") if above using ActivityResultContracts.GetContent()
            }) {
            Text(text = "Pick Image")
        }
    }
}