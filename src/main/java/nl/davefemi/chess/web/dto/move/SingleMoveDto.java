package nl.davefemi.chess.web.dto.move;

import lombok.Data;

@Data
public class SingleMoveDto implements MoveDto {
    private String from;
    private String to;
}
