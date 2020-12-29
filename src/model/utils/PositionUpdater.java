package model.utils;

import javafx.util.Pair;
import model.Piece;

import java.util.function.Function;

public interface PositionUpdater {

    public static Function<Pair<Integer, Integer>, Boolean> addToReachableFunction(Piece piece) {
        return p -> piece.reachablePositions.add(p);
    }
    public static Function<Pair<Integer, Integer>, Boolean> addToTakeableFunction(Piece piece) {
        return p -> {
            int a = p.getKey(), b = p.getValue();
            if (piece.color == Piece.team.WHITE && Character.isUpperCase(piece.board.get(a, b)))
                return piece.takeablePositions.add(p);
            if (piece.color == Piece.team.BLACK && Character.isLowerCase(piece.board.get(a, b)))
                return piece.takeablePositions.add(p);
            return false;
        };
    }
}
