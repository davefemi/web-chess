package nl.davidfemi.data.mapper;

import nl.davidfemi.data.dto.PositionDTO;
import nl.davidfemi.domain.board.Position;
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
