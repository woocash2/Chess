package model.utils;
import model.*;

public interface PieceFactory {

    public static Piece get(int i, int j, Board board) {
        Piece piece;
        piece = switch (board.get(i, j)) {
            case 'k' -> new King(i, j, Piece.team.WHITE, board);
            case 'K' -> new King(i, j, Piece.team.BLACK, board);
            case 'q' -> new Queen(i, j, Piece.team.WHITE, board);
            case 'Q' -> new Queen(i, j, Piece.team.BLACK, board);
            case 'b' -> new Bishop(i, j, Piece.team.WHITE, board);
            case 'B' -> new Bishop(i, j, Piece.team.BLACK, board);
            case 'r' -> new Rook(i, j, Piece.team.WHITE, board);
            case 'R' -> new Rook(i, j, Piece.team.BLACK, board);
            case 'n' -> new Knight(i, j, Piece.team.WHITE, board);
            case 'N' -> new Knight(i, j, Piece.team.BLACK, board);
            case 'p' -> new Pawn(i, j, Piece.team.WHITE, board);
            default -> new Pawn(i, j, Piece.team.BLACK, board);
        };
        return piece;
    }

    public static Piece putOnBoardAndGet(char name, int i, int j, Board board) {
        board.set(i, j, name);
        return get(i, j, board);
    }
}
