package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.data.Array;

import static fj.data.Array.arrayArray;
import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightBattleShipsGameState extends CellsGameState<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState> {
    public LightBattleShipsObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public LightBattleShipsGameState(LightBattleShipsGame game) {
        super(game);
        objArray = new LightBattleShipsObject[rows() * cols()];
        Arrays.fill(objArray, LightBattleShipsObject.Empty);
        for (Map.Entry<Position, LightBattleShipsObject> entry : game.pos2obj.entrySet()) {
            Position p = entry.getKey();
            LightBattleShipsObject o = entry.getValue();
            set(p, o);
        }
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public LightBattleShipsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public LightBattleShipsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, LightBattleShipsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, LightBattleShipsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(LightBattleShipsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.pos2obj.containsKey(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(LightBattleShipsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<LightBattleShipsObject, LightBattleShipsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        LightBattleShipsObject.Marker : LightBattleShipsObject.BattleShipUnit;
            case BattleShipUnit:
                return LightBattleShipsObject.BattleShipMiddle;
            case BattleShipMiddle:
                return LightBattleShipsObject.BattleShipLeft;
            case BattleShipLeft:
                return LightBattleShipsObject.BattleShipTop;
            case BattleShipTop:
                return LightBattleShipsObject.BattleShipRight;
            case BattleShipRight:
                return LightBattleShipsObject.BattleShipBottom;
            case BattleShipBottom:
                return markerOption == MarkerOptions.MarkerLast ?
                        LightBattleShipsObject.Marker : LightBattleShipsObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        LightBattleShipsObject.BattleShipUnit : LightBattleShipsObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Battle Ships

        Summary
        Play solo Battleships, with the help of the numbers on the border.

        Description
        1. Standard rules of Battleships apply, but you are guessing the other
           player ships disposition, by using the numbers on the borders.
        2. Each number tells you how many ship or ship pieces you're seeing in
           that row or column.
        3. Standard rules apply: a ship or piece of ship can't touch another,
           not even diagonally.
        4. In each puzzle there are
           1 Aircraft Carrier (4 squares)
           2 Destroyers (3 squares)
           3 Submarines (2 squares)
           4 Patrol boats (1 square)

        Variant
        5. Some puzzle can also have a:
           1 Supertanker (5 squares)
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == LightBattleShipsObject.Forbidden)
                    set(r, c, LightBattleShipsObject.Empty);
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++) {
                LightBattleShipsObject o = get(r, c);
                if (o == LightBattleShipsObject.BattleShipTop || o == LightBattleShipsObject.BattleShipBottom ||
                        o == LightBattleShipsObject.BattleShipLeft || o == LightBattleShipsObject.BattleShipRight ||
                        o == LightBattleShipsObject.BattleShipMiddle || o == LightBattleShipsObject.BattleShipUnit)
                    n1++;
            }
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++) {
                LightBattleShipsObject o = get(r, c);
                if (o == LightBattleShipsObject.BattleShipTop || o == LightBattleShipsObject.BattleShipBottom ||
                        o == LightBattleShipsObject.BattleShipLeft || o == LightBattleShipsObject.BattleShipRight ||
                        o == LightBattleShipsObject.BattleShipMiddle || o == LightBattleShipsObject.BattleShipUnit)
                    n1++;
            }
            col2state[c] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                LightBattleShipsObject o = get(r, c);
                if ((o == LightBattleShipsObject.Empty || o == LightBattleShipsObject.Marker) && allowedObjectsOnly && (
                        row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    set(r, c, LightBattleShipsObject.Forbidden);
            }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                LightBattleShipsObject o = get(p);
                if (o == LightBattleShipsObject.BattleShipTop || o == LightBattleShipsObject.BattleShipBottom ||
                        o == LightBattleShipsObject.BattleShipLeft || o == LightBattleShipsObject.BattleShipRight ||
                        o == LightBattleShipsObject.BattleShipMiddle || o == LightBattleShipsObject.BattleShipUnit) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : LightBattleShipsGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(node, pos2node.get(p2));
            }
        }
        Integer[] shipNumbers = {0, 0, 0, 0, 0};
        Integer[] shipNumbers2 = {0, 4, 3, 2, 1};
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            for (Position p : area)
                pos2node.remove(p);
            area.sort(Position::compareTo);
            if (!(area.size() == 1 && get(area.get(0)) == LightBattleShipsObject.BattleShipUnit ||
                    area.size() > 1 && area.size() < 5 && ((
                    iterableList(area).forall(p -> p.row == area.get(0).row) &&
                    get(area.get(0)) == LightBattleShipsObject.BattleShipLeft &&
                    get(area.get(area.size() - 1)) == LightBattleShipsObject.BattleShipRight ||
                    iterableList(area).forall(p -> p.col == area.get(0).col) &&
                    get(area.get(0)) == LightBattleShipsObject.BattleShipTop &&
                    get(area.get(area.size() - 1)) == LightBattleShipsObject.BattleShipBottom) &&
                    Array.range(1, area.size() - 2).forall(i -> get(area.get(i)) == LightBattleShipsObject.BattleShipMiddle)) &&
                    arrayArray(LightBattleShipsGame.offset2).forall(os -> iterableList(area).forall(p -> {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) return true;
                        LightBattleShipsObject o = get(p2);
                        return o == LightBattleShipsObject.Empty || o == LightBattleShipsObject.Forbidden || o == LightBattleShipsObject.Marker;
                    })))) {isSolved = false; return;}
            shipNumbers[area.size()]++;
        }
        if (!Arrays.equals(shipNumbers, shipNumbers2)) isSolved = false;
    }
}