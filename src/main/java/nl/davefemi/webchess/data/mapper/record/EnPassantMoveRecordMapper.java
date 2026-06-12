package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.record.EnPassantMoveRecordDTO;
import nl.davefemi.webchess.data.entity.record.EnPassantMoveRecordEntity;
import nl.davefemi.webchess.data.mapper.move.PositionMapper;
import nl.davefemi.webchess.game.actions.move.EnPassantMove;
import nl.davefemi.webchess.game.actions.record.EnPassantMoveRecord;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.Square;
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
        dto.setPlayerColor(record.playerColor().getColor());
        dto.setMovedPieceType(record.movedPiece().getLabel());
        dto.setMovedPieceId(record.movedPieceId());
        if (record.capturedPiece() != null){
            dto.setCapturedPieceType(record.capturedPiece().getLabel());
            dto.setCapturedPieceId(record.capturedPieceId());
        }
        return dto;
    }

    protected EnPassantMoveRecordDTO mapDomainToDTO(EnPassantMoveRecord record){
        EnPassantMoveRecordDTO dto = new EnPassantMoveRecordDTO();
        dto.setOldPos(positionMapper.mapDomainToDTO(record.move().from()));
        dto.setNewPos(positionMapper.mapDomainToDTO(record.move().to()));
        dto.setPlayerColor(record.playerColor().getColor());
        dto.setMovedPieceType(record.movedPiece().getLabel());
        dto.setMovedPieceId(record.movedPieceId());
        if (record.capturedPiece() != null){
            dto.setCapturedPieceType(record.capturedPiece().getLabel());
            dto.setCapturedPieceId(record.capturedPieceId());
        }
        return dto;
    }
}
