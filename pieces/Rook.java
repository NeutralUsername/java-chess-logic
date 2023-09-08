package chess.pieces;

public class Rook extends Piece {
    private boolean hasMoved;

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
}
