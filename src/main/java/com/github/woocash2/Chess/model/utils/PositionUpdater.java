package com.github.woocash2.Chess.model.utils;

import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.Move;
import javafx.util.Pair;

import java.util.function.Function;

public interface PositionUpdater {

    public static Board.Piece preToPos(Board.Piece piece) {
        return switch (piece) {
            case PAWN -> Board.Piece.PAWNJ;
            case PAWNJ -> Board.Piece.PAWNM;
            case KING -> Board.Piece.KINGM;
            case ROOK -> Board.Piece.ROOKM;
            default -> piece;
        };
    }

    public static Function<Pair<Integer, Integer>, Boolean> addMoveIfLegal(Board board, int x, int y) {
        return p -> {
            boolean legal = true;
            int a = p.getKey(), b = p.getValue();
            Board.Team team = board.teams[x][y];

            if (board.teams[a][b] == team)
                return false;

            Move move = new Move(x, y, a, b, board.pieces[x][y], preToPos(board.pieces[x][y]), board.teams[x][y], board.pieces[a][b]);
            board.move(move);

            if (team == Board.Team.WHITE && board.isWhiteKingAttacked())
                legal = false;
            if (team == Board.Team.BLACK && board.isBlackKingAttacked())
                legal = false;

            board.unMove(move);

            if (!legal)
                return false;

            if (move.post == Board.Piece.PAWNM && ((move.team == Board.Team.WHITE && move.toX == 0) || (move.team == Board.Team.BLACK && move.toX == 7))) {
                for (Board.Piece po : new Board.Piece[] {Board.Piece.QUEEN, Board.Piece.ROOK, Board.Piece.BISHOP, Board.Piece.KNIGHT}) {
                    Move m = new Move(move);
                    m.post = po;
                    board.moves.add(m);
                }
                return true;
            }

            return board.moves.add(move);
        };
    }

    public static Move getMove(Board board, int x, int y, int a, int b) {
        return new Move(x, y, a, b, board.pieces[x][y], preToPos(board.pieces[x][y]), board.teams[x][y], board.pieces[a][b]);
    }
}
