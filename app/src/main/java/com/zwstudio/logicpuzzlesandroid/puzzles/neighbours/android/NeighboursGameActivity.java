package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data.NeighboursDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class NeighboursGameActivity extends GameGameActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState> {
    public NeighboursDocument doc() {return app.neighboursDocument;}

    protected NeighboursGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new NeighboursGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o._1().equals(selectedLevelID)).orSome(0))._2();
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new NeighboursGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                NeighboursGameMove move = doc().loadMove(rec);
                game.setObject(move);
            }
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Click
    protected void btnHelp() {
        NeighboursHelpActivity_.intent(this).start();
    }
}
