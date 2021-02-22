package com.github.woocash2.Chess.model.utils;

import com.github.woocash2.Chess.model.Piece;

import java.util.Random;

public interface TeamRandomizer {
    public static Piece.team getRandomTeam() {
        double d = Math.random();
        if (d < 0.5)
            return Piece.team.WHITE;
        else
            return Piece.team.BLACK;
    }

    public static Piece.team getOpposite(Piece.team team) {
        if (team == Piece.team.WHITE)
            return Piece.team.BLACK;
        else
            return Piece.team.WHITE;
    }
}
