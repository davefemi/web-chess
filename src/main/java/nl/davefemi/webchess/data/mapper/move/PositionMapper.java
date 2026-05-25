package nl.davefemi.webchess.data.mapper.move;

import nl.davefemi.webchess.data.dto.move.PositionDTO;
import nl.davefemi.webchess.game.board.AlgebraicSquare;
import nl.davefemi.webchess.game.board.Position;
import org.springframework.stereotype.Component;

@Component
public class PositionMapper {

    public String mapDomainToDTO(Position position){
        return AlgebraicSquare.fromFileAndRank(position.file()-1, position.rank()-1).value();
    }

    public Position mapDTOtoDomain(PositionDTO position){
        return new Position(position.getFile(), position.getRank());
    }

}
