package com.bugra.tabu.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bugra.tabu.GameViewModel
import com.bugra.tabu.R
import com.bugra.tabu.ui.components.*
import com.bugra.tabu.ui.theme.*

@Composable
fun LobbyScreen(vm: GameViewModel) {
    var showTeamsDialog by remember { mutableStateOf(false) }
    var showDurationDialog by remember { mutableStateOf(false) }
    var showTargetDialog by remember { mutableStateOf(false) }
    var showPassDialog by remember { mutableStateOf(false) }
    var showTabooPenaltyDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Minimal App Title
            Row(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Tabu",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Text(
                        text = "Kelime Anlatma Oyunu",
                        fontSize = 13.sp,
                        color = TextSub
                    )
                }
            }

            // Summary row: Takımlar
            val teamsSummary = vm.teams.joinToString(" & ") { it.name }
            MenuRow(
                label = "👥 Takımlar",
                value = if (teamsSummary.length > 20) "${vm.teams.size} Takım" else teamsSummary
            ) {
                showTeamsDialog = true
            }

            // Summary row: Tur Süresi
            MenuRow(
                label = "⏱️ Tur Süresi",
                value = "${vm.roundDuration} sn"
            ) {
                showDurationDialog = true
            }

            // Summary row: Hedef Puan
            val targetLabel = if (vm.targetScore == 0) "Limitsiz" else "${vm.targetScore} Puan"
            MenuRow(
                label = "🎯 Hedef Puan",
                value = targetLabel
            ) {
                showTargetDialog = true
            }

            // Summary row: Pas Hakkı
            val passLabel = if (vm.passLimit == -1) "Limitsiz" else "${vm.passLimit} Pas"
            MenuRow(
                label = "⏭️ Pas Hakkı",
                value = passLabel
            ) {
                showPassDialog = true
            }

            // Summary row: Tabu Cezası (Kullanıcı tercihi)
            val tabooPenaltyLabel = if (vm.tabooPenalty == 0) "Ceza Yok (0 Puan)" else "-${vm.tabooPenalty} Puan"
            MenuRow(
                label = "⚠️ Tabu Cezası",
                value = tabooPenaltyLabel
            ) {
                showTabooPenaltyDialog = true
            }

            // Summary row: Kategori
            MenuRow(
                label = "🗂️ Kategori",
                value = vm.selectedCategory
            ) {
                showCategoryDialog = true
            }
        }

        // Start Game Button
        Box(
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        ) {
            PrimaryButton(
                text = "Oyunu Başlat",
                onClick = { vm.startNewGame() }
            )
        }
    }

    // Dialogs
    if (showTeamsDialog) {
        TeamsDialog(
            teams = vm.teams,
            onAddTeam = { vm.addTeam(it) },
            onRenameTeam = { idx, name -> vm.renameTeam(idx, name) },
            onRemoveTeam = { idx -> vm.removeTeam(idx) },
            onDismiss = { showTeamsDialog = false }
        )
    }

    if (showDurationDialog) {
        SingleChoiceDialog(
            title = "Tur Süresi Seçimi",
            options = listOf(
                "15 saniye (Yıldırım)" to 15,
                "20 saniye" to 20,
                "30 saniye (Hızlı)" to 30,
                "45 saniye" to 45,
                "60 saniye (Standart)" to 60,
                "90 saniye" to 90,
                "120 saniye" to 120
            ),
            selectedValue = vm.roundDuration,
            onSelect = { vm.setDuration(it) },
            onDismiss = { showDurationDialog = false }
        )
    }

    if (showTargetDialog) {
        SingleChoiceDialog(
            title = "Hedef Puan Seçimi",
            options = listOf(
                "25 Puan" to 25,
                "50 Puan" to 50,
                "75 Puan" to 75,
                "100 Puan" to 100,
                "Limitsiz (Serbest Oyun)" to 0
            ),
            selectedValue = vm.targetScore,
            onSelect = { vm.setTarget(it) },
            onDismiss = { showTargetDialog = false }
        )
    }

    if (showPassDialog) {
        SingleChoiceDialog(
            title = "Tur Başına Pas Hakkı",
            options = listOf(
                "1 Pas" to 1,
                "2 Pas" to 2,
                "3 Pas" to 3,
                "5 Pas" to 5,
                "Limitsiz Pas" to -1
            ),
            selectedValue = vm.passLimit,
            onSelect = { vm.setPass(it) },
            onDismiss = { showPassDialog = false }
        )
    }

    if (showTabooPenaltyDialog) {
        SingleChoiceDialog(
            title = "Tabu Cezası Seçimi",
            options = listOf(
                "Ceza Yok (0 Puan)" to 0,
                "-1 Puan (Klasik)" to 1,
                "-2 Puan (Zor Mod)" to 2,
                "-3 Puan (Ağır Ceza)" to 3
            ),
            selectedValue = vm.tabooPenalty,
            onSelect = { vm.setPenalty(it) },
            onDismiss = { showTabooPenaltyDialog = false }
        )
    }

    if (showCategoryDialog) {
        CategorySelectionDialog(
            selectedCategory = vm.selectedCategory,
            onSelectCategory = { vm.setCategory(it) },
            onDismiss = { showCategoryDialog = false }
        )
    }
}
