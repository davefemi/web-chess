package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.GameStateDTO;
import nl.davefemi.data.dto.record.MoveRecordData;
import nl.davefemi.data.entity.GameStateEntity;
import nl.davefemi.data.entity.PositionPieceEntity;
import nl.davefemi.data.mapper.move.PositionPieceMapper;
import nl.davefemi.data.mapper.record.MoveRecordMapper;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.game.Game;
import nl.davefemi.game.board.Piece;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.game.record.MoveRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameStateMapper {
    private final BoardMapper boardMapper;
    private final PositionPieceMapper positionPieceMapper;
    private final MoveRecordMapper moveRecordMapper;

    public GameStateEntity mapDomainToEntity(Game game){
        GameStateEntity entity = new GameStateEntity();
        entity.setBoardState(boardMapper.mapDomainToEntity(game.getCopyOfBoard()));
        for (Piece p: game.getCapturedPieces()){
            entity.getCapturedPieces().add(positionPieceMapper.mapDomainToEntity(p));
        }
        entity.setNextPieceId(game.getLatestPieceId());
        entity.setActiveGame(game.isGameActive());
        String turn;
        try{
            turn = game.getPlayerTurn().getColor();
        } catch (GameException e) {
            turn = null;
        }
        entity.setNextTurn(turn);
        game.getMoveHistory().forEach(m -> entity.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return entity;
    }

    public Game mapEntityToDomain(GameStateEntity entity) throws BoardException {
        List<MoveRecord> moveHistory = new ArrayList<>();
        List<Piece> capturedPiece = new ArrayList<>();
        for (MoveRecordData d : entity.getMoveHistory()) {
            moveHistory.add(moveRecordMapper.mapDataToDomain(d));
        }
        for (PositionPieceEntity e : entity.getCapturedPieces())
            capturedPiece.add(positionPieceMapper.mapEntityToPiece(e));
        return new Game(
                entity.getNextPieceId(),
                boardMapper.mapEntityToDomain(entity.getBoardState()),
                entity.isActiveGame(),
                PieceColor.fromString(entity.getNextTurn()),
                moveHistory,
                capturedPiece);
    }

    public GameStateDTO mapDomainToDTO(Game game, String turn){
        GameStateDTO dto = new GameStateDTO();
        dto.setNextTurn(turn);
        dto.setActiveGame(game.isGameActive());
        game.getMoveHistory().forEach(m -> dto.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return dto;
    }
}
