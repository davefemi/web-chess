package nl.davefemi.chess.http.dto.move;

import lombok.Data;
import nl.davefemi.chess.http.dto.MoveDto;

@Data
public class PromotionMoveDto implements MoveDto {
    private String from;
    private String to;
    private String newPieceType;
}
