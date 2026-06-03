package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.PositionPieceDTO;
import nl.davefemi.webchess.data.entity.PieceEntity;
import nl.davefemi.webchess.game.board.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PieceMapper {
    private final PositionMapper positionMapper;

    public PositionPieceDTO mapDomainToDTO(Square position, Piece piece){
        PositionPieceDTO dto = new PositionPieceDTO();
        dto.setSquare(positionMapper.mapDomainToDTO(position));
        dto.setPieceType(piece == null ? "vacant" : piece.getType().getLabel());
        dto.setColor(piece == null ? "vacant": piece.getColor().getColor());
        return dto;
    }


    public PieceEntity mapDomainToEntity(Piece piece){
        if (piece != null){
            PieceEntity entity = new PieceEntity();
            entity.setPieceId(piece.getId());
            entity.setPieceType(piece.getType().getLabel());
            entity.setColor(piece.getColor().getColor());
            return entity;
        }
        return null;
    }

    public Piece mapEntityToPiece(PieceEntity entity){
        if (entity.getPieceType() != null){
            return new Piece(entity.getPieceId(),
                    PieceType.fromString(entity.getPieceType()),
                    PieceColor.fromString(entity.getColor()));
        }
        return null;
    }

}
