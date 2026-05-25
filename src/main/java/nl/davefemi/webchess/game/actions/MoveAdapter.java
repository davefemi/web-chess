package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.Square;

public final class MoveAdapter {

    public static Move to0x88(Move move){
        if (move instanceof CastlingMove m){
            return toCastlingMove0x88(m);
        }
        if (move instanceof PromotionMove m){
            return toPromotionMove0x88(m);
        }
        SingleMove m = (SingleMove) move;
        return toSingleMove0x88(m);
    }

    private static CastlingMove0x88 toCastlingMove0x88(CastlingMove move){
        SingleMove0x88 moveKing = toSingleMove0x88(move.moveKing());
        SingleMove0x88 moveRook = toSingleMove0x88(move.moveRook());
        return new CastlingMove0x88(moveKing, moveRook);
    }

    private static PromotionMove0x88 toPromotionMove0x88(PromotionMove move){
        return new PromotionMove0x88(toSingleMove0x88(move.move()), move.newPieceType());
    }

    private static SingleMove0x88 toSingleMove0x88(SingleMove move){
        return new SingleMove0x88(Square.fromFileAndRank(move.from().file()-1, move.from().rank()-1),
                Square.fromFileAndRank(move.to().file()-1, move.to().rank()-1));
    }
}
