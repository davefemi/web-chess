package nl.davefemi.data.mapper.move.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.move.record.PromotionMoveRecordData;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.actions.move.PromotionMove;
import nl.davefemi.domain.game.actions.record.PromotionMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveRecordMapper {

    protected PromotionMoveRecord mapDataToDomain(PromotionMoveRecordData data){
        return new PromotionMoveRecord(
                new PromotionMove(new Position(data.getPosFile(), data.getPosRank()),
                        PieceType.fromString(data.getPieceType())),
                PlayerColor.fromString(data.getPlayerColor()),
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
