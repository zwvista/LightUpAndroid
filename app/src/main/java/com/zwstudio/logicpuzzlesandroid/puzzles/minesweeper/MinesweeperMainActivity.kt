package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MinesweeperMainActivity : GameMainActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        MinesweeperOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        MinesweeperGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MinesweeperOptionsActivity : GameOptionsActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class MinesweeperHelpActivity : GameHelpActivity<MinesweeperGame, MinesweeperDocument, MinesweeperGameMove, MinesweeperGameState>() {
    @Bean
    protected lateinit var document: MinesweeperDocument
    override val doc get() = document
}