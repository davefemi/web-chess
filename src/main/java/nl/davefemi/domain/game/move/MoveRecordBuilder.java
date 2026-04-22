package nl.davefemi.domain.game.move;

public final class MoveRecordBuilder {
    public static MoveRecord duplicateMoveRecord(MoveRecord o){
        return new MoveRecord(o.move(), o.color(), o.movedPiece(), o.capturedPiece(), o.upForPromotion());
    }
}
