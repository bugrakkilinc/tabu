package com.bugra.tabu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.bugra.tabu.model.GameScreen
import com.bugra.tabu.ui.screens.*
import com.bugra.tabu.ui.theme.DarkBg
import com.bugra.tabu.ui.theme.TabuTheme
import com.bugra.tabu.ui.theme.TextMain

class MainActivity : ComponentActivity() {

    private val vm: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabuTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBg)
                        .systemBarsPadding()
                ) {
                    CompositionLocalProvider(LocalContentColor provides TextMain) {
                        TabuApp(vm)
                    }
                }
            }
        }
    }
}

@Composable
fun TabuApp(vm: GameViewModel) {
    when (vm.screen) {
        GameScreen.LOBBY -> LobbyScreen(vm)
        GameScreen.READY -> ReadyScreen(vm)
        GameScreen.PLAYING -> GameScreen(vm)
        GameScreen.ROUND_SUMMARY -> RoundSummaryScreen(vm)
        GameScreen.GAME_OVER -> GameOverScreen(vm)
    }
}
