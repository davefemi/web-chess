package nl.davefemi.chess.web.dto.response.game;

import lombok.Data;
import nl.davefemi.chess.web.dto.record.MoveRecordDto;
import nl.davefemi.chess.web.dto.move.PositionPieceDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateDto {
    private int currentRound;
    private String colorToMove;
    private boolean inCheck;
    private List<PositionPieceDto> board = new ArrayList<>();
    private List<MoveRecordDto> moveHistory = new ArrayList<>();
}
