package nl.davefemi.chess.http.dto.record;

import lombok.Data;
import nl.davefemi.chess.http.dto.MoveRecordDto;

@Data
public class PromotionMoveRecordDto implements MoveRecordDto {
    private String oldPos;
    private String newPos;
    private String playerColor;
    private String newPieceType;
    private int newPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
