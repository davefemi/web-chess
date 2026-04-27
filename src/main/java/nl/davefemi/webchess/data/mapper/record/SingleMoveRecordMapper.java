package nl.davefemi.webchess.data.mapper.record;

import nl.davefemi.webchess.data.dto.record.SingleMoveRecordData;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.record.SingleMoveRecord;
import org.springframework.stereotype.Component;

@Component
public class SingleMoveRecordMapper {

    protected SingleMoveRecord mapDataToDomain(SingleMoveRecordData data){
        return new SingleMoveRecord(new SingleMove(
                new Position(data.getOldPosFile(), data.getOldPosRank()),
                new Position(data.getNewPosFile(), data.getNewPosRank())),
                PieceColor.fromString(data.getPlayerColor()),
                PieceType.fromString(data.getMovedPieceType()),
                data.getMovedPieceId(),
                data.getCapturedPieceType() == null? null : PieceType.fromString(data.getCapturedPieceType()),
                data.getCapturedPieceId());
    }

    protected SingleMoveRecordData mapDomainToData(SingleMoveRecord record){
        SingleMoveRecordData dto = new SingleMoveRecordData();
        dto.setOldPosFile(record.move().from().file());
        dto.setOldPosRank(record.move().from().rank());
        dto.setNewPosFile(record.move().to().file());
        dto.setNewPosRank(record.move().to().rank());
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
