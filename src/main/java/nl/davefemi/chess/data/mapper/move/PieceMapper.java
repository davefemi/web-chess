package nl.davefemi.chess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.web.dto.move.PositionPieceDto;
import nl.davefemi.chess.data.entity.session.PieceEntity;
import nl.davefemi.chess.gameplay.model.board.Piece;
import nl.davefemi.chess.gameplay.model.board.PieceType;
import nl.davefemi.chess.gameplay.model.board.Square;
import nl.davefemi.chess.gameplay.model.game.Color;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PieceMapper {
    private final PositionMapper positionMapper;

    public PositionPieceDto mapDomainToDTO(Square position, Piece piece){
        PositionPieceDto dto = new PositionPieceDto();
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
