package model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Function;

public class Board {

    char[][] positions = new char[8][8];
    Pair<Integer, Integer> whiteKingPos, blackKingPos;

    public Board() {
        // init empty fields
        for (int i = 2; i < 6; i++)
            for (int j = 0; j < 8; j++)
                positions[i][j] = '-';

        // init white pawns
        for (int j = 0; j < 8; j++)
            positions[6][j] = 'p';

        // init black pawns
        for (int j = 0; j < 8; j++)
            positions[1][j] = 'P';

        // init white figures
        positions[7][0] = positions[7][7] = 'r'; // rook
        positions[7][1] = positions[7][6] = 'n'; // knight
        positions[7][2] = positions[7][5] = 'b'; // bishop
        positions[7][3] = 'q'; // queen
        positions[7][4] = 'k'; // king

        // init black figures
        positions[0][0] = positions[0][7] = 'R'; // rook
        positions[0][1] = positions[0][6] = 'N'; // knight
        positions[0][2] = positions[0][5] = 'B'; // bishop
        positions[0][3] = 'Q'; // queen
        positions[0][4] = 'K'; // king

        whiteKingPos = new Pair<>(7, 4);
        blackKingPos = new Pair<>(0, 4);
    }

    public Board(Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                positions[i][j] = board.positions[i][j];
                if (positions[i][j] == 'k')
                    whiteKingPos = new Pair<>(i, j);
                if (positions[i][j] == 'K')
                    blackKingPos = new Pair<>(i, j);
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(positions[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public boolean isEmpty(int x, int y) {
        return positions[x][y] == '-';
    }
    public char get(int x, int y) { return positions[x][y]; }

    public boolean inBoardRange(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean opponentStaysOn(int x, int y, Piece.team myTeam) {
        if (myTeam == Piece.team.WHITE)
            return Character.isUpperCase(positions[x][y]);
        else
            return Character.isLowerCase(positions[x][y]);
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.printBoard();
    }

    public void move(int x, int y, int a, int b) {
        positions[a][b] = positions[x][y];
        positions[x][y] = '-';
        if (positions[a][b] == 'k')
            whiteKingPos = new Pair<>(a, b);
        if (positions[a][b] == 'K')
            blackKingPos = new Pair<>(a, b);
    }

    public boolean isAttacked(Piece.team team, int x, int y) { // is the field x, y attacked by a piece from (team)^(-1)
        Function<Character, Boolean> isOpponent;
        if (team == Piece.team.WHITE)
            isOpponent = Character::isUpperCase;
        else
            isOpponent = Character::isLowerCase;

        ArrayList<Pair<Integer, Integer>> attackers = new ArrayList<>();
        Rook.boardIteration(p -> null, p -> {
            char c = positions[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'q' || Character.toLowerCase(c) == 'r') && attackers.add(p);
        }, this, x, y);
        if (!attackers.isEmpty()) return true;
        Bishop.boardIteration(p -> null, p -> {
            char c = positions[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'q' || Character.toLowerCase(c) == 'b') && attackers.add(p);
        }, this, x, y);
        if (!attackers.isEmpty()) return true;
        Knight.boardIteration(p -> null, p -> {
            char c = positions[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'n') && attackers.add(p);
        }, this, x, y);
        if (!attackers.isEmpty()) return true;
        King.boardIteration(p -> null, p -> {
            char c = positions[p.getKey()][p.getValue()];
            return isOpponent.apply(c) && (Character.toLowerCase(c) == 'k') && attackers.add(p);
        }, this, x, y);

        if (team == Piece.team.WHITE) {
            if (inBoardRange(x - 1, y - 1) && positions[x - 1][y - 1] == 'P')
                attackers.add(new Pair<>(x - 1, y - 1));
            if (inBoardRange(x - 1, y + 1) && positions[x - 1][y + 1] == 'P')
                attackers.add(new Pair<>(x - 1, y + 1));
        }
        else {
            if (inBoardRange(x + 1, y - 1) && positions[x + 1][y - 1] == 'p')
                attackers.add(new Pair<>(x + 1, y - 1));
            if (inBoardRange(x + 1, y + 1) && positions[x + 1][y + 1] == 'p')
                attackers.add(new Pair<>(x + 1, y + 1));
        }

        return !attackers.isEmpty();
    }

    public boolean isWhiteKingAttacked() {
        return isAttacked(Piece.team.WHITE, whiteKingPos.getKey(), whiteKingPos.getValue());
    }

    public boolean isBlackKingAttacked() {
        return isAttacked(Piece.team.BLACK, blackKingPos.getKey(), blackKingPos.getValue());
    }

    public void set(int i, int j, char c) {
        positions[i][j] = c;
    }
}
