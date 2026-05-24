package nl.davefemi.webchess.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.BoardDTO;
import nl.davefemi.webchess.data.entity.BoardContextEntity;
import nl.davefemi.webchess.data.entity.PositionPieceEntity;
import nl.davefemi.webchess.data.mapper.move.PositionPieceMapper;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.record.MoveRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final PositionPieceMapper positionPieceMapper;

    public BoardDTO mapDomainToDTO(Board board){
        BoardDTO dto = new BoardDTO();
        for (Position p : board.getBoardPositions()){
            dto.getPositionList().add(positionPieceMapper.
                    mapDomainToDTO(p, board.getPieceAt(p) != null? board.getPieceAt(p): null));
        }
        return dto;
    }

    public BoardContextEntity mapDomainToEntity(BoardContext boardContext){
        BoardContextEntity entity = new BoardContextEntity();
        Board board = boardContext.getCopyOfBoard();
        for (Position p: board.getBoardPositions()){
            Piece piece = board.getPieceAt(p);
            PositionPieceEntity positionPiece = new PositionPieceEntity();
            positionPiece.setFile(p.file());
            positionPiece.setRank(p.rank());
            if(piece != null) {
                positionPiece.setPieceType(piece.getType().getLabel());
                positionPiece.setColor(piece.getColor().getColor());
                positionPiece.setPieceId(piece.getId());
            }
            entity.getPositions().add(positionPiece);
        }
        for (Piece p: boardContext.getCapturedPieces()){
            entity.getCapturedPieces().add(positionPieceMapper.mapDomainToEntity(p));
        }
        entity.setNextPieceId(boardContext.getCopyOfBoard().getNextPieceId());
        entity.setOriginalRooks(boardContext.getOriginalRooks());
        return entity;
    }

    public BoardContext mapEntityToDomain(BoardContextEntity entity, MoveRecord lastMove, PieceColor playerToMove) throws BoardException {
        TreeMap<Position, Piece> positions = new TreeMap<>();
        for (PositionPieceEntity p : entity.getPositions()) {
            positions.put(positionPieceMapper.mapEntityToPosition(p), positionPieceMapper.mapEntityToPiece(p));
        }
        List<Piece> capturedPieces = new ArrayList<>();
        for (PositionPieceEntity e : entity.getCapturedPieces())
            capturedPieces.add(positionPieceMapper.mapEntityToPiece(e));
        return new BoardContext(playerToMove,
                new Board(positions, entity.getNextPieceId()),
                lastMove,
                capturedPieces,
                entity.getOriginalRooks());
    }
}
