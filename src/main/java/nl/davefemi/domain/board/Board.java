package nl.davefemi.domain.board;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.domain.game.move.CastlingMove;
import nl.davefemi.domain.game.move.Move;
import nl.davefemi.domain.game.move.PromotionMove;
import nl.davefemi.domain.game.move.SingleMove;
import nl.davefemi.domain.piece.Piece;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import nl.davefemi.exception.BoardException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Slf4j
public class Board {
    private TreeMap<Position, Piece> positions;

    public Board(){
        initBoard();
        initPieces();
    }

    private void initBoard(){
        positions = new TreeMap<>();
        for (int rank = 1; rank<9; rank++){
            for (int file = 1; file<9; file++){
                positions.put(new Position (file, rank), null);
            }
        }
    }

    private void initPieces(){
        for (PlayerColor c : PlayerColor.values()){
            createPawns(c);
            createRooks(c);
            createKnights(c);
            createBishops(c);
            createQueen(c);
            createKing(c);
        }
    }

    private void createPawns(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 2 : 7;
        for (int file = 1; file<9; file++){
            positions.put(new Position (file,rank), new Piece(PieceType.PAWN, color));
        }
    }

    private void createRooks(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(1, rank), new Piece(PieceType.ROOK, color));
        positions.put(new Position(8, rank), new Piece(PieceType.ROOK, color));
    }

    private void createKnights(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(2, rank), new Piece(PieceType.KNIGHT, color));
        positions.put(new Position(7, rank), new Piece(PieceType.KNIGHT, color));
    }

    private void createBishops(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(3, rank), new Piece(PieceType.BISHOP, color));
        positions.put(new Position(6, rank), new Piece(PieceType.BISHOP, color));
    }

    private void createQueen(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(4, rank), new Piece(PieceType.QUEEN, color));
    }

    private void createKing(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(5, rank), new Piece(PieceType.KING, color));
    }

    public boolean isOccupied(Position position){
        return positions.get(position) != null;
    }

    public Piece getPieceAt(Position position){
        if (!isOccupied(position))
                return null;
        return positions.get(position);
    }

    public Piece movePieceTo(Move move){
        if (move instanceof CastlingMove(SingleMove moveKing, SingleMove moveRook)){
            updatePositions((moveKing));
            updatePositions(moveRook);
            return null;
        }
        if (move instanceof PromotionMove(Position position, String newPiece)){
            return promotePawnTo(position, newPiece);
        }
        return updatePositions((SingleMove) move);
    }

    private Piece updatePositions(SingleMove move) throws BoardException{
        if (positions.get(move.from()) == null)
            throw new BoardException("There is no piece at " + move.from().toString());
        Piece p = positions.get(move.to());
        positions.put(move.to(), getPieceAt(move.from()));
        positions.put(move.from(), null);
        return p;
    }

    public List<Position> getPositions(){
        return new ArrayList<>(this.positions.keySet());
    }

    public PieceType getPieceType(String pieceType) {
        PieceType[] types = PieceType.values();
        for (PieceType p : types) {
            if (p.getLabel().equals(pieceType)) {
                return p;
            }
        }
        return null;
    }

    private Piece promotePawnTo(Position position, String pieceType){
        PieceType type = getPieceType(pieceType);
        if (type == null)
            throw new BoardException("Piece does not exist");
        PlayerColor color = getPieceAt(position).getColor();
        Piece p = positions.get(position);
        positions.put(position, new Piece(type, color));
        return p;
    }

    public Board (Board other){
        this.positions = new TreeMap<>();
        this.positions.putAll(other.positions);
    }
}