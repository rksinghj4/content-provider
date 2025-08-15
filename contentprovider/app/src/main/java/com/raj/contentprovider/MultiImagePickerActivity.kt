package com.raj.contentprovider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.raj.contentprovider.ui.theme.ContentProviderTheme

class MultiImagePickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentProviderTheme {
                MultiImageWithButton()
            }
        }
    }

    companion object {
        fun show(fromActivity: Activity) {
            Intent(fromActivity, MultiImagePickerActivity::class.java).also {
                fromActivity.startActivity(it)
            }
        }
    }
}

@Composable
fun MultiImageWithButton() {
    var imageUris by rememberSaveable {
        mutableStateOf<List<Uri?>>(emptyList())
    }
    //ActivityResultContracts.PickVisualMedia() -  For single VisualMediaMedia, (ImageOnly or VideoOnly)
    //ActivityResultContracts.PickMultipleVisualMedia() - ImageAndVideo
    val resultContract =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { pickedUris: List<@JvmSuppressWildcards Uri> ->
                //It is single image picker
                imageUris = pickedUris
            }
        )


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                //ImageAndVideo - Picker will show both Images and Videos
                //ImageOnly - Launch the Image Picker: which will show you just images (filter for images)
                resultContract.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
            Text(text = "Pick Image")
        }

        Spacer(modifier = Modifier.height(20.dp))

        imageUris.isNotEmpty().let {
            LazyColumn {
                items(items = imageUris) { uri ->
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
                }
            }

        }
    }
}