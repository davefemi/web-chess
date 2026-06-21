package nl.davefemi.chess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.web.message.dto.record.CastlingMoveRecordDto;
import nl.davefemi.chess.data.entity.record.CastlingMoveRecordEntity;
import nl.davefemi.chess.data.mapper.move.PositionMapper;
import nl.davefemi.chess.gameplay.model.action.move.CastlingMove;
import nl.davefemi.chess.gameplay.model.action.move.SingleMove;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.board.Square;
import nl.davefemi.chess.gameplay.model.action.record.CastlingMoveRecord;
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
        dto.setPlayerColor(record.playerColor().toString());
        dto.setKingId(record.kingId());
        dto.setRookId(record.rookId());
        return dto;
    }

    protected CastlingMoveRecordDto mapDomainToDTO(CastlingMoveRecord record){
        CastlingMoveRecordDto dto = new CastlingMoveRecordDto();
        SingleMove kingMove = record.move().moveKing();
        SingleMove rookMove = record.move().moveRook();
        dto.setKingOldPos(positionMapper.mapDomainToDTO(kingMove.from()));
        dto.setKingNewPos(positionMapper.mapDomainToDTO(kingMove.to()));
        dto.setRookOldPos(positionMapper.mapDomainToDTO(rookMove.from()));
        dto.setRookNewPos(positionMapper.mapDomainToDTO(rookMove.to()));
        dto.setPlayerColor(record.playerColor().toString());
        dto.setKingId(record.kingId());
        dto.setRookId(record.rookId());
        return dto;
    }

}
