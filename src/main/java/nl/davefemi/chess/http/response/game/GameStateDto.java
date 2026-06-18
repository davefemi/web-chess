package nl.davefemi.chess.http.response.game;

import lombok.Data;
import nl.davefemi.chess.http.dto.MoveRecordDto;
import nl.davefemi.chess.http.dto.move.PositionPieceDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateDto {
    private String id;
    private int currentRound;
    private String colorToMove;
    private String gamePhase;
    private boolean inCheck;
    private String winner;
    private String gameEndReason;
    private List<PositionPieceDto> board = new ArrayList<>();
    private List<MoveRecordDto> moveHistory = new ArrayList<>();
}
