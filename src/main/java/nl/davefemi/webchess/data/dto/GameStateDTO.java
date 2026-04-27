package nl.davefemi.webchess.data.dto;

import lombok.Data;
import nl.davefemi.webchess.data.dto.record.MoveRecordData;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateDTO {
    private String nextTurn;
    private boolean activeGame;
    private List<MoveRecordData> moveHistory = new ArrayList<>();
}
