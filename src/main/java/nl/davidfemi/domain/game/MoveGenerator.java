package nl.davidfemi.domain.game;

import nl.davidfemi.domain.board.Board;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.pieces.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveGenerator {

    public static boolean isMoveAllowed(Game game, Move move) {
        return (getLegalMoves(game).contains(move));
    }

    public static List<Move> getLegalMoves(Game game){
        List<Move> moves = new ArrayList<>();
        PlayerColor color = game.getTurn();
        Board board = game.getGameBoard();
        List<Position> positions = board.getPositions();
        Position king = null;
        Position queen = null;
        List<Position> bishops = new ArrayList<>();
        List<Position> knights = new ArrayList<>();
        List<Position> rooks = new ArrayList<>();
        List<Position> pawns = new ArrayList<>();
        for (Position p : positions){
            Piece piece = board.getPieceAt(p);
            if (piece != null && piece.getColor() == color) {
                if (piece.getType() == PieceType.KING)
                    king = p;
                if (piece.getType() == PieceType.QUEEN)
                    queen = p;
                if (piece.getType() == PieceType.BISHOP)
                    bishops.add(p);
                if (piece.getType() == PieceType.KNIGHT)
                    knights.add(p);
                if (piece.getType() == PieceType.ROOK)
                    rooks.add(p);
                if (piece.getType() == PieceType.PAWN)
                    pawns.add(p);
            }
        }
        if (checkKing(king)){
            return getKingMoves(board, king);
        }
        moves.addAll(getRookMoves(board, rooks));
        moves.addAll(getKnightMoves(board, knights));
        moves.addAll(getBishopMoves(board, bishops));
        moves.addAll(getPawnMoves(board, pawns));
//        for (Position p : positions){
//            if (board.getPieceAt(p)!= null){
//                for (Position pp : positions) {
//                    if (board.getPieceAt(pp) == null){
//                        moves.add(new Move (p, pp));
//                    }
//                }
//            }
//        }
        return moves;
    }

    private static boolean checkKing(Position position){
        return false;
    }

    private static List<Move>  getKingMoves(Board board,Position position){
        List<Move> moves = new ArrayList<>();
        for (int file = Math.max(position.file()-1, 1) ; file <= Math.min(position.file()+1, 8); file ++ ){
            for (int rank = Math.max(position.rank()-1, 1); rank <= Math.min(position.rank()+1,8); rank++){
                Position newPos = new Position(file,rank);
                if (!board.isOccupied(newPos)){
                    if (!checkKing(newPos)){
                        moves.add(new Move(position, newPos));
                    }
                }
            }
        }
        System.out.println(moves);
        return moves;
    }

    private static List<Move>  getQueenMoves(Position position){
        List<Move> moves = new ArrayList<>();

        return moves;
    }

    private static List<Move>  getBishopMoves(Board board, List<Position> positions){
        List<Move> moves = new ArrayList<>();
        for (Position position: positions) {
            int incr = 1;
            for (int file = position.file() - 1; file >= 1; file--) {
                Position newPos = new Position(file, position.rank() - incr);
                if (newPos.file() < 1 || newPos.rank() < 1)
                    break;
                if (evaluateOccupancy(board, position, newPos, moves)) break;
                incr--;
            }
            incr = 1;
            for (int file = position.file() + 1; file <= 8; file++) {
                Position newPos = new Position(file, position.rank() + incr);
                if (newPos.file() > 8 || newPos.rank() > 8)
                    break;
                if (evaluateOccupancy(board, position, newPos, moves)) break;
                incr++;
            }
            incr = 1;
            for (int file = Math.max(position.file() - 1, 1); file >= 1; file--) {
                int rank = file + 1;
                Position newPos = new Position(file, rank + incr);
                if (newPos.file() < 1 || newPos.rank() > 8)
                    break;
                if (evaluateOccupancy(board, position, newPos, moves)) break;
                incr++;
            }
            incr = 1;
            for (int file = Math.min(position.file() + 1, 8); file <= 8; file++) {
                Position newPos = new Position(file, position.rank() - incr);
                if (newPos.file() > 8 || newPos.rank() < 1)
                    break;
                if (evaluateOccupancy(board, position, newPos, moves)) break;
                incr--;
            }
        }
        return moves;
    }

    private static boolean evaluateOccupancy(Board board, Position oldPos, Position newPos, List<Move> moves){
        if (board.isOccupied(newPos)) {
            if (board.getPieceAt(oldPos).getColor() == board.getPieceAt(newPos).getColor())
                return true;
            moves.add(new Move(oldPos, newPos));
            return true;
        }
        moves.add(new Move(oldPos, newPos));
        return false;
    }

    private static List<Move>  getKnightMoves(Board board,List<Position> positions){
        List<Move> moves = new ArrayList<>();
        for (Position p: positions)
        for (int file = Math.max(p.file()-2, 1); file <= Math.min(p.file()+2, 8); file++){
            for (int rank = Math.max(p.rank()-2, 1); rank <= Math.min(p.rank()+2, 8); rank++){
                if (Math.abs(file-p.file()) ==2 && Math.abs(rank-p.rank())==1 ||
                Math.abs(file-p.file()) ==1 && Math.abs(rank-p.rank())==2){
                    Position newPos = new Position(file, rank);
                    evaluateOccupancy(board, p, newPos, moves);
                }
            }
        }
        return moves;
    }

    // Castling
    private static List<Move> getRookMoves(Board board, List<Position> positions){
        List<Move> moves = new ArrayList<>();
        for (Position position : positions) {
            for (int file = Math.max(position.file()-1, 1); file >= 1; file--) {
                Position newPos = new Position(file, position.rank());
                if (evaluateOccupancy(board, position, newPos, moves)) break;
            }
            for (int file = Math.min(position.file()+1, 8); file <= 8; file++) {
                Position newPos = new Position(file, position.rank());
                if (evaluateOccupancy(board, position, newPos, moves)) break;
            }
            for (int rank = Math.max(position.rank()-1, 1); rank >= 1; rank--) {
                Position newPos = new Position(position.file(), rank);
                if (evaluateOccupancy(board, position, newPos, moves)) break;
            }
            for (int rank = Math.min(position.rank()+1, 8); rank <= 8; rank++) {
                Position newPos = new Position(position.file(), rank);
                if (evaluateOccupancy(board, position, newPos, moves)) break;
            }
        }
        return moves;
    }

    private static List<Move>  getPawnMoves(Board board, List<Position> positions){
        List<Move> moves = new ArrayList<>();
        for (Position position : positions){
            List<Position> newPos = new ArrayList<>();
            if (board.getPieceAt(position).getColor() == PlayerColor.WHITE){
                if (position.rank() < 8){
                    newPos.add(new Position(position.file(), position.rank()+1));
                    if(position.file()-1 > 0)
                        newPos.add(new Position(position.file()-1, position.rank()+1));
                    if(position.file()+1<9)
                        newPos.add(new Position(position.file()+1, position.rank()+1));                }
                if(position.rank() == 2)
                    newPos.add(new Position(position.file(), position.rank()+2));
                }
            if (board.getPieceAt(position).getColor() == PlayerColor.BLACK){
                if (position.rank() > 1){
                    newPos.add(new Position(position.file(), position.rank()-1));
                    if(position.file()-1 > 0)
                        newPos.add(new Position(position.file()-1, position.rank()-1));
                    if(position.file()+1<9)
                        newPos.add(new Position(position.file()+1, position.rank()-1));                }
                if(position.rank() == 7)
                    newPos.add(new Position(position.file(), position.rank()-2));
            }
            for (Position p : newPos){
                if (board.isOccupied(p) &&
                        board.getPieceAt(p).getColor() != board.getPieceAt(position).getColor() &&
                        position.file() != p.file())
                        moves.add(new Move(position, p));
                if (!board.isOccupied(p) && position.file() == p.file())
                    moves.add(new Move(position, p));
            }
        }
        return moves;
    }
}
