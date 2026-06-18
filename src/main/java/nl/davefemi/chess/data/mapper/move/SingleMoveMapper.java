package nl.davefemi.chess.data.mapper.move;

import nl.davefemi.chess.http.dto.move.EnPassantMoveDto;
import nl.davefemi.chess.http.dto.move.SingleMoveDto;
import nl.davefemi.chess.play.model.actions.move.EnPassantMove;
import nl.davefemi.chess.play.model.actions.move.SingleMove;
import nl.davefemi.chess.play.model.board.AlgebraicSquare;
import org.springframework.stereotype.Component;

@Component
public class SingleMoveMapper {

    public SingleMoveDto mapDomainToDTO(SingleMove move){
        SingleMoveDto dto = new SingleMoveDto();
        dto.setFrom(AlgebraicSquare.fromFileAndRank(move.from().file(), move.from().rank()).value());
        dto.setTo(AlgebraicSquare.fromFileAndRank(move.to().file(), move.to().rank()).value());
        return dto;
    }

    public EnPassantMoveDto mapDomainToDTO(EnPassantMove move){
        EnPassantMoveDto dto = new EnPassantMoveDto();
        dto.setFrom(AlgebraicSquare.fromFileAndRank(move.from().file(), move.from().rank()).value());
        dto.setTo(AlgebraicSquare.fromFileAndRank(move.to().file(), move.to().rank()).value());
        return dto;
    }

    public EnPassantMove mapDTOtoDomain(EnPassantMoveDto move){
        AlgebraicSquare from = new AlgebraicSquare(move.getFrom());
        AlgebraicSquare to = new AlgebraicSquare(move.getTo());
        return new EnPassantMove(from.toSquare(), to.toSquare());
    }

    public SingleMove mapDTOtoDomain(SingleMoveDto move){
        AlgebraicSquare from = new AlgebraicSquare(move.getFrom());
        AlgebraicSquare to = new AlgebraicSquare(move.getTo());
        return new SingleMove(from.toSquare(), to.toSquare());
    }
}
