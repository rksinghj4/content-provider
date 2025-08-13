package com.raj.contentprovider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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

    val resultContract =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { pickedUri: Uri? ->
                imageUri = pickedUri
            })
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUri?.let { uri ->
            Image(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(fraction = .5f),
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(uri), contentDescription = "picked-img"
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                resultContract.launch("image/*")
            }) {
            Text(text = "Pick Image")
        }
    }
}