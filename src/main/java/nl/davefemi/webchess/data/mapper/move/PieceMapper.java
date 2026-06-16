package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.PositionPieceDTO;
import nl.davefemi.webchess.data.entity.session.PieceEntity;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.board.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PieceMapper {
    private final PositionMapper positionMapper;

    public PositionPieceDTO mapDomainToDTO(Square position, Piece piece){
        PositionPieceDTO dto = new PositionPieceDTO();
        dto.setSquare(positionMapper.mapDomainToDTO(position));
        dto.setPieceType(piece == null ? "vacant" : piece.type().toString());
        dto.setColor(piece == null ? "vacant": piece.color().toString());
        return dto;
    }


    public PieceEntity mapDomainToEntity(Piece piece){
        if (piece != null){
            PieceEntity entity = new PieceEntity();
            entity.setPieceId(piece.id());
            entity.setPieceType(piece.type().toString());
            entity.setColor(piece.color().toString());
            return entity;
        }
        return null;
    }

    public Piece mapEntityToPiece(PieceEntity entity){
        if (entity.getPieceType() != null){
            return new Piece(entity.getPieceId(),
                    PieceType.fromString(entity.getPieceType()),
                    Color.fromString(entity.getColor()));
        }
        return null;
    }

}
