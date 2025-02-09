package com.example.composequadrant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Justify
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composequadrant.ui.theme.ComposeQuadrantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeQuadrantTheme {
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
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Quadrant(
                    title = "Text Composable",
                    description = "Displays text and follows the recommended Material Design guidelines.",
                    backgroundColor = Color(0xFFEADDFF),
                    modifier = Modifier.weight(1f)
                )
                Quadrant(
                    title = "Image Composable",
                    description = "Creates a composable that lays out and draws a given Painter class object.",
                    backgroundColor = Color(0xFFD0BCFF),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.weight(1f)) {
                Quadrant(
                    title = "Row Composable",
                    description = "A layout composable that places its children in a horizontal sequence.",
                    backgroundColor = Color(0xFFB69DF8),
                    modifier = Modifier.weight(1f)
                )
                Quadrant(
                    title = "Column Composable",
                    description = "A layout composable that places its children in a vertical sequence.",
                    backgroundColor = Color(0xFF9375E0),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun Quadrant(title: String, description: String, backgroundColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium , modifier = Modifier.padding(bottom = 16.dp) )
        Text(text = description, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainContent() {
    ComposeQuadrantTheme {
        MainContent()
    }
}
