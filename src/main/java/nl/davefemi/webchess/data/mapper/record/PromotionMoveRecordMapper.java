package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.record.PromotionMoveRecordDTO;
import nl.davefemi.webchess.data.entity.record.PromotionMoveRecordEntity;
import nl.davefemi.webchess.data.mapper.move.PositionMapper;
import nl.davefemi.webchess.game.actions.move.PromotionMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.Square;
import nl.davefemi.webchess.game.actions.record.PromotionMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveRecordMapper {
    private final PositionMapper positionMapper;

    protected PromotionMoveRecord mapDataToDomain(PromotionMoveRecordEntity data){
        return new PromotionMoveRecord(
                new PromotionMove(new SingleMove(Square.fromFileAndRank(data.getOldPosFile(), data.getOldPosRank()),
                        Square.fromFileAndRank(data.getNewPosFile(), data.getNewPosRank())),
                        PieceType.fromString(data.getNewPieceType())),
                PieceColor.fromString(data.getPlayerColor()),
                PieceType.fromString(data.getNewPieceType()),
                data.getOldPieceId(),
                PieceType.fromString(data.getCapturedPieceType()),
                data.getCapturedPieceId());
    }

    protected PromotionMoveRecordEntity mapDomainToEntity(PromotionMoveRecord record){
        PromotionMoveRecordEntity data = new PromotionMoveRecordEntity();
        data.setOldPosFile(record.move().move().from().file());
        data.setOldPosRank(record.move().move().from().rank());
        data.setNewPosFile(record.move().move().to().file());
        data.setNewPosRank(record.move().move().to().rank());
        data.setPlayerColor(record.playerColor().getColor());
        data.setNewPieceType(record.newPiece().getLabel());
        data.setNewPieceId(record.pieceId());
        data.setCapturedPieceType(record.capturedPiece().getLabel());
        data.setCapturedPieceId(record.capturedPieceId());
        return data;
    }

    protected PromotionMoveRecordDTO mapDomainToDTO(PromotionMoveRecord record){
        PromotionMoveRecordDTO data = new PromotionMoveRecordDTO();
        data.setOldPos(positionMapper.mapDomainToDTO(record.move().move().from()));
        data.setNewPos(positionMapper.mapDomainToDTO(record.move().move().to()));
        data.setPlayerColor(record.playerColor().getColor());
        data.setNewPieceType(record.newPiece().getLabel());
        data.setNewPieceId(record.pieceId());
        data.setCapturedPieceType(record.capturedPiece().getLabel());
        data.setCapturedPieceId(record.capturedPieceId());
        return data;
    }

}
