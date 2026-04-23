package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.BoardDTO;
import nl.davefemi.data.entity.BoardStateEntity;
import nl.davefemi.data.entity.PositionPieceEntity;
import nl.davefemi.domain.board.*;
import org.springframework.stereotype.Component;
import java.util.TreeMap;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final PositionPieceMapper piecePositionMapper;

    public BoardDTO mapDomainToDTO(UUID gameId, Board board){
        BoardDTO dto = new BoardDTO();
        dto.setGameId(gameId.toString());
        for (Position p : board.getPositions()){
            dto.getPositionList().add(piecePositionMapper.
                    mapDomainToDTO(p, board.getPieceAt(p) != null? board.getPieceAt(p): null));
        }
        return dto;
    }

    public BoardStateEntity mapDomainToEntity(UUID gameId, Board board){
        BoardStateEntity entity = new BoardStateEntity();
        for (Position p: board.getPositions()){
            Piece piece = board.getPieceAt(p);
            PositionPieceEntity positionPiece = new PositionPieceEntity();
            positionPiece.setFile(p.file());
            positionPiece.setRank(p.rank());
            if(piece != null) {
                positionPiece.setPieceType(piece.getType().getLabel());
                positionPiece.setColor(piece.getColor().getColor());
            }
            entity.getPositions().add(positionPiece);
        }
        entity.setOriginalRooks(board.getOriginalRooks());
        return entity;
    }

    public Board mapEntityToDomain(BoardStateEntity entity){
        TreeMap<Position, Piece> positions = new TreeMap<>();
        for (PositionPieceEntity p : entity.getPositions()) {
            positions.put(piecePositionMapper.mapEntityToPosition(p), piecePositionMapper.mapEntityToPiece(p));
        }
        return new Board(positions, entity.getOriginalRooks());
    }
}
