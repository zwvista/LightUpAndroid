package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data.KropkiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class KropkiGameActivity : GameGameActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    @Bean
    protected lateinit var document: KropkiDocument
    override fun doc() = document

    protected lateinit var gameView2: KropkiGameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = KropkiGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = KropkiGame(level.layout, "1" == level.settings["Bordered"], this, doc())
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
        KropkiHelpActivity_.intent(this).start()
    }
}