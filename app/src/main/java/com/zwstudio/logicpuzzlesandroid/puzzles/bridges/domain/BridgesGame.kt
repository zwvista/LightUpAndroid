package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BridgesGame(layout: List<String>, gi: GameInterface<BridgesGame, BridgesGameMove, BridgesGameState>, gdi: GameDocumentInterface) : CellsGame<BridgesGame, BridgesGameMove, BridgesGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var islandsInfo = mutableMapOf<Position, BridgesIslandInfo>()
    fun isIsland(p: Position) = islandsInfo.containsKey(p)

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9') {
                    val info = BridgesIslandInfo()
                    info.bridges = ch - '0'
                    islandsInfo[p] = info
                }
            }
        }
        for ((p, info) in islandsInfo) {
            for (i in 0 until 4) {
                val os = offset[i]
                val p2 = p.add(os)
                while (isValid(p2)) {
                    if (isIsland(p2)) {
                        info.neighbors[i] = p2
                        break
                    }
                    p2.addBy(os)
                }
            }
        }
        val state = BridgesGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    fun switchBridges(move: BridgesGameMove): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        if (move.pTo.compareTo(move.pFrom) < 0) {
            val t = move.pFrom
            move.pFrom = move.pTo
            move.pTo = t
        }
        if (!state!!.switchBridges(move)) return false
        states.add(state)
        stateIndex++
        moves.add(move)
        moveAdded(move)
        levelUpdated(states[stateIndex - 1], state)
        return true
    }

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
}