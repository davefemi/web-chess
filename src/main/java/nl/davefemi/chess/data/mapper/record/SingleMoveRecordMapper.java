package nl.davefemi.chess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.http.dto.record.SingleMoveRecordDto;
import nl.davefemi.chess.data.entity.record.SingleMoveRecordEntity;
import nl.davefemi.chess.data.mapper.move.PositionMapper;
import nl.davefemi.chess.play.model.actions.move.SingleMove;
import nl.davefemi.chess.play.model.board.PieceType;
import nl.davefemi.chess.play.model.game.Color;
import nl.davefemi.chess.play.model.board.Square;
import nl.davefemi.chess.play.model.actions.record.SingleMoveRecord;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SingleMoveRecordMapper {
    private final PositionMapper positionMapper;

    protected SingleMoveRecord mapDataToDomain(SingleMoveRecordEntity data){
        return new SingleMoveRecord(new SingleMove(
                new Square(data.getOldPos()),
                new Square(data.getNewPos())),
                Color.fromString(data.getPlayerColor()),
                PieceType.fromString(data.getMovedPieceType()),
                data.getMovedPieceId(),
                data.getCapturedPieceType() == null? null : PieceType.fromString(data.getCapturedPieceType()),
                data.getCapturedPieceId());
    }

    protected SingleMoveRecordEntity mapDomainToEntity(SingleMoveRecord record){
        SingleMoveRecordEntity dto = new SingleMoveRecordEntity();
        dto.setOldPos(record.move().from().value());
        dto.setNewPos(record.move().to().value());
        dto.setPlayerColor(record.playerColor().toString());
        dto.setMovedPieceType(record.movedPiece().toString());
        dto.setMovedPieceId(record.movedPieceId());
        if (record.capturedPiece() != null){
            dto.setCapturedPieceType(record.capturedPiece().toString());
            dto.setCapturedPieceId(record.capturedPieceId());
        }
        return dto;
    }

    protected SingleMoveRecordDto mapDomainToDTO(SingleMoveRecord record){
        SingleMoveRecordDto dto = new SingleMoveRecordDto();
        dto.setOldPos(positionMapper.mapDomainToDTO(record.move().from()));
        dto.setNewPos(positionMapper.mapDomainToDTO(record.move().to()));
        dto.setPlayerColor(record.playerColor().toString());
        dto.setMovedPieceType(record.movedPiece().toString());
        dto.setMovedPieceId(record.movedPieceId());
        if (record.capturedPiece() != null){
            dto.setCapturedPieceType(record.capturedPiece().toString());
            dto.setCapturedPieceId(record.capturedPieceId());
        }
        return dto;
    }
}
