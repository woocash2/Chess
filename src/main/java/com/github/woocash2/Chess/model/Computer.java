package com.github.woocash2.Chess.model;

import com.github.woocash2.Chess.controller.GameController;
import com.github.woocash2.Chess.controller.PieceImg;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Computer {

    public Board board;
    public GameController gameController;
    public Piece.team team;

    public Computer(GameController controller, Piece.team tm) {
        gameController = controller;
        team = tm;
    }

    public void addBoard(Board brd) {
        board = brd;
    }

    public Move getRandomMove() {
        ArrayList<Move> moves = new ArrayList<>();

        for (PieceImg piece : gameController.boardManager.pieces) {
            if (piece.piece.color != team)
                continue;

            int fx = piece.piece.x;
            int fy = piece.piece.y;
            for (Pair<Integer, Integer> pos : piece.piece.reachablePositions) {
                int tx = pos.getKey();
                int ty = pos.getValue();
                moves.add(new Move(fx, fy, tx, ty));
            }
            for (Pair<Integer, Integer> pos : piece.piece.takeablePositions) {
                int tx = pos.getKey();
                int ty = pos.getValue();
                moves.add(new Move(fx, fy, tx, ty));
            }
        }

        Collections.shuffle(moves);
        return moves.get(0);
    }
}
