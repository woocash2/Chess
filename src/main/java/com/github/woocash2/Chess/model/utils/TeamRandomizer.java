package com.github.woocash2.Chess.model.utils;

import com.github.woocash2.Chess.model.Piece;

public interface TeamRandomizer {
    public static Piece.Team getRandomTeam() {
        double d = Math.random();
        if (d < 0.5)
            return Piece.Team.WHITE;
        else
            return Piece.Team.BLACK;
    }

    public static Piece.Team getOpposite(Piece.Team team) {
        if (team == Piece.Team.WHITE)
            return Piece.Team.BLACK;
        else
            return Piece.Team.WHITE;
    }
}
