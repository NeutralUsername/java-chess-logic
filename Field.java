package chess;

import chess.pieces.Piece;

public class Field {
    private int row;
    private int column;
    private Piece piece;

    public Field(int row, int column, Piece piece) {
        this.row = row;
        this.column = column;
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece newPiece) {
        piece = newPiece;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
