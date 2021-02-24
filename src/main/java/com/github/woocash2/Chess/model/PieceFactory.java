package com.github.woocash2.Chess.model;
import com.github.woocash2.Chess.model.*;

import java.util.HashMap;

public class PieceFactory {

    public static HashMap<Character, Piece.Type> typeByChar = new HashMap<>();
    public static HashMap<Piece.Type, Character> charByType = new HashMap<>();

    static {
        typeByChar.put('k', Piece.Type.KING);
        typeByChar.put('K', Piece.Type.KING);
        typeByChar.put('q', Piece.Type.QUEEN);
        typeByChar.put('Q', Piece.Type.QUEEN);
        typeByChar.put('n', Piece.Type.KNIGHT);
        typeByChar.put('N', Piece.Type.KNIGHT);
        typeByChar.put('b', Piece.Type.BISHOP);
        typeByChar.put('B', Piece.Type.BISHOP);
        typeByChar.put('r', Piece.Type.ROOK);
        typeByChar.put('R', Piece.Type.ROOK);
        typeByChar.put('p', Piece.Type.PAWN);
        typeByChar.put('P', Piece.Type.PAWN);
        // moved pieces
        typeByChar.put('y', Piece.Type.ROOK);
        typeByChar.put('Y', Piece.Type.ROOK);
        typeByChar.put('x', Piece.Type.KING);
        typeByChar.put('X', Piece.Type.KING);
        // just moved
        typeByChar.put('z', Piece.Type.PAWN);
        typeByChar.put('Z', Piece.Type.PAWN);


        charByType.put(Piece.Type.KING, 'k');
        charByType.put(Piece.Type.QUEEN, 'q');
        charByType.put(Piece.Type.KNIGHT, 'n');
        charByType.put(Piece.Type.BISHOP, 'b');
        charByType.put(Piece.Type.ROOK, 'r');
        charByType.put(Piece.Type.PAWN, 'p');
    }


    public Piece get(int i, int j, char c, Board board) {
        if (c == '-')
            return null;
        Piece.Team team = Character.isLowerCase(c) ? Piece.Team.WHITE : Piece.Team.BLACK;
        Piece.Type type = typeByChar.get(c);
        Piece piece = new Piece(i, j, team, type, board, c);
        if (Character.toLowerCase(c) == 'x' || Character.toLowerCase(c) == 'y')
            piece.moved = true;
        if (Character.toLowerCase(c) == 'z')
            piece.justMoved = piece.justMoved = true;
        return piece;
    }
}
