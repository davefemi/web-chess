package nl.davefemi.chess.web.dto.move;

import lombok.Data;

@Data
public class EnPassantMoveDto implements MoveDto {
    private String from;
    private String to;
}
