package nl.davefemi.game.board;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.game.IdGenerator;
import nl.davefemi.game.actions.CastlingMove;
import nl.davefemi.game.actions.Move;
import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.game.actions.SingleMove;
import nl.davefemi.exception.BoardException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

@Slf4j
public class Board {
    private TreeMap<Position, Piece> positions;
    private final List<Integer> originalRooks = new ArrayList<>();

    public Board(IdGenerator pieceIdGenerator){
        initBoard();
        initPieces(pieceIdGenerator);
    }

    public Board (Board other){
        this.positions = new TreeMap<>();
        this.positions.putAll(other.positions);
        this.originalRooks.addAll(other.originalRooks);
    }

    public Board(TreeMap<Position, Piece> positions, List<Integer> originalRooks) throws BoardException {
        initBoard();
        matchBoard(positions);
        this.originalRooks.addAll(originalRooks);
    }

    public boolean isBoardPositionOccupied(Position position){
        return positions.get(position) != null;
    }

    public int piecesOnBoard(){
        return (new HashSet<>(positions.values()).size()-1);
    }

    public Piece getPieceAt(Position position){
        if (!isBoardPositionOccupied(position))
            return null;
        return positions.get(position);
    }

    public List<Position> getBoardPositions(){
        return new ArrayList<>(this.positions.keySet());
    }

    public List<Integer> getOriginalRooks(){
        return new ArrayList<>(originalRooks);
    }

    public boolean isOriginalRook(int pieceId){
        return originalRooks.contains(pieceId);
    }

    public Piece applyValidatedMove(IdGenerator pieceIdGenerator, Move move) throws BoardException {
        if (move instanceof CastlingMove(SingleMove moveKing, SingleMove moveRook)){
            updatePiecePositions((moveKing));
            updatePiecePositions(moveRook);
            return null;
        }
        if (move instanceof PromotionMove(Position position, PieceType newPiece)){
            return promotePawnTo(pieceIdGenerator, position, newPiece);
        }
        return updatePiecePositions((SingleMove) move);
    }

    public Piece applyValidatedMove(Move move) throws BoardException {
        if (move instanceof PromotionMove)
            throw new IllegalArgumentException("New piece MUST have an id");
        if (move instanceof CastlingMove(SingleMove moveKing, SingleMove moveRook)){
            updatePiecePositions((moveKing));
            updatePiecePositions(moveRook);
            return null;
        }
        return updatePiecePositions((SingleMove) move);
    }

    private void matchBoard(TreeMap<Position, Piece> positions) throws BoardException {
        HashSet<Position> newPos = new HashSet<>(positions.keySet());
        HashSet<Position> pos = new HashSet<>(this.positions.keySet());
        if(!pos.equals(newPos))
            throw new BoardException("Positions are incorrect");
        for (Position p : pos){
            this.positions.put(p, positions.get(p));
        }
    }

    private void initBoard(){
        positions = new TreeMap<>();
        for (int rank = 1; rank<9; rank++){
            for (int file = 1; file<9; file++){
                positions.put(new Position (file, rank), null);
            }
        }
    }

    private void initPieces(IdGenerator pieceIdGenerator){
        for (PieceColor c : PieceColor.values()){
            createPawns(pieceIdGenerator, c);
            createRooks(pieceIdGenerator, c);
            createKnights(pieceIdGenerator, c);
            createBishops(pieceIdGenerator, c);
            createQueen(pieceIdGenerator, c);
            createKing(pieceIdGenerator, c);
        }
    }

    private void createPawns(IdGenerator pieceIdGenerator, PieceColor color){
        int rank = color == PieceColor.WHITE? 2 : 7;
        for (int file = 1; file<9; file++){
            positions.put(new Position (file,rank), new Piece(pieceIdGenerator.getNextId(), PieceType.PAWN, color));
        }
    }

    private void createRooks(IdGenerator pieceIdGenerator, PieceColor color){
        int rank = color == PieceColor.WHITE? 1 : 8;
        Piece queenSideRook = new Piece(pieceIdGenerator.getNextId(), PieceType.ROOK, color);
        Piece kingSideRook = new Piece(pieceIdGenerator.getNextId(), PieceType.ROOK, color);
        positions.put(new Position(1, rank), queenSideRook);
        positions.put(new Position(8, rank), kingSideRook);
        originalRooks.add(queenSideRook.getId());
        originalRooks.add(kingSideRook.getId());
    }

    private void createKnights(IdGenerator pieceIdGenerator, PieceColor color){
        int rank = color == PieceColor.WHITE? 1 : 8;
        positions.put(new Position(2, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.KNIGHT, color));
        positions.put(new Position(7, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.KNIGHT, color));
    }

    private void createBishops(IdGenerator pieceIdGenerator, PieceColor color){
        int rank = color == PieceColor.WHITE? 1 : 8;
        positions.put(new Position(3, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.BISHOP, color));
        positions.put(new Position(6, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.BISHOP, color));
    }

    private void createQueen(IdGenerator pieceIdGenerator, PieceColor color){
        int rank = color == PieceColor.WHITE? 1 : 8;
        positions.put(new Position(4, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.QUEEN, color));
    }

    private void createKing(IdGenerator pieceIdGenerator, PieceColor color){
        int rank = color == PieceColor.WHITE? 1 : 8;
        positions.put(new Position(5, rank), new Piece(pieceIdGenerator.getNextId(), PieceType.KING, color));
    }

    private Piece updatePiecePositions(SingleMove move) throws BoardException{
        if (positions.get(move.from()) == null)
            throw new BoardException("There is no piece at " + move.from().toString());
        Piece p = positions.get(move.to());
        positions.put(move.to(), getPieceAt(move.from()));
        positions.put(move.from(), null);
        return p;
    }

    private Piece promotePawnTo(IdGenerator pieceIdGenerator, Position position, PieceType pieceType) {
        PieceColor color = getPieceAt(position).getColor();
        Piece p = positions.get(position);
        positions.put(position, new Piece(pieceIdGenerator.getNextId(), pieceType, color));
        return p;
    }
}