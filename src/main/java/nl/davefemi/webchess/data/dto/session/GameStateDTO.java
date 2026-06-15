package nl.davefemi.webchess.data.dto.session;

import lombok.Data;
import nl.davefemi.webchess.data.dto.MoveRecordDTO;
import nl.davefemi.webchess.data.dto.move.PositionPieceDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateDTO {
    private int currentRound;
    private String colorToMove;
    private String gamePhase;
    private boolean inCheck;
    private String winner;
    private String gameEndReason;
    private List<PositionPieceDTO> board = new ArrayList<>();
    private List<MoveRecordDTO> moveHistory = new ArrayList<>();
}
