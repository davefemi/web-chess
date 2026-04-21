package nl.davidfemi.domain.board;

import nl.davidfemi.domain.game.utility.CastlingMove;
import nl.davidfemi.domain.game.utility.Move;
import nl.davidfemi.domain.pieces.Piece;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Board {
    private TreeMap<Position, Piece> squares;

    public Board(){
        initBoard();
        initPieces();
    }

    private void initBoard(){
        squares = new TreeMap<>();
        for (int rank = 1; rank<9; rank++){
            for (int file = 1; file<9; file++){
                squares.put(new Position (file, rank), null);
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
            squares.put(new Position (file,rank), new Piece(PieceType.PAWN, color));
        }
    }

    private void createRooks(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(1, rank), new Piece(PieceType.ROOK, color));
        squares.put(new Position(8, rank), new Piece(PieceType.ROOK, color));
    }

    private void createKnights(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(2, rank), new Piece(PieceType.KNIGHT, color));
        squares.put(new Position(7, rank), new Piece(PieceType.KNIGHT, color));
    }

    private void createBishops(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(3, rank), new Piece(PieceType.BISHOP, color));
        squares.put(new Position(6, rank), new Piece(PieceType.BISHOP, color));
    }

    private void createQueen(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(4, rank), new Piece(PieceType.QUEEN, color));
    }

    private void createKing(PlayerColor color){
        int rank = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(5, rank), new Piece(PieceType.KING, color));
    }

    public boolean isOccupied(Position position){
        return squares.get(position) != null;
    }

    public Piece getPieceAt(Position position){
        if (!isOccupied(position))
                return null;
        return squares.get(position);
    }

    public boolean movePieceTo(Move move){
        squares.put(move.to(), getPieceAt(move.from()));
        squares.put(move.from(), null);
        return true;
    }

    public boolean movePieceTo(CastlingMove move){
        return (movePieceTo(move.moveKing()) && movePieceTo(move.moveRook()));
    }

    public List<Position> getPositions(){
        List<Position> positions = new ArrayList<>();
        positions.addAll(squares.keySet());
        return positions;
    }

    public boolean promotePawnTo(Position position, PieceType pieceType){
        PlayerColor color = getPieceAt(position).getColor();
        squares.put(position, new Piece(pieceType, color));
        return true;
    }

    public void printBoard(){
        int sz = 0;
        for(Position p: squares.keySet()){
            if (squares.get(p) != null) {
                sz++;
                System.out.println("" + p.getPosition() + squares.get(p).getType() + squares.get(p).getColor().name());
            }
        }
        System.out.println("Total: " + sz + " pieces");
    }

    public Board (Board other){
        this.squares = new TreeMap<>();
        other.squares.forEach(this.squares::put);
    }
}
