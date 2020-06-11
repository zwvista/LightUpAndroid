package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.android.MosaikGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.android.MosaikOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MosaikMainActivity : GameMainActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        MosaikOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        MosaikGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MosaikOptionsActivity : GameOptionsActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class MosaikHelpActivity : GameHelpActivity<MosaikGame, MosaikDocument, MosaikGameMove, MosaikGameState>() {
    @Bean
    protected lateinit var document: MosaikDocument
    override fun doc() = document
}