package nl.davefemi.webchess.data.dto;

import lombok.Data;
import nl.davefemi.webchess.data.MoveRecordDTO;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateDTO {
    private String colorToMove;
    private String gamePhase;
    private String winner;
    private String gameEndReason;
    private List<MoveRecordDTO> moveHistory = new ArrayList<>();
}
