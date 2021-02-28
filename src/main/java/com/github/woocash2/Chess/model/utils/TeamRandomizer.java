package com.github.woocash2.Chess.model.utils;

import com.github.woocash2.Chess.model.Board;

public interface TeamRandomizer {
    public static Board.Team getRandomTeam() {
        double d = Math.random();
        if (d < 0.5)
            return Board.Team.WHITE;
        else
            return Board.Team.BLACK;
    }

    public static Board.Team getOpposite(Board.Team team) {
        if (team == Board.Team.WHITE)
            return Board.Team.BLACK;
        else
            return Board.Team.WHITE;
    }
}
