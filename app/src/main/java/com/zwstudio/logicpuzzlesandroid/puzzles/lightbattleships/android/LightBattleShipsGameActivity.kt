package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.data.LightBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LightBattleShipsGameActivity : GameGameActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState>() {
    @Bean
    protected lateinit var document: LightBattleShipsDocument
    override fun doc() = document

    @AfterViews
    override fun init() {
        gameView = LightBattleShipsGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = LightBattleShipsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        LightBattleShipsHelpActivity_.intent(this).start()
    }
}