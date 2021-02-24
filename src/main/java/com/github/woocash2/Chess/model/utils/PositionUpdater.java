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
            Piece nPiece = afterMove.get(piece.x, piece.y);
            nPiece.move(p.getKey(), p.getValue());

            if (afterMove.whiteKingPos == null || afterMove.blackKingPos == null) {
                afterMove.printBoard();
                System.out.println(piece.x + " " + piece.y + " " + p.getKey() + " " + p.getValue());
            }

            if (piece.team == Piece.Team.WHITE && afterMove.isWhiteKingAttacked()) {
                return false;
            }
            if (piece.team == Piece.Team.BLACK && afterMove.isBlackKingAttacked()) {
                return false;
            }
            return piece.reachablePositions.add(p);
        };
    }

    public static Function<Pair<Integer, Integer>, Boolean> addToTakeableFunction(Piece piece) {
        return p -> {
            Board afterMove = new Board(piece.board);
            Piece nPiece = afterMove.get(piece.x, piece.y);
            nPiece.move(p.getKey(), p.getValue());
            int a = p.getKey(), b = p.getValue();
            if (piece.team == Piece.Team.WHITE && piece.board.get(a, b).team == Piece.Team.BLACK && !afterMove.isWhiteKingAttacked())
                return piece.takeablePositions.add(p);
            if (piece.team == Piece.Team.BLACK && piece.board.get(a, b).team == Piece.Team.WHITE && !afterMove.isBlackKingAttacked())
                return piece.takeablePositions.add(p);
            return false;
        };
    }

    public static Function<Pair<Integer, Integer>, Boolean> addToTakeableEnPassantFunction(Piece pieceA, Piece pieceB) {
        return p -> {
            Board afterMove = new Board(pieceA.board);
            afterMove.move(pieceB.x, pieceB.y, p.getKey(), p.getValue());
            afterMove.move(pieceA.x, pieceA.y, p.getKey(), p.getValue());
            int a = p.getKey(), b = p.getValue();
            if (pieceA.team == Piece.Team.WHITE && pieceA.board.get(pieceB.x, pieceB.y).team == Piece.Team.BLACK && !afterMove.isWhiteKingAttacked())
                return pieceA.takeablePositions.add(p);
            if (pieceA.team == Piece.Team.BLACK && pieceA.board.get(pieceB.x, pieceB.y).team == Piece.Team.WHITE && !afterMove.isBlackKingAttacked())
                return pieceA.takeablePositions.add(p);
            return false;
        };
    }
}
