package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.data.CarpentersWallDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGame
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
open class CarpentersWallOptionsActivity : GameOptionsActivity<CarpentersWallGame?, CarpentersWallDocument?, CarpentersWallGameMove?, CarpentersWallGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: CarpentersWallDocument? = null
    override fun doc(): CarpentersWallDocument {
        return document!!
    }
}