package nl.davefemi.webchess.data.mapper.session;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.session.BoardDTO;
import nl.davefemi.webchess.data.entity.session.BoardContextEntity;
import nl.davefemi.webchess.data.entity.session.PieceEntity;
import nl.davefemi.webchess.data.mapper.move.PieceMapper;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.MoveRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final PieceMapper positionPieceMapper;

    public BoardDTO mapDomainToDTO(Board board) throws BoardException {
        BoardDTO dto = new BoardDTO();
        for (Piece p : board.getPieces()){
            dto.getBoard().add(positionPieceMapper.mapDomainToDTO(board.getPositionById(p.id()), p));
        }
        return dto;
    }

    public BoardContextEntity mapDomainToEntity(GameBoardContext boardContext) throws BoardException {
        BoardContextEntity entity = new BoardContextEntity();
        Board board = boardContext.getCopyOfBoard();
        for (Piece piece: board.getPieces()){
            PieceEntity positionPiece = new PieceEntity();
            positionPiece.setPosition(board.getPositionById(piece.id()).value());
            positionPiece.setPieceType(piece.type().getLabel());
            positionPiece.setColor(piece.color().getColor());
            positionPiece.setPieceId(piece.id());
            entity.getPositions().add(positionPiece);
        }
        for (Piece p: boardContext.getCapturedPieces()){
            entity.getCapturedPieces().add(positionPieceMapper.mapDomainToEntity(p));
        }
        entity.setNextPieceId(boardContext.getCopyOfBoard().getNextPieceId());
        entity.setOriginalRooks(boardContext.getOriginalRooks());
        return entity;
    }

    public GameBoardContext mapEntityToDomain(BoardContextEntity entity, MoveRecord lastMove, Color playerToMove) throws BoardException {
        Piece[] pieces = new Piece[128];
        for (PieceEntity p : entity.getPositions()) {
            Piece piece = positionPieceMapper.mapEntityToPiece(p);
            pieces[p.getPosition()] = piece;
        }
        List<Piece> capturedPieces = new ArrayList<>();
        for (PieceEntity e : entity.getCapturedPieces())
            capturedPieces.add(positionPieceMapper.mapEntityToPiece(e));
        return new GameBoardContext(playerToMove,
                new Board(pieces, entity.getNextPieceId()),
                lastMove,
                capturedPieces,
                entity.getOriginalRooks());
    }
}
