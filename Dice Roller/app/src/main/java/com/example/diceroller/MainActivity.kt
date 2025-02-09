package com.example.diceroller

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
    }
}

@Composable
fun DiceRollerApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        DiceWithButtonAndImage(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .padding(paddingValues)
        )
    }
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    val diceRoll = remember { androidx.compose.runtime.mutableIntStateOf(Random.nextInt(1, 7)) }
    val diceImage: Painter = when (diceRoll.intValue) {
        1 -> painterResource(id = R.drawable.dice_1)
        2 -> painterResource(id = R.drawable.dice_2)
        3 -> painterResource(id = R.drawable.dice_3)
        4 -> painterResource(id = R.drawable.dice_4)
        5 -> painterResource(id = R.drawable.dice_5)
        6 -> painterResource(id = R.drawable.dice_6)
        else -> painterResource(id = R.drawable.dice_1)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Roll Dice game by Asif",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
            .align(Alignment.CenterHorizontally)

        )
        Spacer(modifier = Modifier.size(300.dp))
        Image(painter = diceImage, contentDescription = "Dice Roll")

        Button(onClick = {
            diceRoll.intValue = Random.nextInt(1, 7)
        }) {
            Text(text = "Roll Dice")
        }
    }
}



@Preview
@Composable
fun PreviewDiceRollerApp() {
    DiceRollerApp()
}

