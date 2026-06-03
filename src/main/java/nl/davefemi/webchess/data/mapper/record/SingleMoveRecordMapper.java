package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.record.SingleMoveRecordDTO;
import nl.davefemi.webchess.data.entity.record.SingleMoveRecordEntity;
import nl.davefemi.webchess.data.mapper.move.PositionMapper;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.Square;
import nl.davefemi.webchess.game.actions.record.SingleMoveRecord;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SingleMoveRecordMapper {
    private final PositionMapper positionMapper;

    protected SingleMoveRecord mapDataToDomain(SingleMoveRecordEntity data){
        return new SingleMoveRecord(new SingleMove(
                new Square(data.getOldPos()),
                new Square(data.getNewPos())),
                PieceColor.fromString(data.getPlayerColor()),
                PieceType.fromString(data.getMovedPieceType()),
                data.getMovedPieceId(),
                data.getCapturedPieceType() == null? null : PieceType.fromString(data.getCapturedPieceType()),
                data.getCapturedPieceId());
    }

    protected SingleMoveRecordEntity mapDomainToEntity(SingleMoveRecord record){
        SingleMoveRecordEntity dto = new SingleMoveRecordEntity();
        dto.setOldPos(record.move().from().value());
        dto.setNewPos(record.move().to().value());
        dto.setPlayerColor(record.color().getColor());
        dto.setMovedPieceType(record.movedPiece().getLabel());
        dto.setMovedPieceId(record.movedPieceId());
        if (record.capturedPiece() != null){
            dto.setCapturedPieceType(record.capturedPiece().getLabel());
            dto.setCapturedPieceId(record.capturedPieceId());
        }
        return dto;
    }

    protected SingleMoveRecordDTO mapDomainToDTO(SingleMoveRecord record){
        SingleMoveRecordDTO dto = new SingleMoveRecordDTO();
        dto.setOldPos(positionMapper.mapDomainToDTO(record.move().from()));
        dto.setNewPos(positionMapper.mapDomainToDTO(record.move().to()));
        dto.setPlayerColor(record.color().getColor());
        dto.setMovedPieceType(record.movedPiece().getLabel());
        dto.setMovedPieceId(record.movedPieceId());
        if (record.capturedPiece() != null){
            dto.setCapturedPieceType(record.capturedPiece().getLabel());
            dto.setCapturedPieceId(record.capturedPieceId());
        }
        return dto;
    }
}
