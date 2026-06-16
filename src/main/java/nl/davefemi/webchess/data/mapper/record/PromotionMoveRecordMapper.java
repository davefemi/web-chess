package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.record.PromotionMoveRecordDTO;
import nl.davefemi.webchess.data.entity.record.PromotionMoveRecordEntity;
import nl.davefemi.webchess.data.mapper.move.PositionMapper;
import nl.davefemi.webchess.game.actions.move.PromotionMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.board.Square;
import nl.davefemi.webchess.game.actions.record.PromotionMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveRecordMapper {
    private final PositionMapper positionMapper;

    protected PromotionMoveRecord mapDataToDomain(PromotionMoveRecordEntity data){
        return new PromotionMoveRecord(
                new PromotionMove(new SingleMove(new Square(data.getOldPos()),
                        new Square(data.getNewPos())),
                        PieceType.fromString(data.getNewPieceType())),
                Color.fromString(data.getPlayerColor()),
                data.getNewPieceId(),
                PieceType.fromString(data.getNewPieceType()),
                data.getCapturedPieceId());
    }

    protected PromotionMoveRecordEntity mapDomainToEntity(PromotionMoveRecord record){
        PromotionMoveRecordEntity data = new PromotionMoveRecordEntity();
        data.setOldPos(record.move().move().from().value());
        data.setNewPos(record.move().move().to().value());
        data.setPlayerColor(record.playerColor().toString());
        data.setNewPieceType(record.move().newPieceType().toString());
        data.setNewPieceId(record.newPieceId());
        data.setCapturedPieceType(record.capturedPiece().toString());
        data.setCapturedPieceId(record.capturedPieceId());
        return data;
    }

    protected PromotionMoveRecordDTO mapDomainToDTO(PromotionMoveRecord record){
        PromotionMoveRecordDTO data = new PromotionMoveRecordDTO();
        data.setOldPos(positionMapper.mapDomainToDTO(record.move().move().from()));
        data.setNewPos(positionMapper.mapDomainToDTO(record.move().move().to()));
        data.setPlayerColor(record.playerColor().toString());
        data.setNewPieceType(record.move().newPieceType().toString());
        data.setNewPieceId(record.newPieceId());
        data.setCapturedPieceType(record.capturedPiece().toString());
        data.setCapturedPieceId(record.capturedPieceId());
        return data;
    }

}
