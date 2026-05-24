package nl.davefemi.webchess.game.board;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.record.MoveRecord;
import nl.davefemi.webchess.game.record.MoveRecordBuilder;
import java.util.ArrayList;
import java.util.List;

public final class BoardContext {
    private final Board board;
    private PieceColor playerToMove;
    private MoveRecord lastMove;
    private final List<Piece> capturedPieces;
    private final List<Integer> originalRooks;

    public BoardContext(PieceColor playerToMove) {
        this.board = new Board();
        this.playerToMove = playerToMove;
        this.capturedPieces = new ArrayList<>();
        this.originalRooks = new ArrayList<>(fetchOriginalRooksFromBoard());
    }

    public BoardContext(PieceColor playerToMove, Board board, MoveRecord lastMove, List<Piece> capturedPieces, List<Integer> originalRooks){
        this.playerToMove = playerToMove;
        this.lastMove = lastMove;
        this.board = board;
        this.capturedPieces = capturedPieces;
        this.originalRooks = originalRooks;
    }

    public PieceColor getPlayerToMove(){
        return playerToMove;
    }

    public void setPlayerToMove(PieceColor playerToMove) {
        this.playerToMove = playerToMove;
    }

    public Board getCopyOfBoard() {
        return new Board(board);
    }

    private List<Integer> fetchOriginalRooksFromBoard() {
        List<Integer> originalRooks = new ArrayList<>();
        for (Position p: board.getBoardPositions()){
            Piece piece = board.getPieceAt(p);
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
            lastMove = MoveRecordBuilder.getMoveRecord(board, move, playerToMove, p);
        }
        return new BoardContext(this.playerToMove, board, lastMove, new ArrayList<>(capturedPieces), new ArrayList<>(originalRooks));
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
