package com.bugra.tabu.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bugra.tabu.GameViewModel
import com.bugra.tabu.ui.components.PrimaryButton
import com.bugra.tabu.ui.components.SecondaryButton
import com.bugra.tabu.ui.theme.*

@Composable
fun GameScreen(vm: GameViewModel) {
    BackHandler { vm.togglePause() }

    val card = vm.currentCard
    val team = vm.currentTeam
    val remaining = vm.remainingSeconds
    val isTimerLow = remaining <= 10

    val passEnabled = vm.passLimit == -1 || vm.remainingPasses > 0
    val passText = when {
        vm.passLimit == -1 -> "Pas"
        vm.remainingPasses > 0 -> "Pas (${vm.remainingPasses})"
        else -> "Pas (Bitti)"
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Team Name, Score, Timer, Pause
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface1)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Team & Score
                Column {
                    Text(
                        text = team.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMain
                    )
                    val sign = if (vm.roundScore > 0) "+" else ""
                    Text(
                        text = "Tur: $sign${vm.roundScore} Puan",
                        fontSize = 13.sp,
                        color = if (vm.roundScore >= 0) ActionGreen else ActionRed,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Countdown Timer
                val minutes = remaining / 60
                val seconds = remaining % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurface2)
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timeString,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isTimerLow) ActionAmber else TextMain
                    )
                }

                // Pause Button
                TextButton(
                    onClick = { vm.togglePause() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = DarkSurface2,
                        contentColor = TextSub
                    )
                ) {
                    Text(
                        text = if (vm.isPaused) "Devam" else "Duraklat",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Center Taboo Card
            if (card != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(DarkSurface1)
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Target Word (Large, High Contrast)
                    Text(
                        text = card.word,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.sp
                    )

                    Spacer(Modifier.height(18.dp))

                    // Minimal Divider
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        thickness = 1.5.dp,
                        color = DarkSurface2
                    )

                    Spacer(Modifier.height(20.dp))

                    // 5 Forbidden Words
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        card.forbiddenWords.forEach { word ->
                            Text(
                                text = word,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSub,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(DarkSurface1),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Kart yükleniyor...", color = TextSub, fontSize = 16.sp)
                }
            }

            // Bottom Action Buttons (Doğru, Pas, Tabu)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tabu (-1)
                Button(
                    onClick = { vm.onTaboo() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ActionRed,
                        contentColor = OnActionRed
                    )
                ) {
                    Text(
                        text = "Tabu (-1)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Pas (0)
                Button(
                    onClick = { vm.onPass() },
                    enabled = passEnabled,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ActionAmber,
                        contentColor = OnActionAmber,
                        disabledContainerColor = DisabledSurface,
                        disabledContentColor = DisabledText
                    )
                ) {
                    Text(
                        text = passText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Doğru (+1)
                Button(
                    onClick = { vm.onCorrect() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ActionGreen,
                        contentColor = OnActionGreen
                    )
                ) {
                    Text(
                        text = "Doğru (+1)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Pause Modal Overlay
        if (vm.isPaused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkSurface1)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Oyun Duraklatıldı",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Text(
                        text = "Kalan Süre: ${vm.remainingSeconds} sn\nTur Skoru: ${vm.roundScore}",
                        fontSize = 14.sp,
                        color = TextSub,
                        textAlign = TextAlign.Center
                    )
                    PrimaryButton(text = "Devam Et") {
                        vm.togglePause()
                    }
                    SecondaryButton(text = "Lobiye Dön") {
                        vm.backToLobby()
                    }
                }
            }
        }
    }
}
