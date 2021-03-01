package com.github.woocash2.Chess.model;

import com.github.woocash2.Chess.controller.GameController;
import com.github.woocash2.Chess.model.utils.PieceSquareTable;
import com.github.woocash2.Chess.test.BoardTemplate;
import javafx.util.Pair;
import jdk.nio.mapmode.ExtendedMapMode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Computer {

    public Board board;
    public GameController gameController;
    public Board.Team team;

    public int pawnPts = 100;
    public int bishopPts = 300;
    public int knightPts = 300;
    public int rookPts = 500;
    public int queenPts = 900;
    public int kingPts = 200;

    public double piecesWeight = 1;
    public double attackingWeight = 0.25;
    public double positionWeight = 1;

    public double maxEval = 1000000.0;

    Map<Board.Piece, Integer> points = new HashMap<>();

    int[] reachedPositions = new int[20];

    public Computer(GameController controller, Board.Team tm) {
        gameController = controller;
        team = tm;
        points.put(Board.Piece.PAWN, pawnPts);
        points.put(Board.Piece.PAWNJ, pawnPts);
        points.put(Board.Piece.PAWNM, pawnPts);
        points.put(Board.Piece.BISHOP, bishopPts);
        points.put(Board.Piece.KNIGHT, knightPts);
        points.put(Board.Piece.ROOK, rookPts);
        points.put(Board.Piece.ROOKM, rookPts);
        points.put(Board.Piece.QUEEN, queenPts);
        points.put(Board.Piece.KING, kingPts);
        points.put(Board.Piece.KINGM, kingPts);
    }

    public void addBoard(Board brd) {
        board = brd;
    }

    public Move getRandomMove() {
        Collections.shuffle(board.moves);
        return board.moves.get(0);
    }

    public Move findMove() {
        double wholeTime = 0;
        Pair<Double, Move> best = new Pair<>(0.0, null);
        int maxDepth = 4;

        while (wholeTime < 0.5) {
            long startTime = System.nanoTime();

            Board brd = new Board(board);
            best = minimax(brd, team, 0, maxDepth, -maxEval, maxEval);

            long stopTime = System.nanoTime();
            double t = (double) (stopTime - startTime) / 1e9;
            wholeTime += t;
            //System.out.println("Positions reached at depth: " + maxDepth + ": " + reachedPositions[maxDepth]);
            maxDepth++;
        }

        System.out.println("Time: " + wholeTime + ", Maxdepth: " + (maxDepth - 1) + ", Eval: " + best.getKey());
        return best.getValue();
    }

    public Pair<Double, Move> minimax(Board brd, Board.Team team, int depth, int maxdepth, double alfa, double beta) {
        if (depth == maxdepth) {
            double d = evaluate(brd);
            d -= team == Board.Team.WHITE ? depth : -depth;
            return new Pair<>(d, null);
        }

        brd.updateMoves(team);

        if (brd.moves.size() == 0 && team == Board.Team.WHITE)
            return brd.isWhiteKingAttacked() ? new Pair<>(-maxEval + depth, null) : new Pair<>(0.0, null);
        if (brd.moves.size() == 0 && team == Board.Team.BLACK)
            return brd.isBlackKingAttacked() ? new Pair<>(maxEval - depth, null) : new Pair<>(0.0, null);


        Board.Team opponnent = team == Board.Team.WHITE ? Board.Team.BLACK : Board.Team.WHITE;
        ArrayList<Move> moves = (ArrayList<Move>) brd.moves.clone();
        moves = (ArrayList<Move>) orderedMoves(brd, moves);

        Move best = null;

        for (Move move : moves) {

            brd.move(move);
            Pair<Double, Move> next = minimax(brd, opponnent, depth + 1, maxdepth, alfa, beta);
            brd.unMove(move);

            if (team == Board.Team.WHITE) {
                if (next.getKey() > alfa) {
                    alfa = next.getKey();
                    best = move;
                }
                if (alfa >= beta)
                    return new Pair<>(alfa, move);
            }

            if (team == Board.Team.BLACK) {
                if (next.getKey() < beta) {
                    beta = next.getKey();
                    best = move;
                }
                if (alfa >= beta)
                    return new Pair<>(beta, move);
            }
        }

        return team == Board.Team.WHITE ? new Pair<>(alfa, best) : new Pair<>(beta, best);
    }

    public List<Move> orderedMoves(Board brd, ArrayList<Move> moves) {
        ArrayList<Pair<Move, Double>> moveScores = new ArrayList<>();
        for (Move move : moves) {
            double score = 0.0;
            if (move.taken != Board.Piece.EMPTY) {
                score += 10 * points.get(move.taken) - points.get(move.pre);
            }
            if (move.post != move.pre && move.pre == Board.Piece.PAWNM) {
                score += points.get(move.post);
            }

            ArrayList<Integer> vals = new ArrayList<>();
            vals.add(0);
            Pawn.boardIteration(p -> {
                if (Pawn.isPawn(board.pieces[p.getKey()][p.getValue()]) && board.teams[p.getKey()][p.getValue()] != move.team)
                    vals.add(points.get(move.pre));
                return true;
            }, brd, move.toX, move.toY, move.team);
            score -= vals.get(vals.size() - 1);

            moveScores.add(new Pair<>(move, score));
        }
        moveScores.sort((a, b) -> a.getValue() >= b.getValue() ? (a.getValue().equals(b.getValue()) ? 0 : -1) : 1);
        return moveScores.stream().map(Pair::getKey).collect(Collectors.toList());
    }

    public double attackers(Board brd, int x, int y) {
        Board.Piece piece = brd.pieces[x][y];
        Board.Team t = brd.teams[x][y];
        double multiplier = t == Board.Team.WHITE ? 1 : -1;
        ArrayList<Double> scores = new ArrayList<>();

        Function<Pair<Integer, Integer>, Double> f = p -> {
            if (brd.teams[p.getKey()][p.getValue()] != Board.Team.EMPTY && brd.teams[p.getKey()][p.getValue()] != t)
                scores.add(points.get(brd.pieces[p.getKey()][p.getValue()]) * multiplier);
            return 0.0;
        };

        switch (piece) {
            case KING, KINGM -> King.boardIteration(f, brd, x, y);
            case ROOK, ROOKM -> Rook.boardIteration(f, brd, x, y);
            case PAWN, PAWNJ, PAWNM -> Pawn.boardIteration(f, brd, x, y, t);
            case QUEEN -> { Bishop.boardIteration(f, brd, x, y); Rook.boardIteration(f, brd, x, y); }
            case BISHOP -> Bishop.boardIteration(f, brd, x, y);
            case KNIGHT -> Knight.boardIteration(f, brd, x, y);
        }

        return scores.stream().mapToDouble(Double::doubleValue).sum();
    }

    public double evaluate(Board brd) {
        double eval = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Board.Piece p = brd.pieces[i][j];
                if (p == Board.Piece.EMPTY)
                    continue;

                Board.Team t = brd.teams[i][j];
                int multiplier = t == Board.Team.WHITE ? 1 : -1;

                eval += points.get(p) * piecesWeight * multiplier;
                eval += PieceSquareTable.positionEvaluation(brd, i, j) * positionWeight * multiplier;
                eval -= points.get(p) * multiplier * brd.numOfAttackers(t, i, j) * attackingWeight;
            }
        }
        return eval;
    }



    public static void main(String[] args) {
        Board board = new Board(BoardTemplate.standard);
        Computer computer = new Computer(null, Board.Team.WHITE);
        computer.addBoard(board);
        computer.findMove();
    }
}
