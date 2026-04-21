package nl.davidfemi.data.mapper;

import nl.davidfemi.data.dto.MoveDTO;
import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.game.utility.Move;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MoveMapper {

    public MoveDTO mapDomainToDTO(Move move){
        MoveDTO dto = new MoveDTO();
        dto.setFromFile(move.from().file());
        dto.setFromRank(move.from().rank());
        dto.setToFile(move.to().file());
        dto.setToRank(move.to().rank());
        return dto;
    }

    public Move mapDTOtoDomain(MoveDTO move){
        return new Move(new Position(move.getFromFile(), move.getFromRank()),
                new Position(move.getToFile(), move.getToRank()));
    }

    public List<MoveDTO> mapDomainToDTO(List<Move> moves){
        List<MoveDTO> dtos = new ArrayList<>();
        for (Move m : moves){
            dtos.add(mapDomainToDTO(m));
        }
        return dtos;
    }
}
