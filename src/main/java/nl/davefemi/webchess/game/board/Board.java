package nl.davefemi.webchess.game.board;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.*;

import java.util.*;

@Slf4j
public final class Board {
    private final Piece[] squares = new Piece[128];
    private final IdGenerator pieceIdGenerator;
    private static final int AND_HEX = 0x88;

    public Board(){
        pieceIdGenerator = new IdGenerator();
        PieceFactory.populateBoardWithPieces(squares, pieceIdGenerator);
    }

    public Board(Board other){
        this.pieceIdGenerator = other.pieceIdGenerator;
        for (int i = 0 ; i<0x79; i++){
            if (isLegalPosition(i)){
                squares[i] = other.squares[i];
            }
        }
    }

    public Board(Piece[] pieces, int nextId) throws BoardException {
        this.pieceIdGenerator = new IdGenerator(nextId);
        if(pieces.length != 128)
            throw new IllegalArgumentException("Array length must be exactly 128");
        addPiecesToBoard(pieces);
    }

    public int getNextPieceId(){
        return pieceIdGenerator.peek();
    }


    public Square getPositionById(int id) throws BoardException {
        for (int i = 0; i < squares.length; i++){
            if (squares[i] != null && squares[i].getId() == id){
                return new Square(i);
                }
            }
        throw new BoardException("Piece not found");
    }

    public List<Square> getPositionsByTypeAndColor(PieceType type, PieceColor color){
        List<Square> foundPositions = new ArrayList<>();
        for (int i = 0; i < squares.length; i++)
            if (squares[i] !=null && squares[i].getType() == type && squares[i].getColor() == color){
                foundPositions.add(new Square(i));
            }
        return foundPositions;
    }

    public boolean isPositionOccupied(Square position) throws BoardException {
        if (!isLegalPosition(position.value())) {
            throw new BoardException("Invalid position");
        }
        return squares[position.value()] != null;
    }

    public int piecesOnBoard(){
        return (new HashSet<>(Arrays.asList(squares)).size()-1);
    }

    public Piece getPieceAt(Square position) throws BoardException {
        if (!isLegalPosition(position.value())) {
            throw new BoardException("Invalid position");
        }
        return squares[position.value()];
    }

    public List<Piece> getPieces(){
        List<Piece> pieces = new ArrayList<>();
        for (Piece p: squares){
            if (p != null)
                pieces.add(p);
        }
        return pieces;
    }

    synchronized Piece applyValidatedMove(Move move) throws BoardException {
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

    public static boolean isLegalPosition(int position){
        return ((position & AND_HEX) == 0);
    }

    private void addPiecesToBoard(Piece[] pieces)
            throws BoardException {
        Set<PieceColor> kingsByColor = new HashSet<>();
        HashSet<Integer> uniquePieces = new HashSet<>();
        for (int i = 0; i < squares.length; i++){
            Piece p = pieces[i];
            if (p!= null){
                if (!isLegalPosition(i))
                    throw new BoardException(p.getType() + " " + p.getId() + " on illegal position: " +i);
                if (p.getType() == PieceType.KING && !kingsByColor.add(p.getColor()))
                    throw new BoardException("Board cannot have two kings of the same color");
                if (!uniquePieces.add(p.getId()))
                    throw new BoardException("Pieces are not unique");
                squares[i] = p;
            }
        }
        if (kingsByColor.size() != 2)
            throw new BoardException("There can only be exactly one king of each color on the board");
    }

    private Piece updatePiecePositions(SingleMove move) throws BoardException{
        if (move == null)
            return null;
        if (!isLegalPosition(move.from().value()))
            throw new BoardException("Position " + move.from().value() + " does not exist");
        if (!isLegalPosition(move.to().value()))
            throw new BoardException("Position " + move.to().value() + " does not exist");
        if (squares[move.from().value()] == null)
            throw new BoardException("There is no piece at " + move.from().value());
        Piece p = squares[move.to().value()];
        squares[move.to().value()] = squares[move.from().value()];
        squares[move.from().value()] = null;
        return p;
    }

    private Piece promotePawnTo(int id, SingleMove pawnMove, PieceType pieceType) throws BoardException {
        for (Piece t : squares) {
            if (t != null && t.getId() == id)
                throw new BoardException("New id cannot already exist");
        }
        Piece p = squares[pawnMove.from().value()];
        PieceColor color = p.getColor();
        updatePiecePositions(pawnMove);
        squares[pawnMove.to().value()] = new Piece(id, pieceType, color);
        return p;
    }

    private Piece applyEnPassantMove(EnPassantMove move) throws BoardException {
        updatePiecePositions(new SingleMove(move.from(), move.to()));
        Piece p = squares[move.to().value()-16];
        squares[move.to().value()-16] = null;
        return p;
    }
}