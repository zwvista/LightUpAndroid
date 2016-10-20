package com.zwstudio.logicgamesandroid.slitherlink.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.ArrayList;
import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkGame {
    public enum MarkerOptions {
        NoMarker, MarkerAfterLightbulb, MarkerBeforeLightbulb
    }
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Position size;
    public int rows() {return size.row;}
    public int cols() {return size.col;}
    public boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < size.row && col < size.col;
    }
    public boolean isValid(Position p) {
        return isValid(p.row, p.col);
    }

    private int stateIndex = 0;
    private List<SlitherLinkGameState> states = new ArrayList<>();
    private SlitherLinkGameState state() {return states.get(stateIndex);}
    private List<SlitherLinkGameMove> moves = new ArrayList<>();
    private SlitherLinkGameMove move() {return moves.get(stateIndex - 1);}
    public SlitherLinkGameInterface gi;

    public boolean isSolved() {return state().isSolved;}
    public boolean canUndo() {return stateIndex > 0;}
    public boolean canRedo() {return stateIndex < states.size() - 1;}
    public int moveIndex() {return stateIndex;}
    public int moveCount() {return states.size() - 1;}

    private void moveAdded(SlitherLinkGameMove move) {
        if (gi == null) return;
        gi.moveAdded(this, move);
    }

    private void levelInitilized(SlitherLinkGameState state) {
        if (gi == null) return;
        gi.levelInitilized(this, state);
        if (isSolved()) gi.gameSolved(this);
    }

    private void levelUpdated(SlitherLinkGameState stateFrom, SlitherLinkGameState stateTo) {
        if (gi == null) return;
        gi.levelUpdated(this, stateFrom, stateTo);
        if (isSolved()) gi.gameSolved(this);
    }

    public SlitherLinkGame(List<String> layout, SlitherLinkGameInterface gi) {
        this.gi = gi;
        size = new Position(layout.size(), layout.get(0).length());
        SlitherLinkGameState state = new SlitherLinkGameState(this);
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                if (ch == 'W' || ch >= '0' && ch <= '9') {
                    SlitherLinkWallObject o = new SlitherLinkWallObject();
                    o.lightbulbs = ch == 'W' ? -1 : (ch - '0');
                    o.state = o.lightbulbs <= 0 ? SlitherLinkWallObject.WallState.Complete : SlitherLinkWallObject.WallState.Normal;
                    state.set(r, c, o);
                } else
                    state.set(r, c, new SlitherLinkEmptyObject());
            }
        }
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(Position p, F2<SlitherLinkGameState, SlitherLinkGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        SlitherLinkGameState state = state().clone();
        SlitherLinkGameMove move = new SlitherLinkGameMove();
        boolean changed = f.f(state, move);
        if (changed) {
            states.add(state);
            stateIndex++;
            moves.add(move);
            moveAdded(move);
            levelUpdated(states.get(stateIndex - 1), state);
        }
        return changed;
   }

    public boolean switchObject(Position p, MarkerOptions markerOption, boolean normalLightbulbsOnly) {
        return changeObject(p, (state, move) -> state.switchObject(p, markerOption, normalLightbulbsOnly, move));
    }

    public boolean setObject(Position p, SlitherLinkObject objNew) {
        return changeObject(p, (state, move) -> state.setObject(p, objNew, move));
    }

    public SlitherLinkObject getObject(Position p) {
        return state().get(p);
    }

    public SlitherLinkObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public void undo() {
        if (!canUndo()) return;
        stateIndex--;
        levelUpdated(states.get(stateIndex + 1), state());
    }

    public void redo() {
        if (!canRedo()) return;
        stateIndex++;
        levelUpdated(states.get(stateIndex - 1), state());
    }
}