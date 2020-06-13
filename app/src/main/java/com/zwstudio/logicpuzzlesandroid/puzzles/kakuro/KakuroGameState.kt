package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import java.util.*

class KakuroGameState(game: KakuroGame) : CellsGameState<KakuroGame, KakuroGameMove, KakuroGameState>(game) {
    var pos2num = LinkedHashMap(game.pos2num)
    var pos2horzHint = mutableMapOf<Position, HintState>()
    var pos2vertHint = mutableMapOf<Position, HintState>()

    operator fun get(p: Position) = pos2num[p]
    operator fun set(p: Position, obj: Int) {pos2num[p] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: KakuroGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == null || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: KakuroGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == null) return false
        val o = this[p]!!
        move.obj = (o + 1) % 10
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/Kakuro

        Summary
        Fill the board with numbers 1 to 9 according to the sums

        Description
        1. Your goal is to write a number in every blank tile (without a diagonal
           line).
        2. The number on the top of a column or at the left of a row, gives you
           the sum of the numbers in that column or row.
        3. You can write numbers 1 to 9 in the tiles, however no same number should
           appear in a consecutive row or column.
        4. Tiles which only have a diagonal line aren't used in the game.
    */
    private fun updateIsSolved() {
        isSolved = true
        for ((p, n2) in game.pos2horzHint) {
            var n1 = 0
            val os = KakuroGame.offset[1]
            var n: Int
            val lastN = 0
            var p2 = p + os
            while (pos2num[p2].also { n = it!! } != null) {
                n1 += n
                // 3. You can write numbers 1 to 9 in the tiles, however no same number should
                // appear in a consecutive row.
                if (n == lastN) {
                    isSolved = false
                    pos2horzHint[p2] = HintState.Error
                    pos2horzHint[p2 - os] = HintState.Error
                }
                p2 += os
            }
            // 2. The number on at the left of a row gives you
            // the sum of the numbers in that row.
            val s = if (n1 == 0) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2horzHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        for ((p, n2) in game.pos2vertHint) {
            var n1 = 0
            val os = KakuroGame.offset[2]
            var n: Int
            val lastN = 0
            var p2 = p + os
            while (pos2num[p2].also { n = it!! } != null) {
                n1 += n
                // 3. You can write numbers 1 to 9 in the tiles, however no same number should
                // appear in a consecutive column.
                if (n == lastN) {
                    isSolved = false
                    pos2horzHint[p2] = HintState.Error
                    pos2horzHint[p2 - os] = HintState.Error
                }
                p2 += os
            }
            // 2. The number on the top of a column gives you
            // the sum of the numbers in that column.
            val s = if (n1 == 0) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2vertHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}