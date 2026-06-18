package nl.davefemi.chess.data.mapper.move;

import nl.davefemi.chess.data.dto.move.EnPassantMoveDTO;
import nl.davefemi.chess.data.dto.move.SingleMoveDTO;
import nl.davefemi.chess.play.model.actions.move.EnPassantMove;
import nl.davefemi.chess.play.model.actions.move.SingleMove;
import nl.davefemi.chess.play.model.board.AlgebraicSquare;
import org.springframework.stereotype.Component;

@Component
public class SingleMoveMapper {

    public SingleMoveDTO mapDomainToDTO(SingleMove move){
        SingleMoveDTO dto = new SingleMoveDTO();
        dto.setFrom(AlgebraicSquare.fromFileAndRank(move.from().file(), move.from().rank()).value());
        dto.setTo(AlgebraicSquare.fromFileAndRank(move.to().file(), move.to().rank()).value());
        return dto;
    }

    public EnPassantMoveDTO mapDomainToDTO(EnPassantMove move){
        EnPassantMoveDTO dto = new EnPassantMoveDTO();
        dto.setFrom(AlgebraicSquare.fromFileAndRank(move.from().file(), move.from().rank()).value());
        dto.setTo(AlgebraicSquare.fromFileAndRank(move.to().file(), move.to().rank()).value());
        return dto;
    }

    public EnPassantMove mapDTOtoDomain(EnPassantMoveDTO move){
        AlgebraicSquare from = new AlgebraicSquare(move.getFrom());
        AlgebraicSquare to = new AlgebraicSquare(move.getTo());
        return new EnPassantMove(from.toSquare(), to.toSquare());
    }

    public SingleMove mapDTOtoDomain(SingleMoveDTO move){
        AlgebraicSquare from = new AlgebraicSquare(move.getFrom());
        AlgebraicSquare to = new AlgebraicSquare(move.getTo());
        return new SingleMove(from.toSquare(), to.toSquare());
    }
}
