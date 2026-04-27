package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;

import nl.davefemi.webchess.data.dto.move.CastlingMoveDTO;
import nl.davefemi.webchess.data.dto.move.MoveDTO;
import nl.davefemi.webchess.data.dto.move.PromotionMoveDTO;
import nl.davefemi.webchess.data.dto.move.SingleMoveDTO;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MoveMapper {
    private final SingleMoveMapper singleMoveMapper;
    private final CastlingMoveMapper castlingMoveMapper;
    private final PromotionMoveMapper promotionMoveMapper;

    public MoveDTO mapDomainToDTO(Move move){
        if (move instanceof CastlingMove m)
            return castlingMoveMapper.mapDomainToDTO(m);
        if (move instanceof PromotionMove m)
            return promotionMoveMapper.mapDomainToDTO(m);
        return singleMoveMapper.mapDomainToDTO((SingleMove) move);
    }

    public Move mapDTOtoDomain(MoveDTO move){
        if (move instanceof CastlingMoveDTO m)
            return castlingMoveMapper.mapDTOtoDomain(m);
        if (move instanceof PromotionMoveDTO m)
            return promotionMoveMapper.mapDTOtoDomain(m);
        return singleMoveMapper.mapDTOtoDomain((SingleMoveDTO) move);
    }

    public List<MoveDTO> mapDomainToDTO(List<Move> moves){
        List<MoveDTO> dtos = new ArrayList<>();
        for (Move m : moves){
            dtos.add(mapDomainToDTO(m));
        }
        return dtos;
    }

}
