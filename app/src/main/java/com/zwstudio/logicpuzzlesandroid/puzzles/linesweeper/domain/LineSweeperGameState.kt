package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class LineSweeperGameState(game: LineSweeperGame) : CellsGameState<LineSweeperGame, LineSweeperGameMove, LineSweeperGameState>(game) {
    var objArray = Array(rows() * cols()) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        for ((p, n) in game.pos2hint)
            pos2state[p] = if (n == 0) HintState.Complete else HintState.Normal
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Array<Boolean>) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    fun setObject(move: LineSweeperGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game.isHint(p)) return false
        val dir = move.dir
        val p2 = p.add(LineSweeperGame.offset[dir * 2])
        if (!isValid(p2) || game.isHint(p2)) return false
        val dir2 = (dir + 2) % 4
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return true
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/LineSweeper

        Summary
        Draw a single loop, minesweeper style

        Description
        1. Draw a single closed looping path that never crosses itself or branches off.
        2. A number in a cell denotes how many of the 8 adjacent cells are passed
           by the loop.
        3. The loop can only go horizontally or vertically between cells, but
           not over the numbers.
        4. The loop doesn't need to cover all the board.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. A number in a cell denotes how many of the 8 adjacent cells are passed
        // by the loop.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            for (os in LineSweeperGame.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                var hasLine = false
                for (b in this[p2])
                    if (b)
                        hasLine = true
                if (hasLine) n1++
            }
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            loop@ for (c in 0 until cols()) {
                val p = Position(r, c)
                val n = this[p].filter { it }.size
                when (n) {
                    0 -> continue@loop
                    2 -> {
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                    }
                    else -> {
                        // 1. Draw a single closed looping path that never crosses itself or branches off.
                        isSolved = false
                        return
                    }
                }
            }
        for (p in pos2node.keys) {
            val o = get(p)
            for (i in 0..3) {
                if (!o[i]) continue
                val p2 = p.add(LineSweeperGame.offset[i * 2])
                g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        // 1. Draw a single closed looping path that never crosses itself or branches off.
        g.setRootNode(pos2node.values.first())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}