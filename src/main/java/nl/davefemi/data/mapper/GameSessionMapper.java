package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.GameSessionDTO;
import nl.davefemi.data.dto.record.MoveRecordData;
import nl.davefemi.data.entity.GameSessionEntity;
import nl.davefemi.data.entity.PositionPieceEntity;
import nl.davefemi.data.mapper.record.MoveRecordMapper;
import nl.davefemi.domain.board.Piece;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.domain.game.Game;
import nl.davefemi.domain.game.actions.record.MoveRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GameSessionMapper {
    private final MoveRecordMapper moveRecordMapper;
    private final BoardMapper boardMapper;
    private final PositionPieceMapper positionPieceMapper;

    public GameSessionEntity mapDomainToEntity(Game game, String turn){
        GameSessionEntity session = new GameSessionEntity();
        session.setGameId(game.getGameId().toString());
        session.setBoardState(boardMapper.mapDomainToEntity(game.getGameId(), game.getCopyOfBoard()));
        for (Piece p: game.getCapturedPieces()){
            session.getCapturedPieces().add(positionPieceMapper.mapDomainToEntity(p));
        }
        session.setNextPieceId(game.getLatestPieceId());
        session.setActiveGame(game.isGameActive());
        session.setNextTurn(turn);
        game.getMoveHistory().forEach(m -> session.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return session;
    }

    public GameSessionDTO mapEntityToDTO(GameSessionEntity session, String message){
        GameSessionDTO dto = new GameSessionDTO();
        dto.setGameId(session.getGameId());
        dto.setNextTurn(session.getNextTurn());
        dto.setActiveGame(session.isActiveGame());
        dto.setMessage(message);
        dto.getMoveHistory().addAll(session.getMoveHistory());
        return dto;
    }

    public Game mapEntityToDomain(GameSessionEntity entity){
        List<MoveRecord> moveHistory = new ArrayList<>();
        List<Piece> capturedPiece = new ArrayList<>();
        for (MoveRecordData d : entity.getMoveHistory()) {
            moveHistory.add(moveRecordMapper.mapDataToDomain(d));
        }
        for (PositionPieceEntity e : entity.getCapturedPieces()) {
            capturedPiece.add(positionPieceMapper.mapEntityToPiece(e));
        }
        return new Game(
                UUID.fromString(entity.getGameId()),
                        entity.getNextPieceId(),
                        boardMapper.mapEntityToDomain(entity.getBoardState()),
                                entity.isActiveGame(),
                                PlayerColor.fromString(entity.getNextTurn()),
                                moveHistory,
                                capturedPiece);
    }
}
