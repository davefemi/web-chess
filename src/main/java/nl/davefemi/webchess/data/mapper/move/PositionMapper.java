package nl.davefemi.webchess.data.mapper.move;

import nl.davefemi.webchess.game.board.AlgebraicSquare;
import nl.davefemi.webchess.game.board.Square;
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
