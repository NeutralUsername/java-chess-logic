package chess;

import java.util.ArrayList;

import chess.pieces.Piece;
import chess.pieces.Pawn;
import chess.pieces.Rook;
import chess.pieces.Knight;
import chess.pieces.Bishop;
import chess.pieces.Queen;
import chess.pieces.King;

public class Chess {

    private Field[][] board;
    private ArrayList<String> moves = new ArrayList<>();

    public Chess() {
        board = new Field[8][8];
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Field(1, i, new Pawn(true));
            board[6][i] = new Field(6, i, new Pawn(false));
        }
        board[0][0] = new Field(0, 0, new Rook(true));
        board[0][1] = new Field(0, 1, new Knight(true));
        board[0][2] = new Field(0, 2, new Bishop(true));
        board[0][3] = new Field(0, 3, new Queen(true));
        board[0][4] = new Field(0, 4, new King(true));
        board[0][5] = new Field(0, 5, new Bishop(true));
        board[0][6] = new Field(0, 6, new Knight(true));

        board[0][7] = new Field(0, 7, new Rook(true));
        board[7][0] = new Field(7, 0, new Rook(false));
        board[7][1] = new Field(7, 1, new Knight(false));
        board[7][2] = new Field(7, 2, new Bishop(false));
        board[7][3] = new Field(7, 3, new Queen(false));
        board[7][4] = new Field(7, 4, new King(false));
        board[7][5] = new Field(7, 5, new Bishop(false));
        board[7][6] = new Field(7, 6, new Knight(false));
        board[7][7] = new Field(7, 7, new Rook(false));

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Field(i, j, null);
            }
        }
    }

    public String getLastMove() {
        if (moves.size() == 0) {
            return "";
        }
        return moves.get(moves.size() - 1);
    }

    public void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getPiece() == null) {
                    System.out.print("  ");
                } else if (board[i][j].getPiece() instanceof Pawn) {
                    System.out.print((board[i][j].getPiece().isWhite() ? "P" : "p") + " ");
                } else if (board[i][j].getPiece() instanceof Rook) {
                    System.out.print((board[i][j].getPiece().isWhite() ? "R" : "r") + " ");
                } else if (board[i][j].getPiece() instanceof Knight) {
                    System.out.print((board[i][j].getPiece().isWhite() ? "N" : "n") + " ");
                } else if (board[i][j].getPiece() instanceof Bishop) {
                    System.out.print((board[i][j].getPiece().isWhite() ? "B" : "b") + " ");
                } else if (board[i][j].getPiece() instanceof Queen) {
                    System.out.print((board[i][j].getPiece().isWhite() ? "Q" : "q") + " ");
                } else if (board[i][j].getPiece() instanceof King) {
                    System.out.print((board[i][j].getPiece().isWhite() ? "K" : "k") + " ");
                }
            }
            System.out.println(i + 1);
        }
        System.out.println("  a b c d e f g h");
    }

    public void move(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (isValidAction(fromRow, fromColumn, toRow, toColumn)) {
            String notation = generateAlgebraicNotation(fromRow, fromColumn, toRow, toColumn);
            Piece piece = board[fromRow][fromColumn].getPiece();
            if (piece instanceof King && Math.abs(fromColumn - toColumn) == 2) {
                if (piece.isWhite()) {
                    if (toColumn == 6) {
                        board[0][7].setPiece(null);
                        board[0][5].setPiece(new Rook(true));
                    } else {
                        board[0][0].setPiece(null);
                        board[0][3].setPiece(new Rook(true));
                    }
                } else {
                    if (toColumn == 6) {
                        board[7][7].setPiece(null);
                        board[7][5].setPiece(new Rook(false));
                    } else {
                        board[7][0].setPiece(null);
                        board[7][3].setPiece(new Rook(false));
                    }
                }
            } else if (piece instanceof Pawn && fromColumn != toColumn && board[toRow][toColumn].getPiece() == null) {
                board[toRow + (piece.isWhite() ? -1 : 1)][toColumn].setPiece(null);
            }
            board[toRow][toColumn].setPiece(piece);
            board[fromRow][fromColumn].setPiece(null);
            moves.add(notation);
            if (piece instanceof King) {
                ((King) piece).setHasMoved(true);
            } else if (piece instanceof Rook) {
                ((Rook) piece).setHasMoved(true);
            } else if (piece instanceof Pawn && (toRow == 0 || toRow == 7)) {
                board[toRow][toColumn].setPiece(new Queen(piece.isWhite()));
            }
        }
    }

    public String generateAlgebraicNotation(int fromRow, int fromColumn, int toRow, int toColumn) {
        String notation = "";
        Piece movingPiece = board[fromRow][fromColumn].getPiece();

        if (movingPiece instanceof King && Math.abs(fromColumn - toColumn) == 2) {
            if (toColumn == 6) {
                notation = "O-O";
            } else {
                notation = "O-O-O";
            }
            return notation;
        } else if (movingPiece instanceof Pawn) {
            if (fromColumn != toColumn && board[toRow][toColumn].getPiece() == null) {
                notation = getColumnLetter(fromColumn) + "x" + getColumnLetter(toColumn) + (toRow + 1);
            } else {
                notation = getColumnLetter(toColumn) + (toRow + 1);
            }
            if (toRow == 0 || toRow == 7) {
                notation += "=Q";
            }
            return notation;
        }

        if (board[toRow][toColumn].getPiece() != null) {
            notation += movingPiece instanceof Rook ? "R"
                    : movingPiece instanceof Knight ? "N"
                            : movingPiece instanceof Bishop ? "B" : movingPiece instanceof Queen ? "Q" : "K";
            if (fromColumn == toColumn) {
                notation += getColumnLetter(fromColumn);
            } else {
                notation += getColumnLetter(fromColumn) + "x";
            }
        } else {
            notation += movingPiece instanceof Rook ? "R"
                    : movingPiece instanceof Knight ? "N"
                            : movingPiece instanceof Bishop ? "B" : movingPiece instanceof Queen ? "Q" : "K";
            if (fromColumn == toColumn) {
                notation += getColumnLetter(fromColumn);
            } else {
                notation += getColumnLetter(fromColumn) + "x";
            }
        }
        notation += getColumnLetter(toColumn) + (toRow + 1);
        return notation;
    }

    public boolean isWhiteTurn() {
        return moves.size() % 2 == 0;
    }

    public boolean isUnderAttack(int row, int column) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isThreatening(i, j, row, column)) {
                    return true;
                }

            }
        }
        return false;
    }

    public boolean isThreatening(int fromRow, int fromColumn, int toRow, int toColumn) {
        Piece attackedPiece = board[toRow][toColumn].getPiece();
        Piece attackingPiece = board[fromRow][fromColumn].getPiece();
        if (attackedPiece == null || attackingPiece == null) {
            return false;
        }
        if (attackedPiece.isWhite() == attackingPiece.isWhite()) {
            return false;
        }
        if (attackingPiece instanceof Pawn) {
            return isValidPawnMovement(fromRow, fromColumn, toRow, toColumn);
        } else if (attackingPiece instanceof Rook) {
            return isValidRookMovement(fromRow, fromColumn, toRow, toColumn);
        } else if (attackingPiece instanceof Knight) {
            return isValidKnightMovement(fromRow, fromColumn, toRow, toColumn);
        } else if (attackingPiece instanceof Bishop) {
            return isValidBishopMovement(fromRow, fromColumn, toRow, toColumn);
        } else if (attackingPiece instanceof Queen) {
            return isValidQueenMovement(fromRow, fromColumn, toRow, toColumn);
        } else if (attackingPiece instanceof King) {
            return isValidKingMovement(fromRow, fromColumn, toRow, toColumn);
        }
        return false;
    }

    public boolean isValidAction(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (fromRow < 0 || fromRow > 7 || fromColumn < 0 || fromColumn > 7 || toRow < 0 || toRow > 7 || toColumn < 0
                || toColumn > 7 || (fromRow == toRow && fromColumn == toColumn)) {
            return false;
        }
        Piece piece = board[fromRow][fromColumn].getPiece();
        if (piece == null) {
            return false;
        }
        if (piece.isWhite() != isWhiteTurn()) {
            return false;
        }
        if (piece instanceof Pawn) {
            if (!isValidPawnMovement(fromRow, fromColumn, toRow, toColumn)  ) {
                return false;
            }
        } else if (piece instanceof Rook) {
            if (!isValidRookMovement(fromRow, fromColumn, toRow, toColumn)) {
                return false;
            }
        } else if (piece instanceof Knight) {
            if (!isValidKnightMovement(fromRow, fromColumn, toRow, toColumn)) {
                return false;
            }
        } else if (piece instanceof Bishop) {
            if (!isValidBishopMovement(fromRow, fromColumn, toRow, toColumn)) {
                return false;
            }
        } else if (piece instanceof Queen) {
            if (!isValidQueenMovement(fromRow, fromColumn, toRow, toColumn)) {
                return false;
            }
        } else if (piece instanceof King) {
            if (!isValidKingMovement(fromRow, fromColumn, toRow, toColumn)
                    && !isValidCastling(fromRow, fromColumn, toRow, toColumn)) {
                return false;
            }
        }

        Piece toPiece = board[toRow][toColumn].getPiece();
        board[toRow][toColumn].setPiece(piece);
        board[fromRow][fromColumn].setPiece(null);

        Field kingField = getKingField(piece.isWhite());
        boolean kingUnderAttack = isUnderAttack(kingField.getRow(), kingField.getColumn());

        board[fromRow][fromColumn].setPiece(piece);
        board[toRow][toColumn].setPiece(toPiece);

        return !kingUnderAttack;
    }

    private Field getKingField(boolean forWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getPiece() instanceof King && board[i][j].getPiece().isWhite() == forWhite) {
                    return board[i][j];
                }
            }
        }
        return null;
    }

    private String getColumnLetter(int column) {
        switch (column) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
        }
        return "h";
    }

    private boolean isValidPawnMovement(int fromRow, int fromColumn, int toRow, int toColumn) {
        Piece movingPiece = board[fromRow][fromColumn].getPiece();
        Piece targetLocationPiece = board[toRow][toColumn].getPiece();

        if (movingPiece.isWhite()) {
            if (fromRow == 1 && fromColumn == toColumn && toRow == 3 && board[2][toColumn].getPiece() == null
                    && board[3][toColumn].getPiece() == null) {
                return true;
            }
            if (fromRow + 1 == toRow && fromColumn == toColumn && targetLocationPiece == null) {
                return true;
            }
            if (fromRow + 1 == toRow && (fromColumn == toColumn + 1 || fromColumn == toColumn - 1)
                    && ((targetLocationPiece != null && !targetLocationPiece.isWhite())
                            || (moves.size() > 0
                                    && moves.get(moves.size() - 1).equals(getColumnLetter(toColumn) + 5)))) {
                return true;
            }
        } else {
            if (fromRow == 6 && fromColumn == toColumn && toRow == 4 && board[5][toColumn].getPiece() == null
                    && board[4][toColumn].getPiece() == null) {
                return true;
            }
            if (fromRow - 1 == toRow && fromColumn == toColumn && targetLocationPiece == null) {
                return true;
            }
            if (fromRow - 1 == toRow && (fromColumn == toColumn + 1 || fromColumn == toColumn - 1)
                    && ((targetLocationPiece != null && targetLocationPiece.isWhite())
                            || (moves.size() > 0
                                    && moves.get(moves.size() - 1).equals(getColumnLetter(toColumn) + (4))))) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidRookMovement(int fromRow, int fromColumn, int toRow, int toColumn) {
        Piece movingPiece = board[fromRow][fromColumn].getPiece();
        Piece targetLocationPiece = board[toRow][toColumn].getPiece();

        if (fromRow == toRow) {
            if (fromColumn < toColumn) {
                for (int i = fromColumn + 1; i < toColumn; i++) {
                    if (board[fromRow][i].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            } else {
                for (int i = fromColumn - 1; i > toColumn; i--) {
                    if (board[fromRow][i].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            }
        }
        if (fromColumn == toColumn) {
            if (fromRow < toRow) {
                for (int i = fromRow + 1; i < toRow; i++) {
                    if (board[i][fromColumn].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            } else {
                for (int i = fromRow - 1; i > toRow; i--) {
                    if (board[i][fromColumn].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            }
        }
        return false;
    }

    private boolean isValidKnightMovement(int fromRow, int fromColumn, int toRow, int toColumn) {
        Piece movingPiece = board[fromRow][fromColumn].getPiece();
        Piece targetLocationPiece = board[toRow][toColumn].getPiece();

        if (fromRow == toRow + 2 && fromColumn == toColumn + 1
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }
        if (fromRow == toRow + 2 && fromColumn == toColumn - 1
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }
        if (fromRow == toRow - 2 && fromColumn == toColumn + 1
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }
        if (fromRow == toRow - 2 && fromColumn == toColumn - 1
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }

        if (fromRow == toRow + 1 && fromColumn == toColumn + 2
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }
        if (fromRow == toRow + 1 && fromColumn == toColumn - 2
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }
        if (fromRow == toRow - 1 && fromColumn == toColumn + 2
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }
        if (fromRow == toRow - 1 && fromColumn == toColumn - 2
                && (targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite())) {
            return true;
        }
        return false;
    }

    private boolean isValidBishopMovement(int fromRow, int fromColumn, int toRow, int toColumn) {
        Piece movingPiece = board[fromRow][fromColumn].getPiece();
        Piece targetLocationPiece = board[toRow][toColumn].getPiece();

        for (int i = 0; i < 8; i++) {
            if (fromRow + i == toRow && fromColumn + i == toColumn) {
                for (int j = 1; j < i; j++) {
                    if (board[fromRow + j][fromColumn + j].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            }
            if (fromRow + i == toRow && fromColumn - i == toColumn) {
                for (int j = 1; j < i; j++) {
                    if (board[fromRow + j][fromColumn - j].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            }
            if (fromRow - i == toRow && fromColumn + i == toColumn) {
                for (int j = 1; j < i; j++) {
                    if (board[fromRow - j][fromColumn + j].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            }
            if (fromRow - i == toRow && fromColumn - i == toColumn) {
                for (int j = 1; j < i; j++) {
                    if (board[fromRow - j][fromColumn - j].getPiece() != null) {
                        return false;
                    }
                }
                return targetLocationPiece == null || targetLocationPiece.isWhite() != movingPiece.isWhite();
            }
        }
        return false;
    }

    private boolean isValidQueenMovement(int fromRow, int fromColumn, int toRow, int toColumn) {
        return isValidRookMovement(fromRow, fromColumn, toRow, toColumn)
                || isValidBishopMovement(fromRow, fromColumn, toRow, toColumn);
    }

    private boolean isValidKingMovement(int fromRow, int fromColumn, int toRow, int toColumn) {
        Piece piece = board[fromRow][fromColumn].getPiece();
        if (fromRow == toRow + 1 && fromColumn == toColumn && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        if (fromRow == toRow + 1 && fromColumn == toColumn + 1 && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        if (fromRow == toRow + 1 && fromColumn == toColumn - 1 && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        if (fromRow == toRow && fromColumn == toColumn + 1 && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        if (fromRow == toRow && fromColumn == toColumn - 1 && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        if (fromRow == toRow - 1 && fromColumn == toColumn && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        if (fromRow == toRow - 1 && fromColumn == toColumn + 1 && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        if (fromRow == toRow - 1 && fromColumn == toColumn - 1 && (board[toRow][toColumn].getPiece() == null
                || board[toRow][toColumn].getPiece().isWhite() != piece.isWhite())) {
            return true;
        }
        return false;
    }

    private boolean isValidCastling(int fromRow, int fromColumn, int toRow, int toColumn) {
        King king = (King) board[fromRow][fromColumn].getPiece();
        if (!king.hasMoved() && !isUnderAttack(fromRow, fromColumn)) {
            if (king.isWhite()) {
                if (fromRow == 0 && fromColumn == 4 && toRow == 0 && toColumn == 6
                        && board[0][5].getPiece() == null
                        && board[0][6].getPiece() == null
                        && board[0][7].getPiece().isWhite()
                        && board[0][7].getPiece() instanceof Rook
                        && !((Rook) board[0][7].getPiece()).hasMoved()
                        && !isUnderAttack(0, 5)
                        && !isUnderAttack(0, 6)
                        && !isUnderAttack(0, 7)) {
                    return true;
                }
                if (fromRow == 0 && fromColumn == 4 && toRow == 0 && toColumn == 2
                        && board[0][1].getPiece() == null
                        && board[0][2].getPiece() == null
                        && board[0][3].getPiece() == null
                        && board[0][0].getPiece().isWhite()
                        && board[0][0].getPiece() instanceof Rook
                        && !((Rook) board[0][0].getPiece()).hasMoved()
                        && !isUnderAttack(0, 0)
                        && !isUnderAttack(0, 2)
                        && !isUnderAttack(0, 3)) {
                    return true;
                }
            } else {
                if (fromRow == 7 && fromColumn == 4 && toRow == 7 && toColumn == 6
                        && board[7][5].getPiece() == null
                        && board[7][6].getPiece() == null
                        && !board[7][7].getPiece().isWhite()
                        && board[7][7].getPiece() instanceof Rook
                        && !((Rook) board[7][7].getPiece()).hasMoved()
                        && !isUnderAttack(7, 5)
                        && !isUnderAttack(7, 6)
                        && !isUnderAttack(7, 7)) {
                    return true;
                }
                if (fromRow == 7 && fromColumn == 4 && toRow == 7 && toColumn == 2
                        && board[7][1].getPiece() == null
                        && board[7][2].getPiece() == null
                        && board[7][3].getPiece() == null
                        && !board[7][0].getPiece().isWhite()
                        && board[7][0].getPiece() instanceof Rook
                        && !((Rook) board[7][0].getPiece()).hasMoved()
                        && !isUnderAttack(7, 0)
                        && !isUnderAttack(7, 2)
                        && !isUnderAttack(7, 3)) {
                    return true;
                }
            }
        }
        return false;
    }
}
