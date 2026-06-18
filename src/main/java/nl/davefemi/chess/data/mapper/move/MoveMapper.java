package nl.davefemi.chess.data.mapper.move;

import lombok.RequiredArgsConstructor;

import nl.davefemi.chess.http.dto.move.CastlingMoveDto;
import nl.davefemi.chess.http.dto.MoveDto;
import nl.davefemi.chess.http.dto.move.EnPassantMoveDto;
import nl.davefemi.chess.http.dto.move.PromotionMoveDto;
import nl.davefemi.chess.http.dto.move.SingleMoveDto;
import nl.davefemi.chess.play.model.actions.move.CastlingMove;
import nl.davefemi.chess.play.model.actions.move.Move;
import nl.davefemi.chess.play.model.actions.move.PromotionMove;
import nl.davefemi.chess.play.model.actions.move.SingleMove;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MoveMapper {
    private final SingleMoveMapper singleMoveMapper;
    private final CastlingMoveMapper castlingMoveMapper;
    private final PromotionMoveMapper promotionMoveMapper;

    public MoveDto mapDomainToDTO(Move move){
        if (move instanceof CastlingMove m)
            return castlingMoveMapper.mapDomainToDTO(m);
        if (move instanceof PromotionMove m)
            return promotionMoveMapper.mapDomainToDTO(m);
        return singleMoveMapper.mapDomainToDTO((SingleMove) move);
    }

    public Move mapDTOtoDomain(MoveDto move){
        if (move instanceof CastlingMoveDto m)
            return castlingMoveMapper.mapDTOtoDomain(m);
        if (move instanceof PromotionMoveDto m)
            return promotionMoveMapper.mapDTOtoDomain(m);
        if (move instanceof EnPassantMoveDto m)
            return singleMoveMapper.mapDTOtoDomain(m);
        return singleMoveMapper.mapDTOtoDomain((SingleMoveDto) move);
    }

    public List<MoveDto> mapDomainToDTO(List<Move> moves){
        List<MoveDto> dtos = new ArrayList<>();
        for (Move m : moves){
            dtos.add(mapDomainToDTO(m));
        }
        return dtos;
    }

}
