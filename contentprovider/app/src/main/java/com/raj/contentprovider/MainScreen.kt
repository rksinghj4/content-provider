package com.raj.contentprovider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ClickActions(
    val singleImageOnly: () -> Unit = {},
    val multipleImage: () -> Unit = {},
    val videoPicker: () -> Unit = {},
    val documentPicker: () -> Unit = {}
)

@Composable
fun MainScreen(clickActions: ClickActions) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardItem(text = "Single Image Picker", clickActions.singleImageOnly)

        CardItem(text = "Multiple Image Picker", clickActions.multipleImage)

        CardItem(text = "Video Picker", clickActions.videoPicker)

        CardItem(text = "Document Picker", clickActions.documentPicker)


    }
}

@Composable
fun CardItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .shadow(10.dp, shape = RoundedCornerShape(10.dp))
            .width(200.dp)
            .height(60.dp),
        onClick = {
            onClick.invoke()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                color = Color.White
            )
        }
    }
}