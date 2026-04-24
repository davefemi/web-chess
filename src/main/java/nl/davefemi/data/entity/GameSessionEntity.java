package nl.davefemi.data.entity;

import lombok.Data;
import nl.davefemi.data.dto.record.MoveRecordData;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.util.ArrayList;
import java.util.List;

@RedisHash(value = "game-session", timeToLive = 300)
@Data
public class GameSessionEntity {

    @Id
    private String gameId;
    private String nextTurn;
    private int nextPieceId;
    private BoardStateEntity boardState;
    private boolean activeGame;
    private List<PositionPieceEntity> capturedPieces = new ArrayList<>();
    private List<MoveRecordData> moveHistory = new ArrayList<>();
}
