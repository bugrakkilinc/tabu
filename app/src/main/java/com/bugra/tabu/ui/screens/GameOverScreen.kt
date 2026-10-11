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
fun GameOverScreen(vm: GameViewModel) {
    BackHandler { vm.backToLobby() }

    val winner = vm.winnerTeam ?: vm.teams.maxByOrNull { it.score } ?: vm.currentTeam
    val sortedTeams = vm.teams.sortedByDescending { it.score }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = "Oyun Bitti!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextSub
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "🏆 Şampiyon",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = ActionAmber
            )
        }

        // Winner Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(DarkSurface1)
                .padding(vertical = 32.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = winner.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${winner.score} Puan",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Accent
            )

            Spacer(Modifier.height(10.dp))

            // Ranking table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurface2)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Sonuç Sıralaması",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSub
                )

                sortedTeams.forEachIndexed { index, team ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. ${team.name}",
                            fontSize = 15.sp,
                            fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (index == 0) ActionAmber else TextMain
                        )
                        Text(
                            text = "${team.score} Puan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }
                }
            }
        }

        // Bottom Actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrimaryButton(
                text = "Yeni Oyun Başlat",
                onClick = { vm.startNewGame() }
            )
            SecondaryButton(
                text = "Lobiye Dön",
                onClick = { vm.backToLobby() }
            )
        }
    }
}
