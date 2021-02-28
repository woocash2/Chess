package com.github.woocash2.Chess.model;

import com.github.woocash2.Chess.test.BoardTemplate;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public class Board {
    public enum Piece {
        QUEEN,
        BISHOP,
        KNIGHT,
        KING, KINGM,
        ROOK, ROOKM,
        PAWN, PAWNJ, PAWNM,
        EMPTY
    }


    public static HashSet<Piece> whites = new HashSet<>();
    public static HashSet<Piece> blacks = new HashSet<>();

    public enum Team {
        WHITE, BLACK, EMPTY
    }

    public Pair<Integer, Integer> whiteKingPos, blackKingPos;
    public int whiteQueens = 0;
    public int blackQueens = 0;

    public Piece[][] pieces = new Piece[8][8];
    public Team[][] teams = new Team[8][8];
    public ArrayList<Move> moves = new ArrayList<>();

    public Board(char[][] field) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = PieceFactory.typeByChar.get(field[i][j]).getKey();
                teams[i][j] = PieceFactory.typeByChar.get(field[i][j]).getValue();
                classifyPiece(i, j);
            }
        }
    }

    public Board() {
        this(BoardTemplate.standard);
    }

    public Board(Board brd) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = brd.pieces[i][j];
                teams[i][j] = brd.teams[i][j];
                classifyPiece(i, j);
            }
        }
    }

    public void classifyPiece(int i, int j) {
        if (pieces[i][j] == Piece.QUEEN && teams[i][j] == Team.WHITE)
            whiteQueens++;
        if (pieces[i][j] == Piece.QUEEN && teams[i][j] == Team.BLACK)
            blackQueens++;
        if ((pieces[i][j] == Piece.KING || pieces[i][j] == Piece.KINGM) && teams[i][j] == Team.WHITE)
            whiteKingPos = new Pair<>(i, j);
        if ((pieces[i][j] == Piece.KING || pieces[i][j] == Piece.KINGM) && teams[i][j] == Team.BLACK)
            blackKingPos = new Pair<>(i, j);
    }

    public boolean isEmpty(int x, int y) {
        return pieces[x][y] == Piece.EMPTY;
    }
    public Piece get(int x, int y) { return pieces[x][y]; }
    public boolean inBoardRange(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public Move move(Move mov) {
        int x = mov.fromX, y = mov.fromY, a = mov.toX, b = mov.toY;
        if (pieces[a][b] == Piece.QUEEN && teams[a][b] == Team.WHITE)
            whiteQueens--;
        if (pieces[a][b] == Piece.QUEEN && teams[a][b] == Team.BLACK)
            blackQueens--;
        if (mov.post == Piece.QUEEN && mov.pre != Piece.QUEEN) {
            if (mov.team == Team.WHITE)
                whiteQueens++;
            if (mov.team == Team.BLACK)
                blackQueens++;
        }

        Move additional = null; // for animation purposes
        if (pieces[x][y] == Piece.KING || pieces[x][y] == Piece.KINGM) {
            if (teams[x][y] == Team.WHITE)
                whiteKingPos = new Pair<>(a, b);
            if (teams[x][y] == Team.BLACK)
                blackKingPos = new Pair<>(a, b);
        }
        if (pieces[x][y] == Piece.KING) {
            if (b == y - 2) { // castling left
                additional = new Move(x, 0, x, y - 1, Piece.ROOK, Piece.ROOKM, teams[x][y], null);
                pieces[x][0] = Piece.EMPTY;
                teams[x][0] = Team.EMPTY;
                pieces[x][y - 1] = Piece.ROOKM;
                teams[x][y - 1] = teams[x][y];
            }
            if (b == y + 2) { // castling right
                additional = new Move(x, 7, x, y + 1, Piece.ROOK, Piece.ROOKM, teams[x][y], null);
                pieces[x][7] = Piece.EMPTY;
                teams[x][7] = Team.EMPTY;
                pieces[x][y + 1] = Piece.ROOKM;
                teams[x][y + 1] = teams[x][y];
            }
        }
        if (pieces[x][y] == Piece.PAWNM) {
            if (y != b && isEmpty(a, b)) { // en passant
                additional = new Move(x, b, x, b, Piece.PAWNJ, Piece.PAWNJ, opponnent(teams[x][y]), null);
                pieces[x][b] = Piece.EMPTY;
                teams[x][b] = Team.EMPTY;
            }
        }

        pieces[x][y] = Piece.EMPTY;
        teams[x][y] = Team.EMPTY;
        pieces[a][b] = mov.post;
        teams[a][b] = mov.team;
        return additional;
    }

    public void unMove(Move mov) {
        int x = mov.fromX, y = mov.fromY, a = mov.toX, b = mov.toY;
        if (mov.taken == Piece.QUEEN && mov.team == Team.WHITE)
            blackQueens++;
        if (mov.taken == Piece.QUEEN && mov.team == Team.BLACK)
            whiteQueens++;
        if (mov.post == Piece.QUEEN && mov.pre != Piece.QUEEN) {
            if (mov.team == Team.WHITE)
                whiteQueens--;
            if (mov.team == Team.BLACK)
                blackQueens--;
        }

        if (mov.pre == Piece.KING || mov.pre == Piece.KINGM) {
            if (mov.team == Team.WHITE)
                whiteKingPos = new Pair<>(x, y);
            if (mov.team == Team.BLACK)
                blackKingPos = new Pair<>(x, y);
        }
        if (mov.pre == Piece.KING && y != b) {
            if (b == y - 2) { // castling left
                pieces[x][y - 1] = Piece.EMPTY;
                teams[x][y - 1] = Team.EMPTY;
                pieces[x][0] = Piece.ROOK;
                teams[x][0] = mov.team;
            }
            if (b == y + 2) { // castling right
                pieces[x][y + 1] = Piece.EMPTY;
                teams[x][y + 1] = Team.EMPTY;
                pieces[x][7] = Piece.ROOK;
                teams[x][7] = mov.team;
            }
        }
        if (mov.pre == Piece.PAWNM && y != b && mov.taken == Piece.EMPTY) {
            pieces[x][b] = Piece.PAWNJ;
            teams[x][b] = opponnent(mov.team);
        }

        pieces[a][b] = mov.taken;
        teams[a][b] = mov.taken == Piece.EMPTY ? Team.EMPTY : opponnent(mov.team);
        pieces[x][y] = mov.pre;
        teams[x][y] = mov.team;
    }

    public int numOfAttackers(Team team, int x, int y) { // is the field x, y attacked by a piece from (team)^(-1)
        Function<Team, Boolean> isOpponent = t -> t != team;

        ArrayList<Pair<Integer, Integer>> attackers = new ArrayList<>();
        Rook.boardIteration(p -> {
            Team c = teams[p.getKey()][p.getValue()];
            Piece q = pieces[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (q == Piece.QUEEN || q == Piece.ROOK || q == Piece.ROOKM) && attackers.add(p);
        }, this, x, y);
        Bishop.boardIteration(p -> {
            Team c = teams[p.getKey()][p.getValue()];
            Piece q = pieces[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (q == Piece.QUEEN || q == Piece.BISHOP) && attackers.add(p);
        }, this, x, y);
        Knight.boardIteration(p -> {
            Team c = teams[p.getKey()][p.getValue()];
            Piece q = pieces[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && q == Piece.KNIGHT && attackers.add(p);
        }, this, x, y);
        King.boardIteration(p -> {
            Team c = teams[p.getKey()][p.getValue()];
            Piece q = pieces[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (q == Piece.KING || q == Piece.KINGM) && attackers.add(p);
        }, this, x, y);
        Pawn.boardIteration(p -> {
            Team c = teams[p.getKey()][p.getValue()];
            Piece q = pieces[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (q == Piece.PAWN || q == Piece.PAWNM || q == Piece.PAWNJ) && attackers.add(p);
        }, this, x, y, team);

        return attackers.size();
    }

    public boolean isWhiteKingAttacked() {
        return numOfAttackers(Team.WHITE, whiteKingPos.getKey(), whiteKingPos.getValue()) > 0;
    }

    public boolean isBlackKingAttacked() {
        return numOfAttackers(Team.BLACK, blackKingPos.getKey(), blackKingPos.getValue()) > 0;
    }

    public boolean isCheckMated(Team team) {
        if (team == Team.WHITE)
            return isWhiteKingAttacked() && moves.size() == 0;
        else
            return isBlackKingAttacked() && moves.size() == 0;
    }

    public boolean isStaleMate(Team team) {
        if (team == Team.WHITE)
            return !isWhiteKingAttacked() && moves.size() == 0;
        else
            return !isBlackKingAttacked() && moves.size() == 0;
    }

    public void updateMoves(Team team) {
        moves.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (teams[i][j] != team || pieces[i][j] == Piece.EMPTY)
                    continue;

                switch (pieces[i][j]) {
                    case PAWN, PAWNJ, PAWNM -> Pawn.updatePositions(this, i, j);
                    case KING, KINGM -> King.updatePositions(this, i, j);
                    case ROOK, ROOKM -> Rook.updatePositions(this, i, j);
                    case BISHOP -> Bishop.updatePositions(this, i, j);
                    case QUEEN -> Queen.updatePositions(this, i, j);
                    case KNIGHT -> Knight.updatePositions(this, i, j);
                }
                if (pieces[i][j] == Piece.PAWNJ)
                    pieces[i][j] = Piece.PAWNM;
            }
        }
    }

    public Team opponnent(Team t) {
        return t == Team.WHITE ? Team.BLACK : Team.WHITE;
    }


    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(PieceFactory.charByType.get(new Pair<>(pieces[i][j], teams[i][j])));
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.printBoard();
    }
}
