package nl.davefemi.data.dto;

import lombok.Data;
import nl.davefemi.data.dto.move.record.MoveRecordData;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameSessionDTO {
    private String gameId;
    private String nextTurn;
    private boolean activeGame;
    private String message;
    private List<MoveRecordData> moveHistory = new ArrayList<>();
}
