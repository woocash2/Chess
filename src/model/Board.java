package model;

import javafx.util.Pair;

public class Board {

    char[][] positions = new char[8][8];

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
}
