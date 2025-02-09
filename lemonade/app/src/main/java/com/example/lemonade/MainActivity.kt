package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeTheme {
                LemonadeApp()
            }
        }
    }
}


@Composable
fun LemonadeStep(
    stepText: String,
    imageResource: Int,
    nextStepAction: () -> Unit,
    contentDescription: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stepText)
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(imageResource),
            contentDescription = contentDescription,
            modifier = Modifier
                .wrapContentSize()
                .clickable { nextStepAction() }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = contentDescription,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun LemonadeApp() {
    var currentStep by remember { mutableIntStateOf(1) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (currentStep) {
            1 -> LemonadeStep(
                stepText = stringResource(R.string.lemon_select),
                imageResource = R.drawable.lemon_tree,
                nextStepAction = { currentStep = 2 },
                contentDescription = stringResource(R.string.lemon_tree_content_description)
            )
            2 -> LemonadeStep(
                stepText = stringResource(R.string.lemon_squeeze),
                imageResource = R.drawable.lemon_squeeze,
                nextStepAction = { currentStep = 3 },
                contentDescription = stringResource(R.string.lemon_content_description)
            )
            3 -> LemonadeStep(
                stepText = stringResource(R.string.lemon_drink),
                imageResource = R.drawable.lemon_drink,
                nextStepAction = { currentStep = 4 },
                contentDescription = stringResource(R.string.lemon_drink_description)
            )
            4 -> LemonadeStep(
                stepText = stringResource(R.string.lemon_restart),
                imageResource = R.drawable.lemon_restart,
                nextStepAction = { currentStep = 1 },
                contentDescription = stringResource(R.string.lemon_restart_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LemonadeTheme {
        LemonadeApp()
    }
}