package com.github.woocash2.Chess.model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PieceFactory {

    public static HashMap<Character, Pair<Board.Piece, Board.Team>> typeByChar = new HashMap<>();
    public static HashMap<Pair<Board.Piece, Board.Team>, Character> charByType = new HashMap<>();

    static {
        typeByChar.put('k', new Pair<>(Board.Piece.KING, Board.Team.WHITE));
        typeByChar.put('K', new Pair<>(Board.Piece.KING, Board.Team.BLACK));
        typeByChar.put('q', new Pair<>(Board.Piece.QUEEN, Board.Team.WHITE));
        typeByChar.put('Q', new Pair<>(Board.Piece.QUEEN, Board.Team.BLACK));
        typeByChar.put('n', new Pair<>(Board.Piece.KNIGHT, Board.Team.WHITE));
        typeByChar.put('N', new Pair<>(Board.Piece.KNIGHT, Board.Team.BLACK));
        typeByChar.put('b', new Pair<>(Board.Piece.BISHOP, Board.Team.WHITE));
        typeByChar.put('B', new Pair<>(Board.Piece.BISHOP, Board.Team.BLACK));
        typeByChar.put('r', new Pair<>(Board.Piece.ROOK, Board.Team.WHITE));
        typeByChar.put('R', new Pair<>(Board.Piece.ROOK, Board.Team.BLACK));
        typeByChar.put('p', new Pair<>(Board.Piece.PAWN, Board.Team.WHITE));
        typeByChar.put('P', new Pair<>(Board.Piece.PAWN, Board.Team.BLACK));
        // moved pieces
        typeByChar.put('x', new Pair<>(Board.Piece.KINGM, Board.Team.WHITE));
        typeByChar.put('X', new Pair<>(Board.Piece.KINGM, Board.Team.BLACK));
        typeByChar.put('y', new Pair<>(Board.Piece.ROOKM, Board.Team.WHITE));
        typeByChar.put('Y', new Pair<>(Board.Piece.ROOKM, Board.Team.BLACK));
        typeByChar.put('z', new Pair<>(Board.Piece.PAWNM, Board.Team.WHITE));
        typeByChar.put('Z', new Pair<>(Board.Piece.PAWNM, Board.Team.BLACK));
        // just moved
        typeByChar.put('t', new Pair<>(Board.Piece.PAWNJ, Board.Team.WHITE));
        typeByChar.put('T', new Pair<>(Board.Piece.PAWNJ, Board.Team.BLACK));
        //empty
        typeByChar.put('-', new Pair<>(Board.Piece.EMPTY, Board.Team.EMPTY));

        for (Map.Entry<Character, Pair<Board.Piece, Board.Team>> entry : typeByChar.entrySet()) {
            charByType.put(entry.getValue(), entry.getKey());
        }
    }


}
