package nl.davefemi.domain.board;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.domain.game.PieceIdGenerator;
import nl.davefemi.domain.game.actions.move.CastlingMove;
import nl.davefemi.domain.game.actions.move.Move;
import nl.davefemi.domain.game.actions.move.PromotionMove;
import nl.davefemi.domain.game.actions.move.SingleMove;
import nl.davefemi.exception.BoardException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Slf4j
public class Board {
    private TreeMap<Position, Piece> positions;
    private final List<Integer> originalRooks = new ArrayList<>();

    public Board(PieceIdGenerator pieceIdGenerator){
        initBoard();
        initPieces(pieceIdGenerator);
    }

    public Board (Board other){
        this.positions = new TreeMap<>();
        this.positions.putAll(other.positions);
        this.originalRooks.addAll(other.originalRooks);
    }

    public Board(TreeMap<Position, Piece> positions, List<Integer> originalRooks){
        this.positions = new TreeMap<>();
        this.positions.putAll(positions);
        this.originalRooks.addAll(originalRooks);
    }

    private void initBoard(){
        positions = new TreeMap<>();
        for (int rank = 1; rank<9; rank++){
            for (int file = 1; file<9; file++){
                positions.put(new Position (file, rank), null);
            }
        }
    }

    private void initPieces(PieceIdGenerator pieceIdGenerator){
        for (PlayerColor c : PlayerColor.values()){
            createPawns(pieceIdGenerator, c);
            createRooks(pieceIdGenerator, c);
            createKnights(pieceIdGenerator, c);
            createBishops(pieceIdGenerator, c);
            createQueen(pieceIdGenerator, c);
            createKing(pieceIdGenerator, c);
        }
    }

    private void createPawns(PieceIdGenerator pieceIdGenerator, PlayerColor color){
        int rank = color == PlayerColor.WHITE? 2 : 7;
        for (int file = 1; file<9; file++){
            positions.put(new Position (file,rank), new Piece(pieceIdGenerator.getNextId(), PieceType.PAWN, color));
        }
    }

    private void createRooks(PieceIdGenerator pieceIdGenerator, PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        Piece queenSideRook = new Piece(pieceIdGenerator.getNextId(), PieceType.ROOK, color);
        Piece kingSideRook = new Piece(pieceIdGenerator.getNextId(), PieceType.ROOK, color);
        positions.put(new Position(1, rank), queenSideRook);
        positions.put(new Position(8, rank), kingSideRook);
        originalRooks.add(queenSideRook.getId());
        originalRooks.add(kingSideRook.getId());
    }

    private void createKnights(PieceIdGenerator pieceIdGenerator, PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(2, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.KNIGHT, color));
        positions.put(new Position(7, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.KNIGHT, color));
    }

    private void createBishops(PieceIdGenerator pieceIdGenerator, PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(3, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.BISHOP, color));
        positions.put(new Position(6, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.BISHOP, color));
    }

    private void createQueen(PieceIdGenerator pieceIdGenerator, PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(4, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.QUEEN, color));
    }

    private void createKing(PieceIdGenerator pieceIdGenerator, PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        positions.put(new Position(5, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.KING, color));
    }

    public boolean isOccupied(Position position){
        return positions.get(position) != null;
    }

    public Piece getPieceAt(Position position){
        if (!isOccupied(position))
                return null;
        return positions.get(position);
    }

    public Piece movePieceTo(PieceIdGenerator pieceIdGenerator, Move move) throws BoardException {
        if (move instanceof CastlingMove(SingleMove moveKing, SingleMove moveRook)){
            updatePositions((moveKing));
            updatePositions(moveRook);
            return null;
        }
        if (move instanceof PromotionMove(Position position, String newPiece)){
            return promotePawnTo(pieceIdGenerator, position, newPiece);
        }
        return updatePositions((SingleMove) move);
    }

    public Piece movePieceTo(Move move) throws BoardException {
        if (move instanceof CastlingMove(SingleMove moveKing, SingleMove moveRook)){
            updatePositions((moveKing));
            updatePositions(moveRook);
            return null;
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

    public List<Integer> getOriginalRooks(){
        return new ArrayList<>(originalRooks);
    }

    public boolean isOriginalRook(int pieceId){
        return originalRooks.contains(pieceId);
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

    private Piece promotePawnTo(PieceIdGenerator pieceIdGenerator, Position position, String pieceType) throws BoardException {
        PieceType type = getPieceType(pieceType);
        if (type == null)
            throw new BoardException("Piece does not exist");
        PlayerColor color = getPieceAt(position).getColor();
        Piece p = positions.get(position);
        positions.put(position, new Piece(pieceIdGenerator.getNextId(), type, color));
        return p;
    }


}