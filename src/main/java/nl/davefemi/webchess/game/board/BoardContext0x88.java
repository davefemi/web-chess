package nl.davefemi.webchess.game.board;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.IdGenerator;
import nl.davefemi.webchess.game.actions.Move;

import java.util.ArrayList;
import java.util.List;

public final class BoardContext0x88 {
    private final IdGenerator pieceIdGenerator;
    private Board0x88 board;
    private PieceColor playerToMove;
    private List<Piece> capturedPieces;
    private List<Integer> originalRooks;

    public BoardContext0x88() {
        this.pieceIdGenerator = new IdGenerator();
        board = new Board0x88(pieceIdGenerator);
        this.capturedPieces = new ArrayList<>();
        this.originalRooks = new ArrayList<>(fetchOriginalRooksFromBoard());
    }

    public PieceColor getPlayerToMove(){
        return playerToMove;
    }

    public void setPlayerToMove(PieceColor playerToMove) {
        this.playerToMove = playerToMove;
    }

    public Board0x88 getCopyOfBoard() {
        return new Board0x88(board);
    }

    private List<Integer> fetchOriginalRooksFromBoard() {
        List<Integer> originalRooks = new ArrayList<>();
        for (Piece p: board.getPieces()){
            if (p != null && p.getType() == PieceType.ROOK)
                originalRooks.add(p.getId());
        }
        return originalRooks;
    }

    public BoardContext0x88 applyMove(Move move) throws BoardException {
        board = new Board0x88(board);
        Piece p = board.applyValidatedMove(move);
        if (p != null)
            capturedPieces.add(p);
        return this;
    }
}
