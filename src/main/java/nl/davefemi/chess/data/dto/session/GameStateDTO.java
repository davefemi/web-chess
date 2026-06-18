package nl.davefemi.chess.data.dto.session;

import lombok.Data;
import nl.davefemi.chess.data.dto.MoveRecordDTO;
import nl.davefemi.chess.data.dto.move.PositionPieceDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateDTO {
    private String id;
    private int currentRound;
    private String colorToMove;
    private String gamePhase;
    private boolean inCheck;
    private String winner;
    private String gameEndReason;
    private List<PositionPieceDTO> board = new ArrayList<>();
    private List<MoveRecordDTO> moveHistory = new ArrayList<>();
}
