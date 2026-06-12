package nl.davefemi.webchess.game.board;

import nl.davefemi.webchess.game.Color;

import static nl.davefemi.webchess.game.Color.*;
import static nl.davefemi.webchess.game.board.PieceType.*;

final class PieceFactory {

    private PieceFactory(){
        throw new AssertionError("This class cannot be instantiated");
    }


    static boolean populateBoardWithPieces(Piece[] squares, IdGenerator pieceIdGenerator){
        if(squares.length != 128)
            throw new IllegalArgumentException("Array length must be exactly 128");
        for (Color c : new Color[]{WHITE, BLACK}){
            createPawns(squares, pieceIdGenerator, c);
            createRooks(squares, pieceIdGenerator, c);
            createKnights(squares, pieceIdGenerator, c);
            createBishops(squares, pieceIdGenerator, c);
            createQueen(squares, pieceIdGenerator, c);
            createKing(squares, pieceIdGenerator, c);
        }
        return true;
    }

    private static void createPawns(Piece[] squares, IdGenerator pieceIdGenerator, Color color){
        int square =
                color == WHITE
                        ? 0x10
                        : 0x60;
        for (int i = 0; i<8; i++){
            squares[square+i] = new Piece(pieceIdGenerator.getNextId(), PAWN, color);
        }
    }

    private static void createRooks(Piece[] squares, IdGenerator pieceIdGenerator, Color color){
        int square =
                color == WHITE
                        ? 0x00
                        : 0x70;
        Piece queenSideRook = new Piece(pieceIdGenerator.getNextId(), ROOK, color);
        Piece kingSideRook = new Piece(pieceIdGenerator.getNextId(), ROOK, color);
        squares[square] = queenSideRook;
        squares[square+7] = kingSideRook;
    }

    private static void createKnights(Piece[] squares, IdGenerator pieceIdGenerator, Color color){
        int square =
                color == WHITE
                        ? 0x01
                        : 0x71;
        squares[square] = new Piece(pieceIdGenerator.getNextId(), KNIGHT, color);
        squares[square+5] = new Piece(pieceIdGenerator.getNextId(), KNIGHT, color);
    }

    private static void createBishops(Piece[] squares, IdGenerator pieceIdGenerator, Color color){
        int square =
                color == WHITE
                        ? 0x02
                        : 0x72;
        squares[square] =  new Piece(pieceIdGenerator.getNextId(), BISHOP, color);
        squares[square + 3] = new Piece(pieceIdGenerator.getNextId(), BISHOP, color);
    }

    private static void createQueen(Piece[] squares, IdGenerator pieceIdGenerator, Color color){
        squares[color == WHITE
                ? 0x03
                : 0x73]
                = new Piece(pieceIdGenerator.getNextId(), QUEEN, color);
    }

    private static void createKing(Piece[] squares, IdGenerator pieceIdGenerator, Color color){
        squares[color == WHITE
                ? 0x04
                : 0x74]
                = new Piece(pieceIdGenerator.getNextId(), KING, color);
    }
}