package nl.davefemi.webchess.game.board;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.game.IdGenerator;
import nl.davefemi.webchess.game.actions.*;
import nl.davefemi.webchess.exception.BoardException;

import java.util.*;

@Slf4j
public class Board {
    private TreeMap<Position, Piece> positions;

    public Board(IdGenerator pieceIdGenerator){
        initBoard();
        initPieces(pieceIdGenerator);
    }

    public Board (Board other){
        this.positions = new TreeMap<>();
        this.positions.putAll(other.positions);
    }

    public Board(TreeMap<Position, Piece> positions) throws BoardException {
        initBoard();
        if (validateSubmittedBoard(positions.keySet()) &&
                validateSubmittedPieces(positions.values())) {
            this.positions = new TreeMap<>(positions);
        }
    }

    public Position getPositionById(int id){
        for (Piece p: positions.values()){
            if (p.getId() == id){
                for (Position pos : positions.keySet()) {
                    if (positions.get(pos) == p)
                        return pos;
                }
            }
        }
        return null;
    }

    public List<Position> getPositionsByTypeAndColor(PieceType type, PieceColor color){
        List<Position> foundPositions = new ArrayList<>();
        for (Piece p: positions.values()){
            if (p!=null && p.getType() == type && p.getColor() == color){
                for (Position pos : positions.keySet()) {
                    if (positions.get(pos) == p)
                        foundPositions.add(pos);
                }
            }
        }
        return foundPositions;
    }

    public boolean isBoardPositionOccupied(Position position){
        return positions.get(position) != null;
    }

    public int piecesOnBoard(){
        return (new HashSet<>(positions.values()).size()-1);
    }

    public Piece getPieceAt(Position position){
        return positions.get(position);
    }

    public List<Position> getBoardPositions(){
        return new ArrayList<>(this.positions.keySet());
    }

    public Piece applyValidatedMove(IdGenerator pieceIdGenerator, Move move) throws BoardException {
        if (move instanceof CastlingMove(SingleMove moveKing, SingleMove moveRook)){
            updatePiecePositions((moveKing));
            updatePiecePositions(moveRook);
            return null;
        }
        if (move instanceof PromotionMove(SingleMove pawnMove, PieceType newPiece) ){
            return promotePawnTo(pieceIdGenerator.getNextId(), pawnMove, newPiece);
        }
        if (move instanceof EnPassantMove m)
            return applyEnPassantMove(m);
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
        if (move instanceof EnPassantMove m)
            return applyEnPassantMove(m);
        return updatePiecePositions((SingleMove) move);
    }

    private boolean validateSubmittedBoard(Collection<Position> newPositions) throws BoardException {
        if(newPositions.size() != 64)
            throw new BoardException("Board must contain exactly 64 positions");
        if(this.positions.size() != new HashSet<>(newPositions).size())
            throw new BoardException("Board contains duplicate positions");
        return true;

    }

    private boolean validateSubmittedPieces(Collection<Piece> newPieces)
            throws BoardException {
        Set<PieceColor> kingsByColor = new HashSet<>();
        for (Piece p: newPieces){
            if (p != null && p.getType() == PieceType.KING) {
                if (!kingsByColor.add(p.getColor()))
                    throw new BoardException("Board cannot have two kings of the same color");
            }
        }
        if (kingsByColor.size() != 2)
            throw new BoardException("There can only be exactly one king of each color on the board");
        HashSet<Integer> uniquePieces = new HashSet<>();
        for (Piece p : newPieces){
            if (p != null && !uniquePieces.add(p.getId()))
                throw new BoardException("Pieces are not unique");
        }
        if (uniquePieces.isEmpty())
            throw new BoardException("No pieces have been presented");
        return true;
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
        if (move == null)
            return null;
        if (!positions.containsKey(move.from()))
            throw new BoardException("Position " + move.from().toString() + " does not exist");
        if (!positions.containsKey(move.to()))
            throw new BoardException("Position " + move.from().toString() + " does not exist");
        if (positions.get(move.from()) == null)
            throw new BoardException("There is no piece at " + move.from().toString());
        Piece p = positions.get(move.to());
        positions.put(move.to(), getPieceAt(move.from()));
        positions.put(move.from(), null);
        return p;
    }

    private Piece promotePawnTo(int id, SingleMove pawnMove, PieceType pieceType) throws BoardException {
        for (Piece t : positions.values()) {
            if (t != null && t.getId() == id)
                throw new BoardException("New id cannot already exist");
        }
        Piece p = positions.get(pawnMove.from());
        PieceColor color = p.getColor();
        updatePiecePositions(pawnMove);
        positions.put(pawnMove.to(), new Piece(id, pieceType, color));
        return p;
    }

    private Piece applyEnPassantMove(EnPassantMove move) throws BoardException {
        updatePiecePositions(new SingleMove(move.from(), move.to()));
        return positions.remove(new Position(move.to().file(), move.from().rank()));
    }
}