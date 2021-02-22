package com.github.woocash2.Chess.model.utils;

import com.github.woocash2.Chess.model.Pawn;
import javafx.util.Pair;
import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.Piece;

import java.util.function.Function;

public interface PositionUpdater {

    public static Function<Pair<Integer, Integer>, Boolean> addToReachableFunction(Piece piece) {
        return p -> {
            Board afterMove = new Board(piece.board);
            afterMove.move(piece.x, piece.y, p.getKey(), p.getValue());
            if (piece.color == Piece.team.WHITE && afterMove.isWhiteKingAttacked())
                return false;
            if (piece.color == Piece.team.BLACK && afterMove.isBlackKingAttacked())
                return false;
            return piece.reachablePositions.add(p);
        };
    }

    public static Function<Pair<Integer, Integer>, Boolean> addToTakeableFunction(Piece piece) {
        return p -> {
            Board afterMove = new Board(piece.board);
            afterMove.move(piece.x, piece.y, p.getKey(), p.getValue());
            int a = p.getKey(), b = p.getValue();
            if (piece.color == Piece.team.WHITE && Character.isUpperCase(piece.board.get(a, b)) && !afterMove.isWhiteKingAttacked())
                return piece.takeablePositions.add(p);
            if (piece.color == Piece.team.BLACK && Character.isLowerCase(piece.board.get(a, b)) && !afterMove.isBlackKingAttacked())
                return piece.takeablePositions.add(p);
            return false;
        };
    }

    public static Function<Pair<Integer, Integer>, Boolean> addToTakeableEnPassantFunction(Pawn pieceA, Pawn pieceB) {
        return p -> {
            Board afterMove = new Board(pieceA.board);
            afterMove.move(pieceB.x, pieceB.y, p.getKey(), p.getValue());
            afterMove.move(pieceA.x, pieceA.y, p.getKey(), p.getValue());
            int a = p.getKey(), b = p.getValue();
            if (pieceA.color == Piece.team.WHITE && Character.isUpperCase(pieceA.board.get(pieceB.x, pieceB.y)) && !afterMove.isWhiteKingAttacked())
                return pieceA.takeablePositions.add(p);
            if (pieceA.color == Piece.team.BLACK && Character.isLowerCase(pieceA.board.get(pieceB.x, pieceB.y)) && !afterMove.isBlackKingAttacked())
                return pieceA.takeablePositions.add(p);
            return false;
        };
    }
}
