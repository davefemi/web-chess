package nl.davefemi.webchess.data.mapper.session;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.session.GameStateDTO;
import nl.davefemi.webchess.data.entity.session.GameStateEntity;
import nl.davefemi.webchess.data.entity.MoveRecordEntity;
import nl.davefemi.webchess.data.mapper.move.PieceMapper;
import nl.davefemi.webchess.data.mapper.record.MoveRecordMapper;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.GameStatus;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.board.Piece;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameStateMapper {
    private final BoardMapper boardMapper;
    private final MoveRecordMapper moveRecordMapper;
    private final PieceMapper positionPieceMapper;

    public GameStateEntity mapDomainToEntity(Game game) throws BoardException {
        GameStateEntity entity = new GameStateEntity();
        entity.setCurrentRound(game.getCurrentRound());
        entity.setCurrentBoardContext(boardMapper.mapDomainToEntity(game.getGameBoardContext()));
        entity.setGamePhase(game.getStatus().phase().getPhase());
        entity.setInCheck(game.isInCheck());
        entity.setWinner(game.getStatus().winner().isPresent()? game.getStatus().winner().get().getColor(): null);
        entity.setGameEndReason(game.getStatus().isFinished()? game.getStatus().reason().get().getReason() : null);
        String turn;
        try{
            turn = game.getSideToMove().getColor();
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
        Color colorToMove = entity.getColorToMove() == null ? null : Color.fromString(entity.getColorToMove());
        return new Game(
                entity.getCurrentRound(),
                boardMapper.mapEntityToDomain(entity.getCurrentBoardContext(), lastMove, colorToMove),
                mapEntityToGameStatus(entity),
                colorToMove, entity.isInCheck(),
                moveHistory);
    }

    private GameStatus mapEntityToGameStatus(GameStateEntity entity){
        return switch (entity.getGamePhase()) {
            case "waiting" -> GameStatus.waiting();
            case "active" -> GameStatus.active();
            case "ended" -> switch (entity.getGameEndReason()) {
                case ("checkmate") -> GameStatus.checkmate(Color.fromString(entity.getWinner()));
                case ("surrender") ->
                        GameStatus.surrender(Color.getOpponent(Color.fromString(entity.getWinner())));
                case ("stalemate") -> GameStatus.stalemate();
                default -> throw new IllegalArgumentException("No reason given");
            };
            default -> throw new IllegalArgumentException("No phase given");
        };
    }

    public GameStateDTO mapDomainToDTO(Game game, String turn) throws BoardException {
        GameStateDTO dto = new GameStateDTO();
        dto.setCurrentRound(game.getCurrentRound());
        dto.setColorToMove(turn);
        dto.setGamePhase(game.getStatus().phase().getPhase());
        dto.setInCheck(game.isInCheck());
        dto.setWinner(game.getStatus().winner().isPresent()? game.getStatus().winner().get().getColor(): null);
        dto.setGameEndReason(game.getStatus().isFinished()? game.getStatus().reason().get().getReason() : null);
        for (Piece p : game.getGameBoardContext().getCopyOfBoard().getPieces()){
            dto.getBoard().add(positionPieceMapper.mapDomainToDTO(
                    game.getGameBoardContext().getCopyOfBoard().getPositionById(p.id()), p));
        }
        game.getMoveHistory().forEach(m -> dto.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return dto;
    }
}
