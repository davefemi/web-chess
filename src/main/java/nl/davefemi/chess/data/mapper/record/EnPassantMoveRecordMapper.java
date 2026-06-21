package nl.davefemi.chess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.web.message.dto.record.EnPassantMoveRecordDto;
import nl.davefemi.chess.data.entity.record.EnPassantMoveRecordEntity;
import nl.davefemi.chess.data.mapper.move.PositionMapper;
import nl.davefemi.chess.gameplay.model.action.move.EnPassantMove;
import nl.davefemi.chess.gameplay.model.action.record.EnPassantMoveRecord;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.board.PieceType;
import nl.davefemi.chess.gameplay.model.board.Square;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EnPassantMoveRecordMapper {
    private final PositionMapper positionMapper;

    protected EnPassantMoveRecord mapDataToDomain(EnPassantMoveRecordEntity data){
        return new EnPassantMoveRecord(new EnPassantMove(
                new Square(data.getOldPos()),
                new Square(data.getNewPos())),
                Color.fromString(data.getPlayerColor()),
                PieceType.fromString(data.getMovedPieceType()),
                data.getMovedPieceId(),
                data.getCapturedPieceType() == null? null : PieceType.fromString(data.getCapturedPieceType()),
                data.getCapturedPieceId());
    }

    protected EnPassantMoveRecordEntity mapDomainToEntity(EnPassantMoveRecord record){
        EnPassantMoveRecordEntity dto = new EnPassantMoveRecordEntity();
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

    protected EnPassantMoveRecordDto mapDomainToDTO(EnPassantMoveRecord record){
        EnPassantMoveRecordDto dto = new EnPassantMoveRecordDto();
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
