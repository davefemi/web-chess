package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.record.MoveRecord;
import nl.davefemi.webchess.game.utility.CastlingMoveGenerator;
import nl.davefemi.webchess.game.utility.MoveGenerator;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import java.util.*;

public final class RuleEngine {

    private RuleEngine(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isMoveAllowed(Game game, PieceColor pieceColor, Move move) throws BoardException, MoveException, GameException {
        if (!(pieceColor == game.getPlayerTurn()))
            throw new MoveException("It is not " + pieceColor +"'s turn yet");
        if (move instanceof CastlingMove m)
            if (!(MoveEvaluator.isCastlingMoveLegal(game.getCopyOfBoard(), game.getOriginalRooks(), game.getMoveHistory(), m)))
                throw new MoveException("Castling is not allowed");
        if (move instanceof SingleMove m){
            Piece p = game.getCopyOfBoard().getPieceAt(m.from());
            if (p != null && p.getType() == PieceType.PAWN)
            if ((p.getColor() == PieceColor.WHITE && m.from().rank() == 7 ||
                    p.getColor() == PieceColor.BLACK && m.from().rank() == 2)) {
                throw new MoveException("Single move not allowed here, use promotion move");
            }
        }
        if (move instanceof PromotionMove m) {
            if (!PromotionMoveEvaluator.isPromotionMoveLegal(game.getCopyOfBoard(), m))
                throw new MoveException("Promotion not allowed");
            move = m.move();
        }
        return getAllLegalMovesByPieceColor(game, pieceColor).contains(move);
    }

    public static List<Move> getAllLegalMovesByPieceColor(Game game, PieceColor color) throws BoardException {
        List<Move> moves = new ArrayList<>();
        Board board = game.getCopyOfBoard();
        moves.addAll(MoveGenerator.generateMoves(board, game.getLastMove().getMove(), color, true));
        moves.addAll(CastlingMoveGenerator.generateCastlingMoves(board, color, true));
        return moves;
    }

    public static boolean isCheck(Game game, PieceColor color) throws BoardException {
        return MoveEvaluator.isKingCheck(game.getCopyOfBoard(),
                BoardScanner.getCurrentSinglePiecePosition(game.getCopyOfBoard(), PieceType.KING, color),
                PieceColor.getOpponent(color));
    }

    public static boolean isCheckMate(Game game, PieceColor color) throws BoardException {
        return isCheck(game, color) && getAllLegalMovesByPieceColor(game, color).isEmpty();
    }

}