package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FillominoMainActivity : GameMainActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState>() {
    @Bean
    protected lateinit var document: FillominoDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        FillominoOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        FillominoGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class FillominoOptionsActivity : GameOptionsActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState>() {
    @Bean
    protected lateinit var document: FillominoDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class FillominoHelpActivity : GameHelpActivity<FillominoGame, FillominoDocument, FillominoGameMove, FillominoGameState>() {
    @Bean
    protected lateinit var document: FillominoDocument
    override val doc get() = document
}