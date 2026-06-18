package nl.davefemi.chess.play.rule;

import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.play.model.board.Board;
import nl.davefemi.chess.play.model.board.Piece;
import nl.davefemi.chess.play.model.board.PieceType;
import nl.davefemi.chess.play.model.board.Square;
import nl.davefemi.chess.play.model.game.Color;

import java.util.*;

import static nl.davefemi.chess.play.model.board.PieceType.*;

public final class AttackDetector {
    private static final int[] HORIZONTAL_OFFSET = {-1,1};
    private static final int[] VERTICAL_OFFSET = {-16,16};
    private static final int[] DIAGONAL_OFFSET = {-17,-15,15,17};
    private static final int[] KNIGHT_OFFSET = {-33,-31,-18,-14,14,18,31,33};
    private static final Map<Color, int[]> PAWN_OFFSET = Map.of(
                    Color.WHITE, new int[]{15,17},
                    Color.BLACK, new int[]{-17,-15}
            );

    private AttackDetector(){
        throw new AssertionError("This class cannot be instantiated");
    }

    static boolean detectAttack(Board board, Square defendingPosition, Color defendingColor)
            throws BoardException {
        return !findAttackingSquares(board, defendingPosition, defendingColor).isEmpty();
    }

    static List<Square> findAttackingSquares(Board board, Square defendingPosition, Color defendingColor)
            throws BoardException {
        List<Square> attacks = new ArrayList<>();
        attacks.addAll(detectSlidingAttack(board, defendingPosition, defendingColor));
        attacks.addAll(detectSteppingAttack(board, defendingPosition, defendingColor));
        return attacks;
    }

    private static List<Square> detectSlidingAttack(Board board, Square defendingPosition,
                                                    Color defendingColor) throws BoardException {
        List<Square> enemyPositions = new ArrayList<>();
        for (int[] offset : Arrays.asList(HORIZONTAL_OFFSET, VERTICAL_OFFSET)) {
            enemyPositions.addAll(slideByOffsetAndType(board, defendingPosition, defendingColor, offset, List.of(QUEEN, ROOK)));
        }
        enemyPositions.addAll(slideByOffsetAndType(board, defendingPosition, defendingColor, DIAGONAL_OFFSET,
                List.of(QUEEN, BISHOP)));
        return enemyPositions;
    }

    private static List<Square> slideByOffsetAndType
            (Board board, Square defendingPosition, Color defendingColor, int[] offset, List<PieceType> types)
            throws BoardException {
        List<Square> enemyPositions = new ArrayList<>();
        if (!types.isEmpty()) {
            for (int o : offset) {
                Optional<Piece> piece = slider(board, defendingPosition, o);
                if (piece.isPresent()) {
                    Piece p = piece.get();
                    for (PieceType t: types) {
                        if (p.type() == t && !(p.color() == defendingColor))
                            enemyPositions.add(board.getPositionById(p.id()));
                    }
                }
            }
        }
        return enemyPositions;
    }

    private static Optional<Piece> slider(Board board, Square position, int offset) throws BoardException {
        while (Board.isLegalPosition(position.value()+offset)) {
            position = new Square(position.value() + offset);
            if (board.isPositionOccupied(position)) {
                return Optional.of(board.getPieceAt(position));
            }
        }
        return Optional.empty();
    }

    private static List<Square> detectSteppingAttack
            (Board board, Square defendingPosition, Color defendingColor)
            throws BoardException {
        List<Square> attacks = new ArrayList<>();
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, PAWN_OFFSET.get(defendingColor), PAWN));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, HORIZONTAL_OFFSET, KING));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, VERTICAL_OFFSET, KING));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, DIAGONAL_OFFSET, KING));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, KNIGHT_OFFSET, KNIGHT));
        return attacks;
    }

    private static List<Square> detectSteppingOpponent
            (Board board, Square defendingPosition, Color defendingColor, int[] offset, PieceType pieceType)
            throws BoardException {
        List<Square> enemyPositions = new ArrayList<>();
        for (int o : offset) {
            if (Board.isLegalPosition(defendingPosition.value() + o)) {
                Square newPosition = new Square(defendingPosition.value() + o);
                if (board.isPositionOccupied(newPosition)) {
                    Piece p = board.getPieceAt(newPosition);
                    if (p.type() == pieceType && !(p.color() == defendingColor))
                        enemyPositions.add(newPosition);
                }
            }
        }
        return enemyPositions;
    }
}