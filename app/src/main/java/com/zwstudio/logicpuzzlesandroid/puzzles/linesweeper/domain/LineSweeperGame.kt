package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class LineSweeperGame(layout: List<String>, gi: GameInterface<LineSweeperGame, LineSweeperGameMove, LineSweeperGameState>, gdi: GameDocumentInterface) : CellsGame<LineSweeperGame, LineSweeperGameMove, LineSweeperGameState>(gi, gdi) {
    var pos2hint: MutableMap<Position?, Int> = HashMap()
    fun isHint(p: Position?): Boolean {
        return pos2hint.containsKey(p)
    }

    private fun changeObject(move: LineSweeperGameMove, f: (LineSweeperGameState, LineSweeperGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        val changed = f.f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun setObject(move: LineSweeperGameMove?): Boolean {
        return changeObject(move, F2 { state: LineSweeperGameState?, move2: LineSweeperGameMove? -> state!!.setObject(move2) })
    }

    fun getObject(p: Position?): Array<Boolean?>? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Array<Boolean>? {
        return state()!![row, col]
    }

    fun pos2State(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1))
    }

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        for (r in 0 until rows() - 1) {
            val str = layout[r]
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch >= '0' && ch <= '9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = LineSweeperGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}