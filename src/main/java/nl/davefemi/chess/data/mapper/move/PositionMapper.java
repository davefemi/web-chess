package nl.davefemi.chess.data.mapper.move;

import nl.davefemi.chess.gameplay.model.board.AlgebraicSquare;
import nl.davefemi.chess.gameplay.model.board.Square;
import org.springframework.stereotype.Component;

@Component
public class PositionMapper {

    public String mapDomainToDTO(Square position){
        return AlgebraicSquare.fromFileAndRank(position.file(), position.rank()).value();
    }

    public Square mapDTOtoDomain(String position){
        return new AlgebraicSquare(position).toSquare();
    }

}
