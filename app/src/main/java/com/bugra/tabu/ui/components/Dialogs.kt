package com.bugra.tabu.ui.components

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
import com.bugra.tabu.data.TabuWords
import com.bugra.tabu.model.Team
import com.bugra.tabu.ui.theme.*

@Composable
fun TeamsDialog(
    teams: List<Team>,
    onAddTeam: (String) -> Unit,
    onRenameTeam: (Int, String) -> Unit,
    onRemoveTeam: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var newTeamName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface1,
        titleContentColor = TextMain,
        textContentColor = TextMain,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Takımlar (${teams.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Team items
                teams.forEachIndexed { index, team ->
                    var isEditing by remember { mutableStateOf(false) }
                    var editedName by remember(team.name) { mutableStateOf(team.name) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurface2)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEditing) {
                            OutlinedTextField(
                                value = editedName,
                                onValueChange = { editedName = it },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextMain,
                                    unfocusedTextColor = TextMain,
                                    focusedBorderColor = Accent,
                                    unfocusedBorderColor = DarkSurface1,
                                    cursorColor = Accent
                                )
                            )
                            TextButton(onClick = {
                                if (editedName.isNotBlank()) {
                                    onRenameTeam(index, editedName.trim())
                                }
                                isEditing = false
                            }) {
                                Text("Kaydet", color = Accent, fontWeight = FontWeight.SemiBold)
                            }
                        } else {
                            Text(
                                text = team.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMain,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { isEditing = true }
                            )
                            TextButton(onClick = { isEditing = true }) {
                                Text("Düzenle", color = TextSub, fontSize = 13.sp)
                            }
                            if (teams.size > 2) {
                                TextButton(onClick = { onRemoveTeam(index) }) {
                                    Text("Sil", color = ActionRed, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                // Add team input (up to 4 teams)
                if (teams.size < 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newTeamName,
                            onValueChange = { newTeamName = it },
                            singleLine = true,
                            placeholder = { Text("Yeni Takım Adı", color = TextSub) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain,
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = DarkSurface2,
                                cursorColor = Accent
                            )
                        )
                        Button(
                            onClick = {
                                if (newTeamName.isNotBlank()) {
                                    onAddTeam(newTeamName.trim())
                                    newTeamName = ""
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurface2,
                                contentColor = TextMain
                            )
                        ) {
                            Text("+ Ekle", fontSize = 14.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tamam", color = Accent, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

@Composable
fun SingleChoiceDialog(
    title: String,
    options: List<Pair<String, Int>>,
    selectedValue: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface1,
        titleContentColor = TextMain,
        textContentColor = TextMain,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { (label, value) ->
                    val isSelected = (value == selectedValue)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Accent.copy(alpha = 0.15f) else DarkSurface2)
                            .clickable {
                                onSelect(value)
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) Accent else TextMain,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Text(
                                text = "✓",
                                color = Accent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat", color = TextSub)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelectionDialog(
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface1,
        titleContentColor = TextMain,
        textContentColor = TextMain,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Kategori Seçimi",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabuWords.ALL_CATEGORIES.forEach { category ->
                    val isSelected = (category == selectedCategory)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            onSelectCategory(category)
                            onDismiss()
                        },
                        label = {
                            Text(
                                text = category,
                                fontSize = 14.sp
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkSurface2,
                            labelColor = TextMain,
                            selectedContainerColor = Accent,
                            selectedLabelColor = OnAccent
                        ),
                        border = null
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tamam", color = Accent, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}
