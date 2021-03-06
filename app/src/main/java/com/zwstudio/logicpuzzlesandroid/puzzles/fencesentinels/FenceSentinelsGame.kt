package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FenceSentinelsGame(layout: List<String>, gi: GameInterface<FenceSentinelsGame, FenceSentinelsGameMove, FenceSentinelsGameState>, gdi: GameDocumentInterface) : CellsGame<FenceSentinelsGame, FenceSentinelsGameMove, FenceSentinelsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    var pos2hint = mutableMapOf<Position, Int>()

    override fun isValid(row: Int, col: Int) = row >= 0 && col >= 0 && row < size.row - 1 && col < size.col - 1

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        for (r in 0 until rows - 1) {
            val str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = FenceSentinelsGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}