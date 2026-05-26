package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.PositionPieceDTO;
import nl.davefemi.webchess.data.entity.PositionPieceEntity;
import nl.davefemi.webchess.game.board.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionPieceMapper {
    private final PositionMapper positionMapper;

    public PositionPieceDTO mapDomainToDTO(Square position, Piece piece){
        PositionPieceDTO dto = new PositionPieceDTO();
        dto.setSquare(positionMapper.mapDomainToDTO(position));
        dto.setPieceType(piece == null ? "vacant" : piece.getType().getLabel());
        dto.setColor(piece == null ? "vacant": piece.getColor().getColor());
        return dto;
    }

    public PositionPieceEntity mapDomainToEntity(Square position){
        PositionPieceEntity entity = new PositionPieceEntity();
        entity.setPositionValue(position.value());
        return entity;
    }

    public PositionPieceEntity mapDomainToEntity(Square position, Piece piece){
        PositionPieceEntity entity = mapDomainToEntity(piece);
        entity.setPositionValue(position.value());
        return entity;
    }

    public PositionPieceEntity mapDomainToEntity(Piece piece){
        PositionPieceEntity entity = new PositionPieceEntity();
        if (piece != null){
            entity.setPieceType(piece.getType().getLabel());
            entity.setColor(piece.getColor().getColor());
            return entity;
        }
        return entity;
    }

    public Piece mapEntityToPiece(PositionPieceEntity entity){
        if (entity.getPieceType() != null){
            return new Piece(entity.getPieceId(),
                    PieceType.fromString(entity.getPieceType()),
                    PieceColor.fromString(entity.getColor()));
        }
        return null;
    }

    public Square mapEntityToPosition(PositionPieceEntity entity){
        return new Square(entity.getPositionValue());
    }
}
