package nl.davefemi.webchess.game.board;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.actions.record.MoveRecordBuilder;

import java.util.ArrayList;
import java.util.List;

public final class BoardContext {
    private final Board board;
    private PieceColor colorToMove;
    private MoveRecord lastMove;
    private final List<Piece> capturedPieces;
    private final List<Integer> originalRooks;

    public BoardContext(PieceColor colorToMove) {
        this.board = new Board();
        this.colorToMove = colorToMove;
        this.capturedPieces = new ArrayList<>();
        this.originalRooks = new ArrayList<>(fetchOriginalRooksFromBoard());
    }

    public BoardContext(PieceColor colorToMove, Board board, MoveRecord lastMove, List<Piece> capturedPieces, List<Integer> originalRooks){
        this.colorToMove = colorToMove;
        this.lastMove = lastMove;
        this.board = board;
        this.capturedPieces = capturedPieces;
        this.originalRooks = originalRooks;
    }

    public PieceColor getColorToMove(){
        return colorToMove;
    }

    public void setColorToMove(PieceColor colorToMove) {
        this.colorToMove = colorToMove;
    }

    public Board getCopyOfBoard() {
        return new Board(board);
    }

    private List<Integer> fetchOriginalRooksFromBoard() {
        List<Integer> originalRooks = new ArrayList<>();
        for (Piece piece : board.getPieces()){
            if (piece != null && piece.getType() == PieceType.ROOK)
                originalRooks.add(piece.getId());
        }
        return originalRooks;
    }

    public BoardContext applyMove(Move move) throws BoardException {
        Board board = new Board(this.board);
        synchronized (this) {
            Piece p = board.applyValidatedMove(move);
            if (p != null)
                capturedPieces.add(p);
            lastMove = MoveRecordBuilder.getMoveRecord(board, move, colorToMove, p);
        }
        return new BoardContext(this.colorToMove, board, lastMove, new ArrayList<>(capturedPieces), new ArrayList<>(originalRooks));
    }

    public List<Integer> getOriginalRooks(){
        return new ArrayList<>(originalRooks);
    }

    public List<Piece> getCapturedPieces() {
        return new ArrayList<>(capturedPieces);
    }

    public MoveRecord getLastMove(){
        return lastMove;
    }
}
