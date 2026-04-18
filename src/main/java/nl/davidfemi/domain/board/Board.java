package nl.davidfemi.domain.board;

import nl.davidfemi.domain.pieces.Piece;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;

import java.util.HashMap;

public class Board {
    private HashMap<Position, Piece> squares;

    public Board(){
        initBoard();
        initPieces();
    }
    private void initBoard(){
        squares = new HashMap<>();
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

        for(Position p: squares.keySet()){
            if (squares.get(p) != null)
                System.out.println("" + p.getPosition() + squares.get(p).getType() + squares.get(p).getColor().name());
        }
    }

    private void createPawns(PlayerColor color){
        int file = color == PlayerColor.WHITE? 2 : 7;
        for (int rank = 1; rank<9; rank++){
            squares.put(new Position (file,rank), new Piece(PieceType.PAWN, color));
        }
    }

    private void createRooks(PlayerColor color){
        int file = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(file,1), new Piece(PieceType.ROOK, color));
        squares.put(new Position(file,8), new Piece(PieceType.ROOK, color));
    }

    private void createKnights(PlayerColor color){
        int file = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(file,2), new Piece(PieceType.KNIGHT, color));
        squares.put(new Position(file,7), new Piece(PieceType.KNIGHT, color));
    }

    private void createBishops(PlayerColor color){
        int file = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(file,3), new Piece(PieceType.BISHOP, color));
        squares.put(new Position(file,6), new Piece(PieceType.BISHOP, color));
    }

    private void createQueen(PlayerColor color){
        int file = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(file,4), new Piece(PieceType.QUEEN, color));
    }

    private void createKing(PlayerColor color){
        int file = color == PlayerColor.WHITE? 1 : 8;
        squares.put(new Position(file,5), new Piece(PieceType.KING, color));
    }

    public boolean isOccupied(Position position){
        return squares.get(position) == null;
    }

    public String getPieceAt(Position position){
        if (!isOccupied(position))
                return null;
        return squares.get(position).getType();
    }

    public boolean movePieceTo(Position oldValue, Position newValue){
        return true;
    }
}
