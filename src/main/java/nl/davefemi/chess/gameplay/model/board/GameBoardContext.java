package nl.davefemi.chess.gameplay.model.board;

import lombok.Getter;
import lombok.Setter;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.action.move.Move;
import nl.davefemi.chess.gameplay.model.action.record.MoveRecord;
import nl.davefemi.chess.gameplay.model.action.record.MoveRecordBuilder;

import java.util.ArrayList;
import java.util.List;

public final class GameBoardContext {
    private final Board board;
    @Getter @Setter
    private Color sideToMove;
    @Getter
    private MoveRecord lastMove;
    private final List<Piece> capturedPieces;
    private final List<Integer> originalRooks;

    public GameBoardContext(Color sideToMove) {
        this.board = new Board();
        this.sideToMove = sideToMove;
        this.capturedPieces = new ArrayList<>();
        this.originalRooks = new ArrayList<>(fetchOriginalRooksFromBoard());
    }

    public GameBoardContext(Color sideToMove, Board board, MoveRecord lastMove, List<Piece> capturedPieces, List<Integer> originalRooks){
        this.sideToMove = sideToMove;
        this.lastMove = lastMove;
        this.board = board;
        this.capturedPieces = capturedPieces;
        this.originalRooks = originalRooks;
    }

    public Board getCopyOfBoard() {
        return new Board(board);
    }

    private List<Integer> fetchOriginalRooksFromBoard() {
        List<Integer> originalRooks = new ArrayList<>();
        for (Piece piece : board.getPieces()){
            if (piece != null && piece.type() == PieceType.ROOK) {
                originalRooks.add(piece.id());
            }
        }
        return originalRooks;
    }

    public synchronized GameBoardContext applyValidatedMove(Move move) throws BoardException {
        Board board = new Board(this.board);
        Piece p = board.applyValidatedMove(move);
        MoveRecord lastMove = MoveRecordBuilder.getMoveRecord(board, move, sideToMove, p);
        GameBoardContext boardContext = new GameBoardContext(this.sideToMove, board, lastMove, new ArrayList<>(capturedPieces),
                new ArrayList<>(originalRooks));
        if (p != null) {
            boardContext.capturedPieces.add(p);
        }
        return boardContext;
    }

    public List<Integer> getOriginalRooks(){
        return new ArrayList<>(originalRooks);
    }

    public List<Piece> getCapturedPieces() {
        return new ArrayList<>(capturedPieces);
    }

}