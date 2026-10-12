package com.bugra.tabu.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bugra.tabu.GameViewModel
import com.bugra.tabu.model.ActionType
import com.bugra.tabu.ui.components.PrimaryButton
import com.bugra.tabu.ui.theme.*

@Composable
fun RoundSummaryScreen(vm: GameViewModel) {
    BackHandler { vm.backToLobby() }

    val currentTeam = vm.currentTeam
    val correctCount = vm.roundActions.count { it.actionType == ActionType.CORRECT }
    val tabooCount = vm.roundActions.count { it.actionType == ActionType.TABOO }
    val passCount = vm.roundActions.count { it.actionType == ActionType.PASS }
    val roundScore = vm.roundScore

    val nextTeamIndex = (vm.currentTeamIndex + 1) % vm.teams.size
    val nextTeam = vm.teams[nextTeamIndex]

    // Default olarak kapalı gelir
    var isWordsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Tur Sonu Bilgisi
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val roundText = if (vm.totalRounds > 0) {
                    "${currentTeam.name} — ${vm.currentRound} / ${vm.totalRounds}. Tur Sonu"
                } else {
                    "${currentTeam.name} — Tur Sonu"
                }
                Text(
                    text = roundText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSub
                )
                Spacer(Modifier.height(6.dp))
                val sign = if (roundScore > 0) "+" else ""
                Text(
                    text = "$sign$roundScore Puan",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (roundScore > 0) ActionGreen else if (roundScore < 0) ActionRed else TextMain
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "$correctCount Doğru  •  $tabooCount Tabu  •  $passCount Pas",
                    fontSize = 14.sp,
                    color = TextSub
                )
            }

            // Genel Puan Tablosu Card (Ön planda ve net)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface1)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Genel Puan Tablosu",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSub
                )

                vm.teams.forEach { team ->
                    val isCurrent = (team.id == currentTeam.id)
                    // Eksi puana düşmeye izin verilir
                    val projectedScore = if (isCurrent) team.score + roundScore else team.score

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isCurrent) DarkSurface2 else Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = team.name + if (isCurrent) " (Bu Tur)" else "",
                            fontSize = 15.sp,
                            fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isCurrent) Accent else TextMain
                        )
                        Text(
                            text = "$projectedScore Puan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (projectedScore < 0) ActionRed else TextMain
                        )
                    }
                }
            }

            // Oynanan Kelimeleri Düzenle (Default olarak kapalı menü)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface1)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isWordsExpanded = !isWordsExpanded }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📝 Oynanan Kelimeleri Düzenle (${vm.roundActions.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMain,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (isWordsExpanded) "Kapat ▲" else "İncele ▼",
                        fontSize = 13.sp,
                        color = Accent,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (isWordsExpanded) {
                    HorizontalDivider(color = DarkSurface2, thickness = 1.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "İtiraz varsa kelimeye dokunarak sonucu değiştirebilirsiniz:",
                            fontSize = 12.sp,
                            color = TextSub,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        if (vm.roundActions.isEmpty()) {
                            Text(
                                text = "Bu turda hiç kelime oynanmadı.",
                                color = TextSub,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 280.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                vm.roundActions.forEachIndexed { index, item ->
                                    val badgeColor: Color
                                    val badgeTextColor: Color
                                    val badgeText: String

                                    when (item.actionType) {
                                        ActionType.CORRECT -> {
                                            badgeColor = ActionGreen.copy(alpha = 0.2f)
                                            badgeTextColor = ActionGreen
                                            badgeText = "✓ Doğru (+1)"
                                        }
                                        ActionType.TABOO -> {
                                            badgeColor = ActionRed.copy(alpha = 0.2f)
                                            badgeTextColor = ActionRed
                                            badgeText = if (vm.tabooPenalty > 0) "✕ Tabu (-${vm.tabooPenalty})" else "✕ Tabu (0)"
                                        }
                                        ActionType.PASS -> {
                                            badgeColor = DarkSurface2
                                            badgeTextColor = TextSub
                                            badgeText = "— Pas"
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(DarkSurface2)
                                            .clickable { vm.cycleActionType(index) }
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.card.word,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextMain,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(badgeColor)
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = badgeText,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = badgeTextColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Button
        Box(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            val isNextRoundEnd = (nextTeamIndex == 0)
            val isGameConcluding = (vm.totalRounds > 0 && vm.currentRound >= vm.totalRounds && isNextRoundEnd)
            val buttonText = if (isGameConcluding) {
                "Sonuçları Gör (Oyun Sonu) ›"
            } else {
                "Sıradaki Takım: ${nextTeam.name} ›"
            }

            PrimaryButton(
                text = buttonText,
                onClick = { vm.nextRound() }
            )
        }
    }
}
