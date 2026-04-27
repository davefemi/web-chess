package nl.davefemi.webchess.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.GameStateDTO;
import nl.davefemi.webchess.data.dto.record.MoveRecordData;
import nl.davefemi.webchess.data.entity.GameStateEntity;
import nl.davefemi.webchess.data.entity.PositionPieceEntity;
import nl.davefemi.webchess.data.mapper.move.PositionPieceMapper;
import nl.davefemi.webchess.data.mapper.record.MoveRecordMapper;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.board.Piece;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.record.MoveRecord;
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
        entity.setOriginalRooks(game.getOriginalRooks());
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
                capturedPiece, entity.getOriginalRooks());
    }

    public GameStateDTO mapDomainToDTO(Game game, String turn){
        GameStateDTO dto = new GameStateDTO();
        dto.setNextTurn(turn);
        dto.setActiveGame(game.isGameActive());
        game.getMoveHistory().forEach(m -> dto.getMoveHistory().add(moveRecordMapper.mapDomainToDTO(m)));
        return dto;
    }
}
