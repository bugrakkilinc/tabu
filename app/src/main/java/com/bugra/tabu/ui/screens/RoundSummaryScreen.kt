package com.bugra.tabu.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.bugra.tabu.model.ActionType
import com.bugra.tabu.ui.components.PrimaryButton
import com.bugra.tabu.ui.components.SecondaryButton
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${currentTeam.name} — Tur Sonu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain
                )
                Spacer(Modifier.height(6.dp))
                val sign = if (roundScore > 0) "+" else ""
                Text(
                    text = "$sign$roundScore Puan",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (roundScore >= 0) ActionGreen else ActionRed
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$correctCount Doğru  •  $tabooCount Tabu  •  $passCount Pas",
                    fontSize = 14.sp,
                    color = TextSub
                )
            }

            // Word Breakdown List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(DarkSurface1)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Oynanan Kelimeler (${vm.roundActions.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSub
                    )
                    Text(
                        text = "Dokunup Değiştir",
                        fontSize = 12.sp,
                        color = Accent
                    )
                }

                if (vm.roundActions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Bu turda hiç kelime oynanmadı.",
                            color = TextSub,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(vm.roundActions) { index, item ->
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

            // General Scoreboard Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface1)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Genel Puan Tablosu",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSub
                )

                vm.teams.forEach { team ->
                    val isCurrent = (team.id == currentTeam.id)
                    val projectedScore = if (isCurrent) maxOf(0, team.score + roundScore) else team.score

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
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
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }
                }
            }
        }

        // Bottom Button
        Box(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            PrimaryButton(
                text = "Sıradaki Takım: ${nextTeam.name} ›",
                onClick = { vm.nextRound() }
            )
        }
    }
}
