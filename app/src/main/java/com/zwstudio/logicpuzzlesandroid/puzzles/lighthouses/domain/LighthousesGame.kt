package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LighthousesGame(layout: List<String>, gi: GameInterface<LighthousesGame, LighthousesGameMove, LighthousesGameState>, gdi: GameDocumentInterface) : CellsGame<LighthousesGame, LighthousesGameMove, LighthousesGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = LighthousesGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: LighthousesGameMove, f: (LighthousesGameState, LighthousesGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: LighthousesGameMove) = changeObject(move, LighthousesGameState::switchObject)
    fun setObject(move: LighthousesGameMove) = changeObject(move, LighthousesGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun pos2State(p: Position) = state()!!.pos2state[p]
}