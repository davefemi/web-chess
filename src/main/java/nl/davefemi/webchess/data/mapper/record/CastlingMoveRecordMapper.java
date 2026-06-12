package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.record.CastlingMoveRecordDTO;
import nl.davefemi.webchess.data.entity.record.CastlingMoveRecordEntity;
import nl.davefemi.webchess.data.mapper.move.PositionMapper;
import nl.davefemi.webchess.game.actions.move.CastlingMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.board.Square;
import nl.davefemi.webchess.game.actions.record.CastlingMoveRecord;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CastlingMoveRecordMapper {
    private final PositionMapper positionMapper;

    protected CastlingMoveRecord mapDataToDomain(CastlingMoveRecordEntity data){
        return new CastlingMoveRecord(
                new CastlingMove(
                        new SingleMove(
                                new Square(data.getKingOldPos()),
                                new Square(data.getKingNewPos())),
                        new SingleMove(
                                new Square(data.getRookOldPos()),
                                new Square(data.getRookNewPos())
                        )),
                Color.fromString(data.getPlayerColor()), data.getKingId(), data.getRookId());
    }

    protected CastlingMoveRecordEntity mapDomainToEntity(CastlingMoveRecord record){
        CastlingMoveRecordEntity dto = new CastlingMoveRecordEntity();
        dto.setKingOldPos(record.move().moveKing().from().value());
        dto.setKingNewPos(record.move().moveKing().to().value());
        dto.setRookOldPos(record.move().moveRook().from().value());
        dto.setRookNewPos(record.move().moveRook().to().value());
        dto.setPlayerColor(record.playerColor().getColor());
        dto.setKingId(record.kingId());
        dto.setRookId(record.rookId());
        return dto;
    }

    protected CastlingMoveRecordDTO mapDomainToDTO(CastlingMoveRecord record){
        CastlingMoveRecordDTO dto = new CastlingMoveRecordDTO();
        SingleMove kingMove = record.move().moveKing();
        SingleMove rookMove = record.move().moveRook();
        dto.setKingOldPos(positionMapper.mapDomainToDTO(kingMove.from()));
        dto.setKingNewPos(positionMapper.mapDomainToDTO(kingMove.to()));
        dto.setRookOldPos(positionMapper.mapDomainToDTO(rookMove.from()));
        dto.setRookNewPos(positionMapper.mapDomainToDTO(rookMove.to()));
        dto.setPlayerColor(record.playerColor().getColor());
        dto.setKingId(record.kingId());
        dto.setRookId(record.rookId());
        return dto;
    }

}
