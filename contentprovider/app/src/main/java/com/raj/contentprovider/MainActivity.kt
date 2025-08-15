package com.raj.contentprovider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.raj.contentprovider.ui.theme.ContentProviderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentProviderTheme {
                CustomTopAppBar(titleText = "Content Provider") {
                    MainScreen(clickHandler())
                }
            }
        }
    }


    private fun clickHandler(): ClickActions =
        ClickActions(
            singleImageOnly = {
                ImagePickerActivity.show(this)
            },
            multipleImage = {
                MultiImagePickerActivity.show(fromActivity = this)
            },
            videoPicker = {
                VideoPickerActivity.show(this)
            },
             documentPicker = {
                 DocumentPickerActivity.show(this)
             }
        )
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContentProviderTheme {
        Greeting("Android")
    }
}