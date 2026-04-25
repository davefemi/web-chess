package nl.davefemi.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.move.PositionPieceDTO;
import nl.davefemi.data.entity.PositionPieceEntity;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.game.board.Position;
import nl.davefemi.game.board.Piece;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionPieceMapper {
    private final PositionMapper positionMapper;

    public PositionPieceDTO mapDomainToDTO(Position position, Piece piece){
        PositionPieceDTO dto = new PositionPieceDTO();
        dto.setPosition(positionMapper.mapDomainToDTO(position));
        dto.setPieceType(piece == null ? "vacant" : piece.getType().getLabel());
        dto.setColor(piece == null ? "vacant": piece.getColor().getColor());
        return dto;
    }

    public PositionPieceEntity mapDomainToEntity(Position position){
        PositionPieceEntity entity = new PositionPieceEntity();
        entity.setFile(position.file());
        entity.setRank(position.rank());
        return entity;
    }

    public PositionPieceEntity mapDomainToEntity(Position position, Piece piece){
        PositionPieceEntity entity = mapDomainToEntity(piece);
        entity.setFile(position.file());
        entity.setRank(position.rank());
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

    public Position mapEntityToPosition(PositionPieceEntity entity){
        return new Position(entity.getFile(), entity.getRank());
    }
}
