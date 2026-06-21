package nl.davefemi.chess.web.message.dto.move;

import lombok.Data;

@Data
public class SingleMoveDto implements MoveDto {
    private String from;
    private String to;
}
