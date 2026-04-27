package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.record.PromotionMoveRecordData;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.record.PromotionMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveRecordMapper {

    protected PromotionMoveRecord mapDataToDomain(PromotionMoveRecordData data){
        return new PromotionMoveRecord(
                new PromotionMove(new SingleMove(new Position(data.getOldPosFile(), data.getOldPosRank()),
                        new Position(data.getNewPosFile(), data.getNewPosRank())),
                        PieceType.fromString(data.getNewPieceType())),
                PieceColor.fromString(data.getPlayerColor()),
                PieceType.fromString(data.getNewPieceType()),
                data.getOldPieceId());
    }

    protected PromotionMoveRecordData mapDomainToData(PromotionMoveRecord record){
        PromotionMoveRecordData data = new PromotionMoveRecordData();
        data.setOldPosFile(record.move().move().from().file());
        data.setOldPosRank(record.move().move().from().rank());
        data.setNewPosFile(record.move().move().to().file());
        data.setNewPosRank(record.move().move().to().rank());
        data.setPlayerColor(record.playerColor().getColor());
        data.setNewPieceType(record.newPiece().getLabel());
        return data;
    }


}
