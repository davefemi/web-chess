package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.board.*;
import java.util.*;
import static nl.davefemi.webchess.game.board.PieceType.*;

public class AttackDetector {
    private static final int[] HORIZONTAL_OFFSET = {-1,1};
    private static final int[] VERTICAL_OFFSET = {-16,16};
    private static final int[] DIAGONAL_OFFSET = {-17,-15,15,17};
    private static final int[] KNIGHT_OFFSET = {-33,-31,-18,-14,14,18,31,33};
    private static final Map<PieceColor, int[]> PAWN_OFFSET = Map.of(
                    PieceColor.WHITE, new int[]{15,17},
                    PieceColor.BLACK, new int[]{-17,-15}
            );

    public static boolean detectAttack(Board board, Square defendingPosition, PieceColor defendingColor) throws BoardException {
        return !findAttacks(board, defendingPosition, defendingColor).isEmpty();
    }

    public static List<Square> findAttacks(Board board, Square defendingPosition, PieceColor defendingColor) throws BoardException {
        List<Square> attacks = new ArrayList<>();
        attacks.addAll(detectSlidingAttack(board, defendingPosition, defendingColor));
        attacks.addAll(detectSteppingAttack(board, defendingPosition, defendingColor));
        return attacks;
    }

    public static boolean containsPinnedPiece(Board board, Square attackingPosition, PieceColor enemyColor) throws BoardException {
        for (int[] offset : Arrays.asList(HORIZONTAL_OFFSET, VERTICAL_OFFSET)) {
            if (pinnedPieceDetection(board, attackingPosition, enemyColor, offset, List.of(QUEEN,ROOK)))
                return true;
        }
        return pinnedPieceDetection(board, attackingPosition, enemyColor, DIAGONAL_OFFSET, List.of(QUEEN, BISHOP));
    }

    private static List<Square> detectSlidingAttack(Board board, Square defendingPosition, PieceColor defendingColor) throws BoardException {
        List<Square> enemyPositions = new ArrayList<>();
        for (int[] offset : Arrays.asList(HORIZONTAL_OFFSET, VERTICAL_OFFSET)) {
            enemyPositions.addAll(slideByOffsetAndType(board, defendingPosition, defendingColor, offset, List.of(QUEEN, ROOK)));
        }
        enemyPositions.addAll(slideByOffsetAndType(board, defendingPosition, defendingColor, DIAGONAL_OFFSET, List.of(QUEEN, BISHOP)));
        return enemyPositions;
    }

    private static List<Square> slideByOffsetAndType(Board board, Square defendingPosition, PieceColor defendingColor,
                                                     int[] offset, List<PieceType> types) throws BoardException {
        List<Square> enemyPositions = new ArrayList<>();
        if (!types.isEmpty()) {
            for (int o : offset) {
                Optional<Piece> piece = slider(board, defendingPosition, o);
                if (piece.isPresent()) {
                    Piece p = piece.get();
                    for (PieceType t: types) {
                        if (p.getType() == t && !(p.getColor() == defendingColor))
                            enemyPositions.add(board.getPositionById(p.getId()));
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

    private static boolean pinnedPieceDetection(Board board, Square attackingPosition, PieceColor enemyColor, int[] offset, List<PieceType> types) throws BoardException {
        if (!types.isEmpty()) {
            for (int o : offset) {
                Square currentPosition = new Square(attackingPosition.value());
                Optional<Piece> piece = slider(board, currentPosition, o);
                if (piece.isEmpty() || piece.get().getType() != KING || piece.get().getColor() != enemyColor) {
                    continue;
                }
                Optional<Piece> attackingPiece = slider(board, attackingPosition, o * -1);
                if (attackingPiece.isEmpty()) {
                    continue;
                }
                Piece p = attackingPiece.get();
                for (PieceType t : types) {
                    if (p.getColor() != enemyColor && p.getType() == t)
                        return true;
                }
            }
        }
        return false;
    }

    private static List<Square> detectSteppingAttack(Board board, Square defendingPosition, PieceColor defendingColor) throws BoardException {
        List<Square> attacks = new ArrayList<>();
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, PAWN_OFFSET.get(defendingColor), PAWN));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, HORIZONTAL_OFFSET, KING));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, VERTICAL_OFFSET, KING));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, DIAGONAL_OFFSET, KING));
        attacks.addAll(detectSteppingOpponent(board, defendingPosition, defendingColor, KNIGHT_OFFSET, KNIGHT));
        return attacks;
    }

    private static List<Square> detectSteppingOpponent(Board board, Square defendingPosition, PieceColor defendingColor,
                                                  int[] offset, PieceType pieceType) throws BoardException {
        List<Square> enemyPositions = new ArrayList<>();
        for (int o : offset) {
            if (Board.isLegalPosition(defendingPosition.value() + o)) {
                Square newPosition = new Square(defendingPosition.value() + o);
                if (board.isPositionOccupied(newPosition)) {
                    Piece p = board.getPieceAt(newPosition);
                    if (p.getType() == pieceType && !(p.getColor() == defendingColor))
                        enemyPositions.add(newPosition);
                }
            }
        }
        return enemyPositions;
    }
}
