package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class TierraDelFuegoObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> TierraDelFuegoMarkerObject()
            "tree" -> TierraDelFuegoTreeObject()
            else -> TierraDelFuegoEmptyObject()
        }
    }
}

class TierraDelFuegoEmptyObject : TierraDelFuegoObject()

class TierraDelFuegoForbiddenObject : TierraDelFuegoObject() {
    override fun objAsString() = "forbidden"
}

class TierraDelFuegoHintObject(var state: HintState = HintState.Normal, var id: Char = '0') : TierraDelFuegoObject() {
    override fun objAsString() = "hint"
}

class TierraDelFuegoMarkerObject : TierraDelFuegoObject() {
    override fun objAsString() = "marker"
}

class TierraDelFuegoTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TierraDelFuegoObject() {
    override fun objAsString() = "tree"
}

class TierraDelFuegoGameMove(val p: Position, var obj: TierraDelFuegoObject = TierraDelFuegoEmptyObject())
