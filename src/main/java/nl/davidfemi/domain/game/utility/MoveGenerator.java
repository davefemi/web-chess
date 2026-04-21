package nl.davidfemi.domain.game.utility;

import nl.davidfemi.domain.board.Board;
import nl.davidfemi.domain.board.BoardScanner;
import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.game.rules.MoveEvaluator;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MoveGenerator {

    public static List<Move> generateMoves(Board board, PlayerColor color, boolean hasTurn){
        List<Move> moves = new ArrayList<>();
        PlayerColor enemyColor = color == PlayerColor.WHITE?PlayerColor.BLACK:PlayerColor.WHITE;
        Position king = BoardScanner.getPosition(board, PieceType.KING, color);
        if (hasTurn) {
            if (MoveEvaluator.isKingCheck(board, king, enemyColor)) {
                return getKingMoves(board, king, enemyColor, true);
            }
        }
        moves.addAll(getKingMoves(board, BoardScanner.getPosition(board, PieceType.KING, color), enemyColor, hasTurn));
        moves.addAll(getQueenMoves(board, BoardScanner.getPosition(board, PieceType.QUEEN, color)));
        moves.addAll(getBishopMoves(board, BoardScanner.getPositions(board, PieceType.BISHOP, color)));
        moves.addAll(getRookMoves(board, BoardScanner.getPositions(board, PieceType.ROOK, color)));
        moves.addAll(getKnightMoves(board, BoardScanner.getPositions(board, PieceType.KNIGHT, color)));
        moves.addAll(getPawnMoves(board, BoardScanner.getPositions(board, PieceType.PAWN, color)));
        return moves;
    }

    private static List<Move>  getKingMoves(Board board,Position position, PlayerColor enemyColor, boolean hasTurn){
        List<Move> psuedoMoves = new ArrayList<>();
        List<Move> legalMoves = new ArrayList<>();
        for (int file = Math.max(position.file()-1, 1) ; file <= Math.min(position.file()+1, 8); file ++ ){
            for (int rank = Math.max(position.rank()-1, 1); rank <= Math.min(position.rank()+1,8); rank++){
                Position newPos = new Position(file,rank);
                MoveEvaluator.evaluateOccupancy(board, position, newPos, psuedoMoves);
            }
        }
        if (hasTurn) {
            for (Move m : psuedoMoves) {
                if (!MoveEvaluator.isKingCheck(MoveEvaluator.fictitiousMove(board, m), m.to(), enemyColor))
                    legalMoves.add(m);
            }
        }
        return legalMoves;
    }

    private static List<Move>  getQueenMoves(Board board, Position position){
        List<Move> moves = new ArrayList<>();
        if (position != null) {
            moves.addAll(getBishopMoves(board, Arrays.asList(position)));
            moves.addAll(getRookMoves(board, Arrays.asList(position)));
        }
        return moves;
    }

    private static List<Move>  getBishopMoves(Board board, List<Position> positions){
        List<Move> moves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                int incr = 1;
                for (int file = position.file() - 1; file >= 1; file--) {
                    Position newPos = new Position(file, position.rank() - incr);
                    if (newPos.file() < 1 || newPos.rank() < 1)
                        break;
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                    incr++;
                }
                incr = 1;
                for (int file = position.file() + 1; file <= 8; file++) {
                    Position newPos = new Position(file, position.rank() + incr);
                    if (newPos.file() > 8 || newPos.rank() > 8)
                        break;
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                    incr++;
                }
                incr = 1;
                for (int file = Math.max(position.file() - 1, 1); file >= 1; file--) {
                    Position newPos = new Position(file, position.rank() + incr);
                    if (newPos.file() < 1 || newPos.rank() > 8)
                        break;
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                    incr++;
                }
                incr = 1;
                for (int file = Math.min(position.file() + 1, 8); file <= 8; file++) {
                    Position newPos = new Position(file, position.rank() - incr);
                    if (newPos.file() > 8 || newPos.rank() < 1)
                        break;
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                    incr++;
                }
            }
        }
        return moves;
    }

    private static List<Move>  getKnightMoves(Board board,List<Position> positions){
        List<Move> moves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position p : positions) {
                for (int file = Math.max(p.file() - 2, 1); file <= Math.min(p.file() + 2, 8); file++) {
                    for (int rank = Math.max(p.rank() - 2, 1); rank <= Math.min(p.rank() + 2, 8); rank++) {
                        if (Math.abs(file - p.file()) == 2 && Math.abs(rank - p.rank()) == 1 ||
                                Math.abs(file - p.file()) == 1 && Math.abs(rank - p.rank()) == 2) {
                            Position newPos = new Position(file, rank);
                            MoveEvaluator.evaluateOccupancy(board, p, newPos, moves);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private static List<Move> getRookMoves(Board board, List<Position> positions){
        List<Move> moves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                for (int file = Math.max(position.file() - 1, 1); file >= 1; file--) {
                    Position newPos = new Position(file, position.rank());
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                }
                for (int file = Math.min(position.file() + 1, 8); file <= 8; file++) {
                    Position newPos = new Position(file, position.rank());
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                }
                for (int rank = Math.max(position.rank() - 1, 1); rank >= 1; rank--) {
                    Position newPos = new Position(position.file(), rank);
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                }
                for (int rank = Math.min(position.rank() + 1, 8); rank <= 8; rank++) {
                    Position newPos = new Position(position.file(), rank);
                    if (MoveEvaluator.evaluateOccupancy(board, position, newPos, moves)) break;
                }
            }
        }
        return moves;
    }

    private static List<Move>  getPawnMoves(Board board, List<Position> positions){
        List<Move> moves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                List<Position> newPos = new ArrayList<>();
                if (board.getPieceAt(position).getColor() == PlayerColor.WHITE) {
                    if (position.rank() < 8) {
                        newPos.add(new Position(position.file(), position.rank() + 1));
                        if (position.file() - 1 > 0)
                            newPos.add(new Position(position.file() - 1, position.rank() + 1));
                        if (position.file() + 1 < 9)
                            newPos.add(new Position(position.file() + 1, position.rank() + 1));
                    }
                    if (position.rank() == 2)
                        newPos.add(new Position(position.file(), position.rank() + 2));
                }
                if (board.getPieceAt(position).getColor() == PlayerColor.BLACK) {
                    if (position.rank() > 1) {
                        newPos.add(new Position(position.file(), position.rank() - 1));
                        if (position.file() - 1 > 0)
                            newPos.add(new Position(position.file() - 1, position.rank() - 1));
                        if (position.file() + 1 < 9)
                            newPos.add(new Position(position.file() + 1, position.rank() - 1));
                    }
                    if (position.rank() == 7)
                        newPos.add(new Position(position.file(), position.rank() - 2));
                }
                for (Position p : newPos) {
                    if (board.isOccupied(p) &&
                            board.getPieceAt(p).getColor() != board.getPieceAt(position).getColor() &&
                            position.file() != p.file())
                        moves.add(new Move(position, p));
                    if (!board.isOccupied(p) && position.file() == p.file())
                        moves.add(new Move(position, p));
                }
            }
        }
        return moves;
    }
}
