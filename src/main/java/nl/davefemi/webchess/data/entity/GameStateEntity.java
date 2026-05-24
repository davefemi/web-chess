package nl.davefemi.webchess.data.entity;

import lombok.Data;
import nl.davefemi.webchess.data.dto.record.MoveRecordData;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateEntity {
    private String nextTurn;
    private BoardContextEntity currentBoardContext;
    private boolean activeGame;
    private List<MoveRecordData> moveHistory = new ArrayList<>();
}
