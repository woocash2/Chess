package com.github.woocash2.Chess.model.utils;

import com.github.woocash2.Chess.model.Board;
import com.github.woocash2.Chess.model.Piece;

import java.util.function.Function;

public interface BoardCode {

    // something similar to FEN but strings are a bit shorter
    public static String encodeBoard(Board board) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        int emptyCnt = 0;

        while (i < 64) {
            int x = i / 8;
            int y = i % 8;

            if (board.isEmpty(x, y)) {
                emptyCnt++;
            }
            else {
                Piece piece = board.get(x, y);
                Function<Character, Character> f = piece.team == Piece.Team.WHITE ? Character::toLowerCase : Character::toUpperCase;
                char ident = switch (piece.type) {
                    case KING -> piece.moved ? f.apply('x') : f.apply('k');
                    case ROOK -> piece.moved ? f.apply('y') : f.apply('r');
                    case PAWN -> piece.moved ? (piece.justMoved ? f.apply('t') : f.apply('z')) : f.apply('p');
                    default -> piece.identifier;
                };
                if (emptyCnt > 0)
                    builder.append(emptyCnt);
                builder.append(ident);
                emptyCnt = 0;
            }
            i++;
        }
        if (emptyCnt > 0)
            builder.append(emptyCnt);

        return builder.toString();
    }
}
