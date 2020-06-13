package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*

class TataminoGame(layout: List<String>, gi: GameInterface<TataminoGame, TataminoGameMove, TataminoGameState>, gdi: GameDocumentInterface) : CellsGame<TataminoGame, TataminoGameMove, TataminoGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 2, 1, 2)
    }

    var areas = mutableListOf<List<Position>>()
    var pos2area = mutableMapOf<Position, Int>()
    var dots: GridDots
    var objArray: CharArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        dots = GridDots(rows + 1, cols + 1)
        objArray = CharArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                this[r, c] = ch
            }
        }
        for (r in 0 until rows) {
            dots[r, 0, 2] = GridLineObject.Line
            dots[r, cols, 2] = GridLineObject.Line
        }
        for (c in 0 until cols) {
            dots[0, c, 1] = GridLineObject.Line
            dots[rows, c, 1] = GridLineObject.Line
        }
        val state = TataminoGameState(this)
        levelInitilized(state)
    }

    fun switchObject(move: TataminoGameMove) = changeObject(move, TataminoGameState::switchObject)
    fun setObject(move: TataminoGameMove) = changeObject(move, TataminoGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getPosState(p: Position) = currentState.pos2state[p]
    fun getStateDots() = currentState.dots
}