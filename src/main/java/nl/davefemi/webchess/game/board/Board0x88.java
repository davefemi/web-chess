package nl.davefemi.webchess.game.board;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.*;

import java.util.*;

@Slf4j
public final class Board0x88 {
    private final Piece[] squares = new Piece[128];
    private final IdGenerator pieceIdGenerator;
    private static final int AND_HEX = 0x88;

    public Board0x88(){
        pieceIdGenerator = new IdGenerator();
        PieceFactory.populateBoardWithPieces(squares, pieceIdGenerator);
        for (int i = 0; i<squares.length; i++) {
            Piece p = squares[i];
            if (p != null) {
                Square square = new Square(i);
                System.out.println("SQUARE " + AlgebraicSquare.fromFileAndRank(square.file(), square.rank()).value() +
                        " contains a " + p.getColor().getColor() + " " + p.getType());
            }
        }
    }

    public Board0x88(Board0x88 other){
        this.pieceIdGenerator = other.pieceIdGenerator;
        for (int i = 0 ; i<0x79; i++){
            if (!isLegalBoardPosition(i)){
                squares[i] = other.squares[i];
            }
        }
    }

    public Board0x88(Piece[] pieces, int nextId) throws BoardException {
        this.pieceIdGenerator = new IdGenerator(nextId);
        if(pieces.length != 128)
            throw new IllegalArgumentException("Array length must be exactly 128");
        addPiecesToBoard(pieces);
    }


    public int getPositionById(int id){
        for (int i = 0; i < squares.length; i++){
            if (squares[i] != null && squares[i].getId() == id){
                return i;
                }
            }
        return -1;
    }

    public List<Integer> getPositionsByTypeAndColor(PieceType type, PieceColor color){
        List<Integer> foundPositions = new ArrayList<>();
        for (int i = 0; i < squares.length; i++)
            if (squares[i] !=null && squares[i].getType() == type && squares[i].getColor() == color){
                foundPositions.add(i);
            }
        return foundPositions;
    }

    public boolean isBoardPositionOccupied(int position) throws BoardException {
        if (!isLegalBoardPosition(position)) {
            throw new BoardException("Invalid position");
        }
        return squares[position] != null;
    }

    public int piecesOnBoard(){
        return (new HashSet<>(List.of(squares)).size()-1);
    }

    public Piece getPieceAt(int position) throws BoardException {
        if (!isLegalBoardPosition(position)) {
            throw new BoardException("Invalid position");
        }
        return squares[position];
    }

    List<Piece> getPieces(){
        return Arrays.stream(squares).toList();
    }

    synchronized Piece applyValidatedMove(Move move) throws BoardException {
        if (move instanceof CastlingMove0x88(SingleMove0x88 moveKing, SingleMove0x88 moveRook)){
            updatePiecePositions((moveKing));
            updatePiecePositions(moveRook);
            return null;
        }
        if (move instanceof PromotionMove0x88(SingleMove0x88 pawnMove, PieceType newPiece) ){
            return promotePawnTo(pieceIdGenerator.getNextId(), pawnMove, newPiece);
        }
        if (move instanceof EnPassantMove0x88 m)
            return applyEnPassantMove(m);
        return updatePiecePositions((SingleMove0x88) move);
    }

    private boolean isLegalBoardPosition(int position){
        return ((position & AND_HEX) == 0);
    }

    private void addPiecesToBoard(Piece[] pieces)
            throws BoardException {
        Set<PieceColor> kingsByColor = new HashSet<>();
        HashSet<Integer> uniquePieces = new HashSet<>();
        for (int i = 0; i < squares.length; i++){
            Piece p = pieces[i];
            if (p!= null){
                if (!isLegalBoardPosition(i))
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

    private Piece updatePiecePositions(SingleMove0x88 move) throws BoardException{
        if (move == null)
            return null;
        if (!isLegalBoardPosition(move.from().value()))
            throw new BoardException("Position " + move.from().value() + " does not exist");
        if (!isLegalBoardPosition(move.to().value()))
            throw new BoardException("Position " + move.to().value() + " does not exist");
        if (squares[move.from().value()] == null)
            throw new BoardException("There is no piece at " + move.from().value());
        Piece p = squares[move.to().value()];
        squares[move.to().value()] = squares[move.from().value()];
        squares[move.from().value()] = null;
        return p;
    }

    private Piece promotePawnTo(int id, SingleMove0x88 pawnMove, PieceType pieceType) throws BoardException {
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

    private Piece applyEnPassantMove(EnPassantMove0x88 move) throws BoardException {
        updatePiecePositions(new SingleMove0x88(move.from(), move.to()));
        Piece p = squares[move.to().value()-16];
        squares[move.to().value()-16] = null;
        return p;
    }
}