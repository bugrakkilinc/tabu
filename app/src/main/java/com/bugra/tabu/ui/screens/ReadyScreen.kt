package com.bugra.tabu.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bugra.tabu.GameViewModel
import com.bugra.tabu.ui.components.PrimaryButton
import com.bugra.tabu.ui.components.SecondaryButton
import com.bugra.tabu.ui.theme.*

@Composable
fun ReadyScreen(vm: GameViewModel) {
    BackHandler { vm.backToLobby() }

    val team = vm.currentTeam

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            val roundText = if (vm.totalRounds > 0) "${vm.currentRound} / ${vm.totalRounds}. Tur" else "${vm.currentRound}. Tur"
            Text(
                text = roundText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextSub
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Sıradaki Takım",
                fontSize = 14.sp,
                color = Accent
            )
        }

        // Center card with Team name and score
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(DarkSurface1)
                .padding(vertical = 40.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = team.name,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Mevcut Puan: ${team.score}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSub
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Telefonu anlatıcıya verin.\nBaşlattığınızda süre geri saymaya başlayacaktır.",
                    fontSize = 14.sp,
                    color = TextSub,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        // Bottom Actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrimaryButton(
                text = "Turu Başlat",
                onClick = { vm.startTurn() }
            )
            SecondaryButton(
                text = "Lobiye Dön",
                onClick = { vm.backToLobby() }
            )
        }
    }
}
