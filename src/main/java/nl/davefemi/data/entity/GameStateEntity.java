package nl.davefemi.data.entity;

import lombok.Data;
import nl.davefemi.data.dto.record.MoveRecordData;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameStateEntity {
    private String nextTurn;
    private int nextPieceId;
    private BoardStateEntity boardState;
    private boolean activeGame;
    private List<PositionPieceEntity> capturedPieces = new ArrayList<>();
    private List<MoveRecordData> moveHistory = new ArrayList<>();
}
