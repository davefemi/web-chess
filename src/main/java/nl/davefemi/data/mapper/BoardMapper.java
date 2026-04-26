package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.BoardDTO;
import nl.davefemi.data.entity.BoardStateEntity;
import nl.davefemi.data.entity.PositionPieceEntity;
import nl.davefemi.data.mapper.move.PositionPieceMapper;
import nl.davefemi.game.board.Board;
import nl.davefemi.game.board.Piece;
import nl.davefemi.game.board.Position;
import nl.davefemi.exception.BoardException;
import org.springframework.stereotype.Component;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final PositionPieceMapper piecePositionMapper;

    public BoardDTO mapDomainToDTO(Board board){
        BoardDTO dto = new BoardDTO();
        for (Position p : board.getBoardPositions()){
            dto.getPositionList().add(piecePositionMapper.
                    mapDomainToDTO(p, board.getPieceAt(p) != null? board.getPieceAt(p): null));
        }
        return dto;
    }

    public BoardStateEntity mapDomainToEntity(Board board){
        BoardStateEntity entity = new BoardStateEntity();
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
        return entity;
    }

    public Board mapEntityToDomain(BoardStateEntity entity) throws BoardException {
        TreeMap<Position, Piece> positions = new TreeMap<>();
        for (PositionPieceEntity p : entity.getPositions()) {
            positions.put(piecePositionMapper.mapEntityToPosition(p), piecePositionMapper.mapEntityToPiece(p));
        }
        return new Board(positions);
    }
}
