package com.github.woocash2.Chess.model;

import com.github.woocash2.Chess.model.utils.PositionUpdater;

public interface Queen {

    public static void updatePositions(Piece piece) {
        piece.reachablePositions.clear();
        piece.takeablePositions.clear();
        Bishop.boardIteration(PositionUpdater.addToReachableFunction(piece), PositionUpdater.addToTakeableFunction(piece), piece.board, piece.x, piece.y);
        Rook.boardIteration(PositionUpdater.addToReachableFunction(piece), PositionUpdater.addToTakeableFunction(piece), piece.board, piece.x, piece.y);
    }
}
