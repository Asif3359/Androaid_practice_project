package com.example.businesscardapp

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscardapp.ui.theme.BusinessCardAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BusinessCardAppTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardContent()
            Spacer(modifier = Modifier.height(100.dp)) // Added spacing
            InformationCard()
        }
    }
}

@Composable
fun CardContent() {
    Column(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.android_logo),
            contentDescription = "Business logo",
            modifier = Modifier
                .height(80.dp)
                .background(Color.Black)
        )
        Text(
            text = "Asif Ahammed",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = "A dedicated Android developer",
            fontSize = 12.sp,
            color = Color.DarkGray,
        )
    }
}

@Composable
fun InformationCard() {
    Column(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InformationContent(
            image = R.drawable.phone_call,
            text = "+880 01789846204"
        )
        InformationContent(
            image = R.drawable.email,
            text = "asifahammednishst@gmail.com"
        )
    }
}

@Composable
fun InformationContent(@DrawableRes image: Int, text: String) {
    Row(
        modifier = Modifier.padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = text,
            modifier = Modifier
                .size(30.dp)
                .padding(end = 10.dp)
        )
        Text(text, fontSize = 14.sp)
    }
}