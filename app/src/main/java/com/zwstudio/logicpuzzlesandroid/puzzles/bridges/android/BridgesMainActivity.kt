package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class BridgesMainActivity : GameMainActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    @Bean
    protected lateinit var document: BridgesDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        BridgesOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BridgesGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class BridgesOptionsActivity : GameOptionsActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    @Bean
    protected lateinit var document: BridgesDocument
    override fun doc() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class BridgesHelpActivity : GameHelpActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    @Bean
    protected lateinit var document: BridgesDocument
    override fun doc() = document
}