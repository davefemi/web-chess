package nl.davefemi.webchess.game.board;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.actions.record.MoveRecordBuilder;

import java.util.ArrayList;
import java.util.List;

public final class GameBoardContext {
    private final Board board;
    private PieceColor colorToMove;
    private MoveRecord lastMove;
    private final List<Piece> capturedPieces;
    private final List<Integer> originalRooks;

    public GameBoardContext(PieceColor colorToMove) {
        this.board = new Board();
        this.colorToMove = colorToMove;
        this.capturedPieces = new ArrayList<>();
        this.originalRooks = new ArrayList<>(fetchOriginalRooksFromBoard());
    }

    public GameBoardContext(PieceColor colorToMove, Board board, MoveRecord lastMove, List<Piece> capturedPieces, List<Integer> originalRooks){
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
            if (piece != null && piece.type() == PieceType.ROOK)
                originalRooks.add(piece.id());
        }
        return originalRooks;
    }

    public synchronized GameBoardContext applyValidatedMove(Move move) throws BoardException {
        Board board = new Board(this.board);
        Piece p = board.applyValidatedMove(move);
        MoveRecord lastMove = MoveRecordBuilder.getMoveRecord(board, move, colorToMove, p);
        GameBoardContext boardContext = new GameBoardContext(this.colorToMove, board, lastMove, new ArrayList<>(capturedPieces),
                new ArrayList<>(originalRooks));
        if (p != null)
            boardContext.capturedPieces.add(p);
        return boardContext;
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