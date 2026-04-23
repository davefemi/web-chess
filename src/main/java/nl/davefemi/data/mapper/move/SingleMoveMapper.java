package nl.davefemi.data.mapper.move;

import nl.davefemi.data.dto.move.SingleMoveDTO;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.actions.move.SingleMove;
import org.springframework.stereotype.Component;

@Component
public class SingleMoveMapper {

    public SingleMoveDTO mapDomainToDTO(SingleMove move){
        SingleMoveDTO dto = new SingleMoveDTO();
        dto.setFromFile(move.from().file());
        dto.setFromRank(move.from().rank());
        dto.setToFile(move.to().file());
        dto.setToRank(move.to().rank());
        return dto;
    }

    public SingleMove mapDTOtoDomain(SingleMoveDTO move){
        return new SingleMove(new Position(move.getFromFile(), move.getFromRank()),
                new Position(move.getToFile(), move.getToRank()));
    }
}
