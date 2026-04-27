package nl.davefemi.webchess.data.mapper.record;

import nl.davefemi.webchess.data.dto.record.CastlingMoveRecordData;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.record.CastlingMoveRecord;
import org.springframework.stereotype.Component;

@Component
public class CastlingMoveRecordMapper {

    protected CastlingMoveRecord mapDataToDomain(CastlingMoveRecordData data){
        return new CastlingMoveRecord(
                new CastlingMove(
                        new SingleMove(
                                new Position(data.getKingOldPosFile(), data.getKingOldPosRank()),
                                new Position(data.getKingNewPosFile(), data.getKingNewPosRank())),
                        new SingleMove(
                                new Position(data.getRookOldPosFile(), data.getRookOldPosRank()),
                                new Position(data.getRookNewPosFile(), data.getRookNewPosRank()
                        ))), PieceColor.fromString(data.getPlayerColor()), data.getKingId(), data.getRookId());
    }

    protected CastlingMoveRecordData mapDomainToData(CastlingMoveRecord record){
        CastlingMoveRecordData dto = new CastlingMoveRecordData();
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

}
