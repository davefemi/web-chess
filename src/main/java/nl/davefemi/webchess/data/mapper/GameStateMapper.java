package nl.davefemi.webchess.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.GameStateDTO;
import nl.davefemi.webchess.data.entity.GameStateEntity;
import nl.davefemi.webchess.data.MoveRecordEntity;
import nl.davefemi.webchess.data.mapper.record.MoveRecordMapper;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.GameStatus;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.actions.MoveRecord;
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
        entity.setGamePhase(game.getStatus().phase().getPhase());
        entity.setWinner(game.getStatus().winner().isPresent()? game.getStatus().winner().get().getColor(): null);
        entity.setGameEndReason(game.getStatus().isFinished()? game.getStatus().reason().get().getReason() : null);
        String turn;
        try{
            turn = game.getColorToMove().getColor();
        } catch (NullPointerException | GameException e) {
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
        PieceColor colorToMove = entity.getColorToMove() == null ? null : PieceColor.fromString(entity.getColorToMove());
        return new Game(
                boardMapper.mapEntityToDomain(entity.getCurrentBoardContext(), lastMove, colorToMove),
                mapEntityToGameStatus(entity),
                colorToMove,
                moveHistory);
    }

    private GameStatus mapEntityToGameStatus(GameStateEntity entity){
        return switch (entity.getGamePhase()) {
            case "waiting" -> GameStatus.waiting();
            case "active" -> GameStatus.active();
            case "ended" -> switch (entity.getGameEndReason()) {
                case ("checkmate") -> GameStatus.checkmate(PieceColor.fromString(entity.getWinner()));
                case ("surrender") ->
                        GameStatus.surrender(PieceColor.getOpponent(PieceColor.fromString(entity.getWinner())));
                case ("stalemate") -> GameStatus.stalemate();
                default -> throw new IllegalArgumentException("No reason given");
            };
            default -> throw new IllegalArgumentException("No phase given");
        };
    }

    public GameStateDTO mapDomainToDTO(Game game, String turn){
        GameStateDTO dto = new GameStateDTO();
        dto.setColorToMove(turn);
        dto.setGamePhase(game.getStatus().phase().getPhase());
        dto.setWinner(game.getStatus().winner().isPresent()? game.getStatus().winner().get().getColor(): null);
        dto.setGameEndReason(game.getStatus().isFinished()? game.getStatus().reason().get().getReason() : null);
        game.getMoveHistory().forEach(m -> dto.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return dto;
    }
}
