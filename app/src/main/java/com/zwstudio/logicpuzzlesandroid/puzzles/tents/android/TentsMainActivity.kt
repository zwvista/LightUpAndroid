package com.zwstudio.logicpuzzlesandroid.puzzles.tents.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.android.TentsOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.data.TentsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TentsMainActivity : GameMainActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    @Bean
    protected lateinit var document: TentsDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        TentsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        TentsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TentsOptionsActivity : GameOptionsActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    @Bean
    protected lateinit var document: TentsDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class TentsHelpActivity : GameHelpActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    @Bean
    protected lateinit var document: TentsDocument
    override fun doc() = document
}