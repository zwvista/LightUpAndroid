package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LightenUpDocument extends GameDocument<LightenUpGame, LightenUpGameMove> {
    protected void saveMove(LightenUpGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public LightenUpGameMove loadMove(MoveProgress rec) {
        return new LightenUpGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = LightenUpObject.objTypeFromString(rec.strValue1);
        }};
    }
    public int getMarkerOption() {
        String o = gameProgress().option1;
        return o == null ? 0 : Integer.parseInt(o);
    }
    public void setMarkerOption(int o) {
        gameProgress().option1 = String.valueOf(o);
    }
    public boolean isNormalLightbulbsOnly() {
        String o = gameProgress().option1;
        return o != null;
    }
    public void setNormalLightbulbsOnly(boolean o) {
        gameProgress().option1 = String.valueOf(o);
    }
}