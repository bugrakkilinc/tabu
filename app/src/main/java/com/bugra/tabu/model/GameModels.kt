package com.bugra.tabu.model

data class TabuCard(
    val id: Int,
    val word: String,
    val forbiddenWords: List<String>,
    val category: String = "Genel"
)

enum class ActionType {
    CORRECT,
    TABOO,
    PASS
}

data class WordAction(
    val card: TabuCard,
    var actionType: ActionType
)

data class Team(
    val id: String,
    val name: String,
    var score: Int = 0
)

enum class GameScreen {
    LOBBY,
    READY,
    PLAYING,
    ROUND_SUMMARY,
    GAME_OVER
}

data class GameSettings(
    val roundDurationSeconds: Int = 60,
    val targetScore: Int = 50, // 0 means unlimited
    val passLimit: Int = 3,    // -1 means unlimited
    val tabooPenalty: Int = 1  // 0, 1, 2, 3
)
