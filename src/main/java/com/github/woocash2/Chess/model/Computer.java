package com.github.woocash2.Chess.model;

import com.github.woocash2.Chess.controller.GameController;
import com.github.woocash2.Chess.controller.PieceImg;
import com.github.woocash2.Chess.model.utils.PieceFactory;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Computer {

    public Board board;
    public GameController gameController;
    public Piece.team team;

    public int pawnPts = 100;
    public int bishopPts = 300;
    public int knightPts = 300;
    public int rookPts = 500;
    public int queenPts = 900;
    public int kingPts = 200;

    public double piecesWeight = 1;
    public double attackingWeight = 0.25;

    public double maxEval = 1000000.0;

    Map<Character, Integer> points = new HashMap<>();

    public Computer(GameController controller, Piece.team tm) {
        gameController = controller;
        team = tm;
        points.put('p', pawnPts);
        points.put('P', -pawnPts);
        points.put('b', bishopPts);
        points.put('B', -bishopPts);
        points.put('n', knightPts);
        points.put('N', -knightPts);
        points.put('r', rookPts);
        points.put('R', -rookPts);
        points.put('q', queenPts);
        points.put('Q', -queenPts);
        points.put('k', kingPts);
        points.put('K', -kingPts);
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

    public Move findMove() {
        double wholeTime = 0;
        Pair<Double, Move> best = new Pair<>(0.0, null);
        int maxDepth = 2;

        while (wholeTime < 0.3) {

            long startTime = System.nanoTime();

            Board brd = new Board(board);
            if (team == Piece.team.WHITE)
                best = getMax(brd, 0, maxDepth, maxEval);
            else
                best = getMin(brd, 0, maxDepth, -maxEval);

            long stopTime = System.nanoTime();
            double t = (double) (stopTime - startTime) / 1e9;
            wholeTime += t;
            maxDepth++;
        }

        System.out.println("Time: " + wholeTime + ", Maxdepth: " + maxDepth + ", Eval: " + best.getKey());
        return best.getValue();
    }

    public Pair<Double, Move> getMin(Board brd, int depth, int maxDepth, double currentMax) {
        if (depth == maxDepth)
            return new Pair<Double, Move>(evaluate(brd), null);

        double best = maxEval;
        Move move = null;

        int allMoves = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char c = brd.get(i, j);
                if (c == '-' || Character.isLowerCase(c))
                    continue;
                Piece piece = PieceFactory.get(i, j, brd);
                piece.updatePositions();
                int fx = i;
                int fy = j;

                piece.reachablePositions.addAll(piece.takeablePositions);
                allMoves += piece.reachablePositions.size();

                for (Pair<Integer, Integer> pos : piece.reachablePositions) { // not considering castling or en passant
                    int tx = pos.getKey();
                    int ty = pos.getValue();
                    Board afterMove = new Board(brd);
                    afterMove.move(fx, fy, tx, ty);

                    Pair<Double, Move> next = getMax(afterMove, depth + 1, maxDepth, best);
                    double ev = next.getKey();

                    if (ev < best) {
                        best = ev;
                        move = new Move(fx, fy, tx, ty);
                    }

                    if (best <= currentMax)
                        return new Pair<>(best, move);
                }
            }
        }

        if (allMoves == 0) {
            return brd.isBlackKingAttacked() ? new Pair<>(maxEval - depth, null) : new Pair<>(0.0, null);
        }

        return new Pair<>(best, move);
    }

    public Pair<Double, Move> getMax(Board brd, int depth, int maxDepth, double currentMin) {
        if (depth == maxDepth)
            return new Pair<Double, Move>(evaluate(brd), null);

        double best = -maxEval;
        Move move = null;

        int allMoves = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char c = brd.get(i, j);
                if (c == '-' || Character.isUpperCase(c))
                    continue;
                Piece piece = PieceFactory.get(i, j, brd);
                piece.updatePositions();
                int fx = i;
                int fy = j;

                piece.reachablePositions.addAll(piece.takeablePositions);
                allMoves += piece.reachablePositions.size();

                for (Pair<Integer, Integer> pos : piece.reachablePositions) {
                    int tx = pos.getKey();
                    int ty = pos.getValue();
                    Board afterMove = new Board(brd);
                    afterMove.move(fx, fy, tx, ty);

                    Pair<Double, Move> next = getMin(afterMove, depth + 1, maxDepth, best);
                    double ev = next.getKey();

                    if (ev > best) {
                        best = ev;
                        move = new Move(fx, fy, tx, ty);
                    }

                    if (best >= currentMin)
                        return new Pair<>(best, move);
                }
            }
        }

        if (allMoves == 0) {
            return brd.isWhiteKingAttacked() ? new Pair<>(-maxEval + depth, null) : new Pair<>(0.0, null);
        }

        return new Pair<>(best, move);
    }

    public double evaluate(Board brd) {
        double eval = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char c = brd.get(i, j);
                if (c == '-')
                    continue;
                Piece.team t = Character.isLowerCase(c) ? Piece.team.WHITE : Piece.team.BLACK;
                eval += points.get(c) * (piecesWeight - brd.numOfAttackers(t, i, j) * attackingWeight);
            }
        }
        return eval;
    }
}
