package nl.davefemi.webchess.game.board;

final class PieceFactory {

    private PieceFactory(){
        throw new AssertionError("This class cannot be instantiated");
    }


    static boolean populateBoardWithPieces(Piece[] squares, IdGenerator pieceIdGenerator){
        if(squares.length != 128)
            throw new IllegalArgumentException("Array length must be exactly 128");
        for (PieceColor c : new PieceColor[]{PieceColor.WHITE, PieceColor.BLACK}){
            createPawns(squares, pieceIdGenerator, c);
            createRooks(squares, pieceIdGenerator, c);
            createKnights(squares, pieceIdGenerator, c);
            createBishops(squares, pieceIdGenerator, c);
            createQueen(squares, pieceIdGenerator, c);
            createKing(squares, pieceIdGenerator, c);
        }
        return true;
    }

    private static void createPawns(Piece[] squares, IdGenerator pieceIdGenerator, PieceColor color){
        int square =
                color == PieceColor.WHITE
                        ? 0x10
                        : 0x60;
        for (int i = 0; i<8; i++){
            squares[square+i] = new Piece(pieceIdGenerator.getNextId(), PieceType.PAWN, color);
        }
    }

    private static void createRooks(Piece[] squares, IdGenerator pieceIdGenerator, PieceColor color){
        int square =
                color == PieceColor.WHITE
                        ? 0x00
                        : 0x70;
        Piece queenSideRook = new Piece(pieceIdGenerator.getNextId(), PieceType.ROOK, color);
        Piece kingSideRook = new Piece(pieceIdGenerator.getNextId(), PieceType.ROOK, color);
        squares[square] = queenSideRook;
        squares[square+7] = kingSideRook;
    }

    private static void createKnights(Piece[] squares, IdGenerator pieceIdGenerator, PieceColor color){
        int square =
                color == PieceColor.WHITE
                        ? 0x01
                        : 0x71;
        squares[square] = new Piece(pieceIdGenerator.getNextId(), PieceType.KNIGHT, color);
        squares[square+5] = new Piece(pieceIdGenerator.getNextId(), PieceType.KNIGHT, color);
    }

    private static void createBishops(Piece[] squares, IdGenerator pieceIdGenerator, PieceColor color){
        int square =
                color == PieceColor.WHITE
                        ? 0x02
                        : 0x72;
        squares[square] =  new Piece(pieceIdGenerator.getNextId(), PieceType.BISHOP, color);
        squares[square + 3] = new Piece(pieceIdGenerator.getNextId(), PieceType.BISHOP, color);
    }

    private static void createQueen(Piece[] squares, IdGenerator pieceIdGenerator, PieceColor color){
        squares[color == PieceColor.WHITE
                ? 0x03
                : 0x73]
                = new Piece(pieceIdGenerator.getNextId(), PieceType.QUEEN, color);
    }

    private static void createKing(Piece[] squares, IdGenerator pieceIdGenerator, PieceColor color){
        squares[color == PieceColor.WHITE
                ? 0x04
                : 0x74]
                = new Piece(pieceIdGenerator.getNextId(), PieceType.KING, color);
    }
}