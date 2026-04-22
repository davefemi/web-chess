package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.PiecePositionDTO;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.piece.Piece;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PiecePositionMapper {
    private final PositionMapper positionMapper;

    public PiecePositionDTO mapDomainToDTO(Position position, Piece piece){
        PiecePositionDTO dto = new PiecePositionDTO();
        dto.setPosition(positionMapper.mapDomainToDTO(position));
        dto.setPieceType(piece == null ? "vacant" : piece.getType().getLabel());
        dto.setColor(piece == null ? "vacant": piece.getColor().getColor());
        return dto;
    }
}
