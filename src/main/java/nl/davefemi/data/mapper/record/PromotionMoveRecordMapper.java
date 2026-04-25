package nl.davefemi.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.record.PromotionMoveRecordData;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.game.board.Position;
import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.game.record.PromotionMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveRecordMapper {

    protected PromotionMoveRecord mapDataToDomain(PromotionMoveRecordData data){
        return new PromotionMoveRecord(
                new PromotionMove(new Position(data.getPosFile(), data.getPosRank()),
                        PieceType.fromString(data.getPieceType())),
                PieceColor.fromString(data.getPlayerColor()),
                PieceType.fromString(data.getPieceType()),
                data.getPieceId());
    }

    protected PromotionMoveRecordData mapDomainToData(PromotionMoveRecord record){
        PromotionMoveRecordData data = new PromotionMoveRecordData();
        data.setPosFile(record.move().position().file());
        data.setPosRank(record.move().position().rank());
        data.setPlayerColor(record.playerColor().getColor());
        data.setPieceType(record.newPiece().getLabel());
        data.setPieceId(record.pieceId());
        return data;
    }


}
