package nl.davefemi.chess.web.message.dto.record;

import lombok.Data;

@Data
public class PromotionMoveRecordDto implements MoveRecordDto {
    private String oldPos;
    private String newPos;
    private String playerColor;
    private String newPieceType;
    private Integer newPieceId;
    private String capturedPieceType;
    private Integer capturedPieceId;
}
