package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class RoomsDocument extends GameDocument<RoomsGame, RoomsGameMove> {
    protected void saveMove(RoomsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.dir;
        rec.intValue2 = move.obj.ordinal();
    }
    public RoomsGameMove loadMove(MoveProgress rec) {
        return new RoomsGameMove() {{
            p = new Position(rec.row, rec.col);
            dir = rec.intValue1;
            obj = RoomsObject.values()[rec.intValue2];
        }};
    }
    public int getMarkerOption() {
        String o = gameProgress().option1;
        return o == null ? 0 : Integer.parseInt(o);
    }
    public void setMarkerOption(int o) {
        gameProgress().option1 = String.valueOf(o);
    }
}