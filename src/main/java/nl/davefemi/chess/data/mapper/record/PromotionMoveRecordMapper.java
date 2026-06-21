package nl.davefemi.chess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.web.message.dto.record.PromotionMoveRecordDto;
import nl.davefemi.chess.data.entity.record.PromotionMoveRecordEntity;
import nl.davefemi.chess.data.mapper.move.PositionMapper;
import nl.davefemi.chess.gameplay.model.action.move.PromotionMove;
import nl.davefemi.chess.gameplay.model.action.move.SingleMove;
import nl.davefemi.chess.gameplay.model.board.PieceType;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.board.Square;
import nl.davefemi.chess.gameplay.model.action.record.PromotionMoveRecord;
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
                PieceType.fromString(data.getCapturedPieceType()),
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

    protected PromotionMoveRecordDto mapDomainToDTO(PromotionMoveRecord record){
        PromotionMoveRecordDto data = new PromotionMoveRecordDto();
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
