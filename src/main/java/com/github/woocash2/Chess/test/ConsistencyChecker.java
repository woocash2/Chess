package com.github.woocash2.Chess.test;

import com.github.woocash2.Chess.controller.BoardManager;
import com.github.woocash2.Chess.controller.Tile;
import com.github.woocash2.Chess.model.Board;

public interface ConsistencyChecker {

    public static void compareDisplayWithBoard(BoardManager boardManager) {
        Board board = boardManager.board;
        Tile[][] tiles = boardManager.tiles;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tiles[i][j].pieceImg == null) {
                    if (board.get(i, j) != null) {
                        System.out.println("FAIL: " + i + " " + j);
                        board.printBoard();
                        return;
                    }
                }
                else {
                    if (board.get(i, j) == null || board.get(i, j).identifier != tiles[i][j].pieceImg.piece.identifier) {
                        System.out.println("FAIL: " + i + " " + j);
                        board.printBoard();
                        return;
                    }
                }
            }
        }

        System.out.println("OK");
    }
}
