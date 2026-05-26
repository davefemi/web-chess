package nl.davefemi.webchess.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.GameStateDTO;
import nl.davefemi.webchess.data.entity.GameStateEntity;
import nl.davefemi.webchess.data.entity.record.MoveRecordEntity;
import nl.davefemi.webchess.data.mapper.record.MoveRecordMapper;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.record.MoveRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameStateMapper {
    private final BoardMapper boardMapper;
    private final MoveRecordMapper moveRecordMapper;

    public GameStateEntity mapDomainToEntity(Game game) throws BoardException {
        GameStateEntity entity = new GameStateEntity();
        entity.setCurrentBoardContext(boardMapper.mapDomainToEntity(game.getCurrentBoardContext()));
        entity.setActiveGame(game.isGameActive());
        String turn;
        try{
            turn = game.getColorToMove().getColor();
        } catch (GameException e) {
            turn = null;
        }
        entity.setColorToMove(turn);
        game.getMoveHistory().forEach(m -> entity.getMoveHistory().add(moveRecordMapper.mapDomainToEntity(m)));
        return entity;
    }

    public Game mapEntityToDomain(GameStateEntity entity) throws BoardException {
        List<MoveRecord> moveHistory = new ArrayList<>();
        for (MoveRecordEntity d : entity.getMoveHistory()) {
            moveHistory.add(moveRecordMapper.mapDataToDomain(d));
        }
        MoveRecord lastMove = moveHistory.isEmpty() ? null : moveHistory.getLast();
        return new Game(
                boardMapper.mapEntityToDomain(entity.getCurrentBoardContext(), lastMove, PieceColor.fromString(entity.getColorToMove())),
                entity.isActiveGame(),
                PieceColor.fromString(entity.getColorToMove()),
                moveHistory);
    }

    public GameStateDTO mapDomainToDTO(Game game, String turn){
        GameStateDTO dto = new GameStateDTO();
        dto.setColorToMove(turn);
        dto.setActiveGame(game.isGameActive());
        game.getMoveHistory().forEach(m -> dto.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return dto;
    }
}
