package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.data.MineShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MineShipsGameActivity : GameGameActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    @Bean
    protected lateinit var document: MineShipsDocument
    override fun doc() = document

    protected lateinit var gameView2: MineShipsGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = MineShipsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = MineShipsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: MineShipsGameMove = doc().loadMove(rec)
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
        MineShipsHelpActivity_.intent(this).start()
    }
}