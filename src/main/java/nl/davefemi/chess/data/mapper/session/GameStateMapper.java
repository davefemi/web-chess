package nl.davefemi.chess.data.mapper.session;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.web.message.response.game.GameStateDto;
import nl.davefemi.chess.data.entity.session.GameStateEntity;
import nl.davefemi.chess.data.entity.record.MoveRecordEntity;
import nl.davefemi.chess.data.mapper.move.PieceMapper;
import nl.davefemi.chess.data.mapper.record.MoveRecordMapper;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.gameplay.model.game.Game;
import nl.davefemi.chess.gameplay.model.game.GameStatus;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.action.record.MoveRecord;
import nl.davefemi.chess.gameplay.model.board.Piece;
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
        entity.setId(game.getId());
        entity.setCurrentRound(game.getCurrentRound());
        entity.setCurrentBoardContext(boardMapper.mapDomainToEntity(game.getGameBoardContext()));
        entity.setGamePhase(game.getStatus().phase().getPhase());
        entity.setInCheck(game.isInCheck());
        entity.setWinner(game.getStatus().winner().isPresent()? game.getStatus().winner().get().toString(): null);
        entity.setGameEndReason(game.getStatus().isFinished()? game.getStatus().reason().get().getReason() : null);
        String turn;
        try{
            turn = game.getSideToMove().toString();
        } catch (NullPointerException e) {
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
                entity.getId(),
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

    public GameStateDto mapDomainToDto(Game game) throws BoardException {
        GameStateDto dto = new GameStateDto();
        dto.setCurrentRound(game.getCurrentRound());
        String sideToMove = game.getSideToMove() == null ? null : game.getSideToMove().toString();
        dto.setColorToMove(sideToMove);
        dto.setInCheck(game.isInCheck());
        for (Piece p : game.getGameBoardContext().getCopyOfBoard().getPieces()){
            dto.getBoard().add(positionPieceMapper.mapDomainToDTO(
                    game.getGameBoardContext().getCopyOfBoard().getPositionById(p.id()), p));
        }
        game.getMoveHistory().forEach(m -> dto.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return dto;
    }
}
