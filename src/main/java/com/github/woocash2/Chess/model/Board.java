package com.github.woocash2.Chess.model;

import com.github.woocash2.Chess.controller.PieceImg;
import com.github.woocash2.Chess.model.utils.TeamRandomizer;
import com.github.woocash2.Chess.test.BoardTemplate;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Function;

public class Board {

    public Piece[][] pieces = new Piece[8][8];
    public Pair<Integer, Integer> whiteKingPos, blackKingPos;
    static PieceFactory factory = new PieceFactory();

    public Board(char[][] field) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = factory.get(i, j, field[i][j], this);
                if (pieces[i][j] == null)
                    continue;

                if (pieces[i][j].type == Piece.Type.KING && pieces[i][j].team == Piece.Team.WHITE)
                    whiteKingPos = new Pair<>(i, j);
                if (pieces[i][j].type == Piece.Type.KING && pieces[i][j].team == Piece.Team.BLACK)
                    blackKingPos = new Pair<>(i, j);
            }
        }
    }

    public Board() {
        this(BoardTemplate.standard);
    }

    public Board(Board brd) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (brd.pieces[i][j] == null) {
                    pieces[i][j] = null;
                    continue;
                }

                pieces[i][j] = new Piece(brd.pieces[i][j], this);
                if (pieces[i][j].type == Piece.Type.KING && pieces[i][j].team == Piece.Team.WHITE)
                    whiteKingPos = new Pair<>(i, j);
                if (pieces[i][j].type == Piece.Type.KING && pieces[i][j].team == Piece.Team.BLACK)
                    blackKingPos = new Pair<>(i, j);
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] == null)
                    System.out.print('-');
                else
                    System.out.print(pieces[i][j].identifier);
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isEmpty(int x, int y) {
        return pieces[x][y] == null;
    }
    public Piece get(int x, int y) { return pieces[x][y]; }
    public boolean inBoardRange(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }


    public static void main(String[] args) {
        Board board = new Board();
        board.printBoard();
    }

    public void move(int x, int y, int a, int b) {
        pieces[a][b] = pieces[x][y];
        pieces[x][y] = null;
        if (pieces[a][b] == null)
            printBoard();
        if (pieces[a][b].identifier == 'k')
            whiteKingPos = new Pair<>(a, b);
        if (pieces[a][b].identifier == 'K')
            blackKingPos = new Pair<>(a, b);
        if (pieces[a][b].type == Piece.Type.PAWN)
            updatePawnsExcept(a, b);
    }

    public int numOfAttackers(Piece.Team team, int x, int y) { // is the field x, y attacked by a piece from (team)^(-1)
        Function<Character, Boolean> isOpponent;
        if (team == Piece.Team.WHITE)
            isOpponent = Character::isUpperCase;
        else
            isOpponent = Character::isLowerCase;

        ArrayList<Pair<Integer, Integer>> attackers = new ArrayList<>();
        Rook.boardIteration(p -> null, p -> {
            char c = pieces[p.getKey()][p.getValue()].identifier;
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'q' || Character.toLowerCase(c) == 'r') && attackers.add(p);
        }, this, x, y);
        Bishop.boardIteration(p -> null, p -> {
            char c = pieces[p.getKey()][p.getValue()].identifier;
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'q' || Character.toLowerCase(c) == 'b') && attackers.add(p);
        }, this, x, y);

        Knight.boardIteration(p -> null, p -> {
            char c = pieces[p.getKey()][p.getValue()].identifier;
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'n') && attackers.add(p);
        }, this, x, y);

        King.boardIteration(p -> null, p -> {
            char c = pieces[p.getKey()][p.getValue()].identifier;
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'k') && attackers.add(p);
        }, this, x, y);

        Pawn.boardIteration(p -> null, p -> {
            char c = pieces[p.getKey()][p.getValue()].identifier;
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'p') && attackers.add(p);
        }, this, x, y, team);

        // en passant
        Piece piece = pieces[x][y];
        if (piece != null && piece.type == Piece.Type.PAWN && piece.justMoved) {
            if (piece.team == Piece.Team.WHITE && piece.x == 4) {
                if (inBoardRange(x, y - 1) && pieces[x][y - 1] != null && pieces[x][y - 1].type == Piece.Type.PAWN && pieces[x][y - 1].team == Piece.Team.BLACK)
                    attackers.add(new Pair<>(x, y - 1));
                if (inBoardRange(x, y + 1) && pieces[x][y + 1] != null && pieces[x][y + 1].type == Piece.Type.PAWN && pieces[x][y + 1].team == Piece.Team.BLACK)
                    attackers.add(new Pair<>(x, y + 1));
            }
            if (piece.team == Piece.Team.BLACK && piece.x == 3 && piece.justMoved) {
                if (inBoardRange(x, y - 1) && pieces[x][y - 1] != null && pieces[x][y - 1].type == Piece.Type.PAWN && pieces[x][y - 1].team == Piece.Team.WHITE)
                    attackers.add(new Pair<>(x, y - 1));
                if (inBoardRange(x, y + 1) && pieces[x][y + 1] != null && pieces[x][y + 1].type == Piece.Type.PAWN && pieces[x][y + 1].team == Piece.Team.WHITE)
                    attackers.add(new Pair<>(x, y + 1));
            }
        }

        return attackers.size();
    }

    public boolean isWhiteKingAttacked() {
        return numOfAttackers(Piece.Team.WHITE, whiteKingPos.getKey(), whiteKingPos.getValue()) > 0;
    }

    public boolean isBlackKingAttacked() {
        return numOfAttackers(Piece.Team.BLACK, blackKingPos.getKey(), blackKingPos.getValue()) > 0;
    }

    public void set(int i, int j, Piece piece) {
        pieces[i][j] = piece;
    }
    public void takeAway(int i, int j) { set(i, j, null); }

    public boolean isCheckMated(Piece.Team team) {
        Piece king;
        if (team == Piece.Team.WHITE) king = pieces[whiteKingPos.getKey()][whiteKingPos.getValue()];
        else king = pieces[blackKingPos.getKey()][blackKingPos.getValue()];

        if (numOfAttackers(team, king.x, king.y) > 0) {
            return numberOfPossibleMoves(team) == 0;
        }

        return false;
    }

    public boolean isStaleMate(Piece.Team team) {
        Piece king;
        if (team == Piece.Team.WHITE) king = pieces[whiteKingPos.getKey()][whiteKingPos.getValue()];
        else king = pieces[blackKingPos.getKey()][blackKingPos.getValue()];

        if (numOfAttackers(team, king.x, king.y) > 0)
            return false;

        return numberOfPossibleMoves(team) == 0;
    }

    public int numberOfPossibleMoves(Piece.Team team) {
        int possibleMoves = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] == null || pieces[i][j].team != team)
                    continue;

                possibleMoves += pieces[i][j].reachablePositions.size();
                possibleMoves += pieces[i][j].takeablePositions.size();
            }
        }

        return possibleMoves;
    }

    public void updatePawnsExcept(int x, int y) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == x && j == y)
                    continue;
                if (pieces[i][j] != null && pieces[i][j].type == Piece.Type.PAWN)
                    pieces[i][j].justMoved = false;
            }
        }
    }

}
