package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotObject
import org.androidannotations.annotations.EBean

@EBean
class FourMeNotDocument : GameDocument<FourMeNotGame, FourMeNotGameMove>() {
    override fun saveMove(move: FourMeNotGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        FourMeNotGameMove(Position(rec.row, rec.col), FourMeNotObject.objFromString(rec.strValue1))
}