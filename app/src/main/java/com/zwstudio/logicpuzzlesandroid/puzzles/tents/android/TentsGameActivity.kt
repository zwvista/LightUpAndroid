package com.zwstudio.logicpuzzlesandroid.puzzles.tents.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.data.TentsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TentsGameActivity : GameGameActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    @Bean
    protected lateinit var document: TentsDocument
    override fun doc() = document

    protected lateinit var gameView2: TentsGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = TentsGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = TentsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: TentsGameMove = doc().loadMove(rec)
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
        TentsHelpActivity_.intent(this).start()
    }
}