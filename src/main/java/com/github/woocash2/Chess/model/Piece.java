package com.github.woocash2.Chess.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Piece {

    public enum Team {WHITE, BLACK};
    public enum Type {PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING}

    public static HashMap<Type, Consumer<Piece>> positionUpdaters = new HashMap<>();

    static {
        positionUpdaters.put(Type.PAWN, Pawn::updatePositions);
        positionUpdaters.put(Type.KNIGHT, Knight::updatePositions);
        positionUpdaters.put(Type.BISHOP, Bishop::updatePositions);
        positionUpdaters.put(Type.ROOK, Rook::updatePositions);
        positionUpdaters.put(Type.QUEEN, Queen::updatePositions);
        positionUpdaters.put(Type.KING, King::updatePositions);
    }

    public Team team;
    public Type type;

    public char identifier;
    public Board board;
    public int x, y;
    public boolean moved = false;
    public boolean justMoved = false;
    public boolean alive = true;

    public ArrayList<Pair<Integer, Integer>> reachablePositions = new ArrayList<>();
    public ArrayList<Pair<Integer, Integer>> takeablePositions = new ArrayList<>();

    public Piece(int x, int y, Team tm, Type tp, Board brd, char ident) {
        this.x = x;
        this.y = y;
        team = tm;
        type = tp;
        board = brd;
        identifier = ident;
    }

    public Piece(Piece piece, Board brd) {
        x = piece.x;
        y = piece.y;
        team = piece.team;
        type = piece.type;
        identifier = piece.identifier;
        moved = piece.moved;
        justMoved = piece.justMoved;
        alive = piece.alive;
        board = brd;
    }

    public void updatePositions() {
        positionUpdaters.get(type).accept(this);
    }

    /*
    Moves the piece.
    Returns the piece for which additional action on UI needs to be taken.
        If null is returned it means no additional action needs to be performed.
        If Pawn is returned it means it needs to be taken away from board due to en passant or it needs to be promoted.
            Its position will indicate which of these two events took place.
        If Rook is returned it means it needs to change its position due to castling.
     */
    public Piece move(int nx, int ny) {
        justMoved = !moved;
        moved = true;
        Piece addidional = null;

        if (type == Type.KING) {
            if (ny == y - 2) { // castling left
                Piece leftRook = board.pieces[x][0];
                if (leftRook == null)
                    board.printBoard();
                leftRook.move(x, y - 1);
                addidional = leftRook;
            }
            if (ny == y + 2) { // castling left
                Piece rightRook = board.pieces[x][7];
                if (rightRook == null)
                    board.printBoard();
                rightRook.move(x, y + 1);
                addidional = rightRook;
            }
        }

        if (type == Type.PAWN) {
            if (y != ny && board.isEmpty(nx, ny)) { // en passant
                Piece opponentPawn = board.get(x, ny);
                opponentPawn.alive = false;
                addidional = opponentPawn;
                board.set(opponentPawn.x, opponentPawn.y, null);
            }
            // promotion
        }

        board.move(x, y, nx, ny);
        x = nx;
        y = ny;
        return addidional;
    };

    public Team getTeam() {
        return team;
    }

    public char getIdentifier() {
        return identifier;
    }

    public void transform(Type tp) {
        type = tp;
        identifier = PieceFactory.charByType.get(tp);
        identifier = PieceFactory.charByType.get(type);
        identifier = team == Piece.Team.WHITE ? Character.toLowerCase(identifier) : Character.toUpperCase(identifier);
    }

    public void die() {

    };
}
