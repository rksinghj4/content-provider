package com.raj.contentprovider


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
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
import com.raj.contentprovider.ui.theme.ContentProviderTheme


//Important Considerations:
//FileProvider:
//If you are providing a PDF from your own app's internal storage,
// you must use a FileProvider to generate a content:// URI for security and compatibility
// with Android 7.0 (API level 24) and higher.

//Permissions:
//For accessing external storage (e.g., Downloads folder),
// you might need READ_EXTERNAL_STORAGE permission, though ActivityResultContracts.GetContent
// often handles this implicitly.

class DocumentPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContentProviderTheme {
                DocumentReaderWithButton()
            }
        }
    }

    companion object {
        fun show(fromActivity: Activity) {
            Intent(fromActivity, DocumentPickerActivity::class.java).also {
                fromActivity.startActivity(it)
            }
        }
    }
}

//Open in other apps who handle this implicit intent.
//Opening with an External PDF Viewer (Implicit Intent).
private fun openPdf(pdfUri: Uri, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    // data is Uri, type is "application/pdf"
    intent.setDataAndType(pdfUri, "application/pdf")
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant temporary read permission
    context.startActivity(intent)
}

@Composable
fun DocumentReaderWithButton() {
    var documentUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    val managedActivityResultLauncher: ManagedActivityResultLauncher<Array<String>, Uri?> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),//Jo bhi open karna h uska Contract
            onResult = { uri ->
                documentUri = uri//picked Uri is given here
            })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        documentUri?.let { uri ->
            openPdf(uri, LocalContext.current)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                //Launch the Document Picker: which will show you just videos(filter for videos)
                managedActivityResultLauncher.launch(arrayOf("application/pdf"))//with ActivityResultContracts.OpenDocument()

                //Launch the image Picker: which will show you just image
                //resultContract.launch("image/*")//with ActivityResultContracts.GetContent()
                //resultContract.launch("video/*")//with ActivityResultContracts.GetContent()
            }) {
            Text(text = "Pick Doc")
        }
    }
}