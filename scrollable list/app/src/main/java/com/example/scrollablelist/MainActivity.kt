package com.example.scrollablelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollablelist.data.Datasource
import com.example.scrollablelist.model.Affirmation
import com.example.scrollablelist.ui.theme.ScrollableListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScrollableListTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(Modifier.fillMaxSize()) { // ✅ Wrap with Column to avoid LazyColumn issues
            Text(
                text = "List of Affirmations",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 16.sp
            )

            LazyColumn( // ✅ Ensures scrolling
                modifier = Modifier
                    .weight(1f) // Makes it take available space
            ) {
                items(Datasource().loadAffirmations()) { affirmation ->
                    AffirmationCard(affirmation = affirmation, modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Grid of Affirmations",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 16.sp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f) // ✅ Allows scrolling
                    .padding(8.dp)
            ) {
                items(Datasource().loadAffirmations().size) { index ->
                    AffirmationCard(
                        affirmation = Datasource().loadAffirmations()[index],
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun AffirmationCard(affirmation: Affirmation, modifier: Modifier) {
    Card (
        modifier = modifier
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(affirmation.imageResourceId),
                contentDescription = stringResource(affirmation.stringResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = LocalContext.current.getString(affirmation.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScrollableListTheme {
        MainContent()
    }
}