package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.record.CastlingMoveRecordDTO;
import nl.davefemi.webchess.data.entity.record.CastlingMoveRecordEntity;
import nl.davefemi.webchess.data.mapper.move.PositionMapper;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.PieceColor;
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
                                Square.fromFileAndRank(data.getKingOldPosFile(), data.getKingOldPosRank()),
                                Square.fromFileAndRank(data.getKingNewPosFile(), data.getKingNewPosRank())),
                        new SingleMove(
                                Square.fromFileAndRank(data.getRookOldPosFile(), data.getRookOldPosRank()),
                                Square.fromFileAndRank(data.getRookNewPosFile(), data.getRookNewPosRank()
                        ))), PieceColor.fromString(data.getPlayerColor()), data.getKingId(), data.getRookId());
    }

    protected CastlingMoveRecordEntity mapDomainToEntity(CastlingMoveRecord record){
        CastlingMoveRecordEntity dto = new CastlingMoveRecordEntity();
        dto.setKingOldPosFile(record.move().moveKing().from().file());
        dto.setKingOldPosRank(record.move().moveKing().from().rank());
        dto.setKingNewPosFile(record.move().moveKing().to().file());
        dto.setKingNewPosRank(record.move().moveKing().to().rank());
        dto.setRookOldPosFile(record.move().moveRook().from().file());
        dto.setRookOldPosRank(record.move().moveRook().from().rank());
        dto.setRookNewPosFile(record.move().moveRook().to().file());
        dto.setRookNewPosRank(record.move().moveRook().to().rank());
        dto.setPlayerColor(record.player_color().getColor());
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
        dto.setPlayerColor(record.player_color().getColor());
        dto.setKingId(record.kingId());
        dto.setRookId(record.rookId());
        return dto;
    }

}
