package com.bugra.tabu

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bugra.tabu.data.TabuWords
import com.bugra.tabu.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("tabu_prefs", Context.MODE_PRIVATE)

    // Screens
    var screen by mutableStateOf(GameScreen.LOBBY)
        private set

    // Settings
    var roundDuration by mutableStateOf(prefs.getInt("round_duration", 60))
        private set
    var targetScore by mutableStateOf(prefs.getInt("target_score", 50)) // 0 = limitsiz
        private set
    var passLimit by mutableStateOf(prefs.getInt("pass_limit", 3))     // -1 = limitsiz
        private set
    var selectedCategory by mutableStateOf(prefs.getString("selected_category", "Tümü") ?: "Tümü")
        private set
    var soundEnabled by mutableStateOf(prefs.getBoolean("sound_enabled", false))
        private set

    // Teams
    val teams = mutableStateListOf<Team>()

    // Game state
    var currentTeamIndex by mutableStateOf(0)
        private set
    var currentRound by mutableStateOf(1)
        private set
    var remainingSeconds by mutableStateOf(60)
        private set
    var isPaused by mutableStateOf(false)
        private set
    var remainingPasses by mutableStateOf(3)
        private set

    // Cards & Deck
    private var deck: MutableList<TabuCard> = mutableListOf()
    private val usedCardIds = mutableSetOf<Int>()
    var currentCard by mutableStateOf<TabuCard?>(null)
        private set

    // Actions in current round
    val roundActions = mutableStateListOf<WordAction>()

    // Calculated round score
    val roundScore: Int
        get() = roundActions.fold(0) { acc, action ->
            acc + when (action.actionType) {
                ActionType.CORRECT -> 1
                ActionType.TABOO -> -1
                ActionType.PASS -> 0
            }
        }

    val currentTeam: Team
        get() = if (teams.isNotEmpty()) teams[currentTeamIndex] else Team("1", "A Takımı")

    val winnerTeam: Team?
        get() {
            if (targetScore <= 0 || teams.isEmpty()) return null
            val leaders = teams.filter { it.score >= targetScore }
            return if (leaders.isNotEmpty()) {
                leaders.maxByOrNull { it.score }
            } else null
        }

    private var timerJob: Job? = null
    private var toneGen: ToneGenerator? = null

    init {
        loadTeams()
        initToneGenerator()
    }

    private fun initToneGenerator() {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 15)
        } catch (_: Exception) {
            toneGen = null
        }
    }

    private fun playTone(toneType: Int) {
        if (!soundEnabled) return
        try {
            toneGen?.startTone(toneType, 70)
        } catch (_: Exception) {
        }
    }

    private fun loadTeams() {
        val savedTeamsStr = prefs.getString("teams_list", null)
        teams.clear()
        if (savedTeamsStr.isNullOrBlank()) {
            teams.add(Team("t1", "A Takımı"))
            teams.add(Team("t2", "B Takımı"))
        } else {
            val names = savedTeamsStr.split(";;;").filter { it.isNotBlank() }
            if (names.size >= 2) {
                names.forEachIndexed { i, n ->
                    teams.add(Team("t$i", n))
                }
            } else {
                teams.add(Team("t1", "A Takımı"))
                teams.add(Team("t2", "B Takımı"))
            }
        }
    }

    private fun saveTeams() {
        val str = teams.joinToString(";;;") { it.name }
        prefs.edit().putString("teams_list", str).apply()
    }

    // Settings actions
    fun setDuration(duration: Int) {
        roundDuration = duration
        prefs.edit().putInt("round_duration", duration).apply()
    }

    fun setTarget(target: Int) {
        targetScore = target
        prefs.edit().putInt("target_score", target).apply()
    }

    fun setPass(limit: Int) {
        passLimit = limit
        prefs.edit().putInt("pass_limit", limit).apply()
    }

    fun setCategory(category: String) {
        selectedCategory = category
        prefs.edit().putString("selected_category", category).apply()
    }

    fun toggleSound(enabled: Boolean) {
        soundEnabled = enabled
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
    }

    // Team management
    fun addTeam(name: String) {
        if (teams.size < 4 && name.isNotBlank()) {
            teams.add(Team("t${System.currentTimeMillis()}", name))
            saveTeams()
        }
    }

    fun renameTeam(index: Int, newName: String) {
        if (index in teams.indices && newName.isNotBlank()) {
            val team = teams[index]
            teams[index] = team.copy(name = newName)
            saveTeams()
        }
    }

    fun removeTeam(index: Int) {
        if (teams.size > 2 && index in teams.indices) {
            teams.removeAt(index)
            saveTeams()
        }
    }

    // Deck preparation
    private fun prepareDeck() {
        val filtered = if (selectedCategory == "Tümü") {
            TabuWords.CARDS
        } else {
            TabuWords.CARDS.filter { it.category == selectedCategory }
        }
        val pool = filtered.ifEmpty { TabuWords.CARDS }
        val unused = pool.filter { it.id !in usedCardIds }

        deck = if (unused.size < 10) {
            usedCardIds.clear()
            pool.shuffled().toMutableList()
        } else {
            unused.shuffled().toMutableList()
        }
    }

    private fun drawNextCard(): TabuCard? {
        if (deck.isEmpty()) {
            prepareDeck()
        }
        if (deck.isEmpty()) return null
        val card = deck.removeAt(0)
        usedCardIds.add(card.id)
        return card
    }

    // Start Game from Lobby
    fun startNewGame() {
        // Reset scores
        teams.forEachIndexed { i, team ->
            teams[i] = team.copy(score = 0)
        }
        currentTeamIndex = 0
        currentRound = 1
        usedCardIds.clear()
        prepareDeck()
        screen = GameScreen.READY
    }

    // Start turn for current team
    fun startTurn() {
        remainingSeconds = roundDuration
        remainingPasses = passLimit
        roundActions.clear()
        isPaused = false
        currentCard = drawNextCard()
        screen = GameScreen.PLAYING
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (remainingSeconds > 0) {
                delay(1000L)
                if (!isPaused) {
                    remainingSeconds--
                    if (remainingSeconds == 0) {
                        playTone(ToneGenerator.TONE_PROP_BEEP2)
                        endTurn()
                    }
                }
            }
        }
    }

    fun togglePause() {
        isPaused = !isPaused
    }

    // In-game actions
    fun onCorrect() {
        val card = currentCard ?: return
        playTone(ToneGenerator.TONE_PROP_BEEP)
        roundActions.add(WordAction(card, ActionType.CORRECT))
        currentCard = drawNextCard()
    }

    fun onTaboo() {
        val card = currentCard ?: return
        playTone(ToneGenerator.TONE_PROP_NACK)
        roundActions.add(WordAction(card, ActionType.TABOO))
        currentCard = drawNextCard()
    }

    fun onPass() {
        val card = currentCard ?: return
        if (passLimit != -1 && remainingPasses <= 0) return
        if (passLimit != -1) {
            remainingPasses--
        }
        playTone(ToneGenerator.TONE_PROP_ACK)
        roundActions.add(WordAction(card, ActionType.PASS))
        currentCard = drawNextCard()
    }

    private fun endTurn() {
        timerJob?.cancel()
        timerJob = null
        screen = GameScreen.ROUND_SUMMARY
    }

    // Change action type in summary screen (to handle objections)
    fun cycleActionType(index: Int) {
        if (index in roundActions.indices) {
            val item = roundActions[index]
            val nextType = when (item.actionType) {
                ActionType.CORRECT -> ActionType.TABOO
                ActionType.TABOO -> ActionType.PASS
                ActionType.PASS -> ActionType.CORRECT
            }
            roundActions[index] = item.copy(actionType = nextType)
        }
    }

    // Finish summary and proceed
    fun nextRound() {
        // Commit score to current team
        val earnedScore = roundScore
        val oldTeam = teams[currentTeamIndex]
        val newScore = maxOf(0, oldTeam.score + earnedScore)
        teams[currentTeamIndex] = oldTeam.copy(score = newScore)

        // Check if any team won
        if (targetScore > 0) {
            val isEndOfRoundCycle = (currentTeamIndex == teams.size - 1)
            val anyReachedTarget = teams.any { it.score >= targetScore }
            if (isEndOfRoundCycle && anyReachedTarget) {
                screen = GameScreen.GAME_OVER
                return
            }
        }

        // Advance to next team
        val nextIndex = (currentTeamIndex + 1) % teams.size
        if (nextIndex == 0) {
            currentRound++
        }
        currentTeamIndex = nextIndex
        screen = GameScreen.READY
    }

    fun backToLobby() {
        timerJob?.cancel()
        timerJob = null
        screen = GameScreen.LOBBY
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        toneGen?.release()
    }
}
