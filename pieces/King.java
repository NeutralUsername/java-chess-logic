package chess.pieces;

public class King extends Piece {
    private boolean hasMoved;

    public King(boolean isWhite) {
        super(isWhite);
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
