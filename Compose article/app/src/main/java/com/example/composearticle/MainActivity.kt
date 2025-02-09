package com.example.composearticle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Justify
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composearticle.ui.theme.ComposeArticleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeArticleTheme {
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
        Column (
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            BackGroundImage()
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle()
            Spacer(modifier = Modifier.height(16.dp))

            TextContent(text = stringResource(id = R.string.compose_short_desc))
            Spacer(modifier = Modifier.height(16.dp))
            TextContent(text = stringResource(id = R.string.compose_long_desc))

        }
    }
}

@Composable
fun TextContent(text : String) {
    Text(
        text = text,
        textAlign = Justify,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SectionTitle() {
    Text(
        "Hello welcome to compose",
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun BackGroundImage() {
    Image(
        painter = painterResource(id = R.drawable.bg_compose_background),
        contentDescription = "Background Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.LightGray)
    )
}
