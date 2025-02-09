package com.example.taskmanager

import android.os.Bundle
import android.provider.MediaStore.Images
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.theme.TaskManagerTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                MainContent()
            }
        }
    }
}
@Composable
fun MainContent() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    )
    { contentPadding ->
        Column  (
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(15.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TaskManagerContent()
        }

    }
}

@Composable
fun TaskManagerContent() {
    Column (
        modifier = Modifier
            .padding(15.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_task_completed),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Text(
            text = "All tasks completed",
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = "Nice work!",
            modifier = Modifier
        )
    }
}
