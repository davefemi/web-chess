package nl.davefemi.chess.data.entity.session;

import lombok.Data;
import nl.davefemi.chess.data.entity.record.MoveRecordEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateEntity {
    private String id;
    private int currentRound;
    private String colorToMove;
    private BoardContextEntity currentBoardContext;
    private String gamePhase;
    private boolean inCheck;
    private String winner;
    private String gameEndReason;
    private List<MoveRecordEntity> moveHistory = new ArrayList<>();
}
