package nl.davefemi.data.mapper.move;

import nl.davefemi.data.dto.move.PositionDTO;
import nl.davefemi.game.board.Position;
import org.springframework.stereotype.Component;

@Component
public class PositionMapper {

    public PositionDTO mapDomainToDTO(Position position){
        PositionDTO dto = new PositionDTO();
        dto.setFile(position.file());
        dto.setRank(position.rank());
        return dto;
    }

    public Position mapDTOtoDomain(PositionDTO position){
        return new Position(position.getFile(), position.getRank());
    }

}
