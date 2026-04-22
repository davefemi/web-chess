package nl.davefemi.data.mapper;

import nl.davefemi.data.dto.SingleMoveDTO;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.move.SingleMove;
import org.springframework.stereotype.Component;

@Component
public class SingleMoveMapper {

    public SingleMoveDTO mapDomainToDTO(SingleMove move){
        SingleMoveDTO dto = new SingleMoveDTO();
        dto.setFrom_file(move.from().file());
        dto.setFrom_rank(move.from().rank());
        dto.setTo_file(move.to().file());
        dto.setTo_rank(move.to().rank());
        return dto;
    }

    public SingleMove mapDTOtoDomain(SingleMoveDTO move){
        return new SingleMove(new Position(move.getFrom_file(), move.getFrom_rank()),
                new Position(move.getTo_file(), move.getTo_rank()));
    }
}
