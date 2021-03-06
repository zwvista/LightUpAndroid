package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class GardenerMainActivity : GameMainActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    @Bean
    protected lateinit var document: GardenerDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        GardenerOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        GardenerGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class GardenerOptionsActivity : GameOptionsActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    @Bean
    protected lateinit var document: GardenerDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class GardenerHelpActivity : GameHelpActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    @Bean
    protected lateinit var document: GardenerDocument
    override val doc get() = document
}