package com.raj.contentprovider

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.raj.contentprovider.ui.theme.ContentProviderTheme

//https://www.youtube.com/watch?v=0Jwjy7o9u24

class VideoPickerActivity : ComponentActivity() {
    //
// rememberLauncherForActivityResult: Handles launching the gallery and receiving the selected video's URI.
// ActivityResultContracts.GetContent(): Specifies that you want to pick content, and 'video/*' filters for video files.
// VideoPlayerComposable: This composable initializes an ExoPlayer instance,
// sets the selected video URI as its MediaItem, and prepares it for playback.
// AndroidView: This composable allows you to embed
// an Android View (like StyledPlayerView from ExoPlayer) within your Compose UI.
//
// DisposableEffect: Ensures that the exoPlayer is released when the composable leaves the composition, preventing memory leaks.
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentProviderTheme {
                VideoWithButton()
            }
        }
    }

    companion object {
        fun show(fromActivity: Activity) {
            Intent(fromActivity, VideoPickerActivity::class.java).also {
                fromActivity.startActivity(it)
            }
        }
    }
}


@Composable
fun VideoWithButton() {
    var thumbnail: Bitmap? = null

    var videoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    //Use rememberLauncherForActivityResult with ActivityResultContracts.GetContent()
    // to launch the gallery and retrieve the video URI:
    val managedActivityResultLauncher: ManagedActivityResultLauncher<String, Uri?> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                videoUri = uri
            }
        )


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        videoUri?.let { uri ->
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(LocalContext.current, videoUri)
            thumbnail = mediaMetadataRetriever.getFrameAtTime(20)//Time in micros
            Box {

                AsyncImage(
                    model = thumbnail, contentDescription = "thumbnail"
                )

                VideoPlayerComposable(uri)
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                //Launch the Video Picker: which will show you just videos(filter for videos)
                managedActivityResultLauncher.launch("video/*")//with ActivityResultContracts.GetContent()

                //Launch the image Picker: which will show you just image
                //resultContract.launch("image/*")//with ActivityResultContracts.GetContent()
            }) {
            Text(text = "Pick Video")
        }
    }
}