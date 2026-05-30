package nl.davefemi.webchess.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.BoardDTO;
import nl.davefemi.webchess.data.entity.BoardContextEntity;
import nl.davefemi.webchess.data.entity.PositionPieceEntity;
import nl.davefemi.webchess.data.mapper.move.PositionPieceMapper;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.MoveRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final PositionPieceMapper positionPieceMapper;

    public BoardDTO mapDomainToDTO(Board board) throws BoardException {
        BoardDTO dto = new BoardDTO();
        for (Piece p : board.getPieces()){
            dto.getBoard().add(positionPieceMapper.mapDomainToDTO(board.getPositionById(p.getId()), p));
        }
        return dto;
    }

    public BoardContextEntity mapDomainToEntity(BoardContext boardContext) throws BoardException {
        BoardContextEntity entity = new BoardContextEntity();
        Board board = boardContext.getCopyOfBoard();
        for (Piece piece: board.getPieces()){
            PositionPieceEntity positionPiece = new PositionPieceEntity();
            positionPiece.setPositionValue(board.getPositionById(piece.getId()).value());
            positionPiece.setPieceType(piece.getType().getLabel());
            positionPiece.setColor(piece.getColor().getColor());
            positionPiece.setPieceId(piece.getId());
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
        Piece[] pieces = new Piece[128];
        for (PositionPieceEntity p : entity.getPositions()) {
            Piece piece = positionPieceMapper.mapEntityToPiece(p);
            pieces[p.getPositionValue()] = piece;
        }
        List<Piece> capturedPieces = new ArrayList<>();
        for (PositionPieceEntity e : entity.getCapturedPieces())
            capturedPieces.add(positionPieceMapper.mapEntityToPiece(e));
        return new BoardContext(playerToMove,
                new Board(pieces, entity.getNextPieceId()),
                lastMove,
                capturedPieces,
                entity.getOriginalRooks());
    }
}
