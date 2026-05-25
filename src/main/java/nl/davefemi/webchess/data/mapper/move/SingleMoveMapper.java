package nl.davefemi.webchess.data.mapper.move;

import nl.davefemi.webchess.data.dto.move.SingleMoveDTO;
import nl.davefemi.webchess.game.board.AlgebraicSquare;
import nl.davefemi.webchess.game.actions.SingleMove;
import org.springframework.stereotype.Component;

@Component
public class SingleMoveMapper {

    public SingleMoveDTO mapDomainToDTO(SingleMove move){
        SingleMoveDTO dto = new SingleMoveDTO();
        dto.setFrom(AlgebraicSquare.fromFileAndRank(move.from().file()-1, move.from().rank()-1).value());
        dto.setTo(AlgebraicSquare.fromFileAndRank(move.to().file()-1, move.to().rank()-1).value());
        return dto;
    }

    public SingleMove mapDTOtoDomain(SingleMoveDTO move){
        AlgebraicSquare from = new AlgebraicSquare(move.getFrom());
        AlgebraicSquare to = new AlgebraicSquare(move.getTo());
        return new SingleMove(from.toPosition(), to.toPosition());
    }
}
