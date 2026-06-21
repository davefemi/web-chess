package nl.davefemi.chess.web.dto.response.game;

import lombok.Data;
import nl.davefemi.chess.web.dto.move.PositionPieceDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDto {
    private List<PositionPieceDto> board = new ArrayList<>();
}
