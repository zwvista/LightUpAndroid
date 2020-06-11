package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.data.NoughtsAndCrossesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NoughtsAndCrossesGameActivity : GameGameActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    @Bean
    protected lateinit var document: NoughtsAndCrossesDocument
    override fun doc() = document

    protected lateinit var gameView2: NoughtsAndCrossesGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = NoughtsAndCrossesGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = NoughtsAndCrossesGame(level.layout, level.settings["num"]!![0], this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex: Int = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        NoughtsAndCrossesHelpActivity_.intent(this).start()
    }
}