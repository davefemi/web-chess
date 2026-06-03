package nl.davefemi.webchess.data.entity;

import lombok.Data;
import nl.davefemi.webchess.data.MoveRecordEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateEntity {
    private String colorToMove;
    private BoardContextEntity currentBoardContext;
    private String gamePhase;
    private String winner;
    private String gameEndReason;
    private List<MoveRecordEntity> moveHistory = new ArrayList<>();
}
