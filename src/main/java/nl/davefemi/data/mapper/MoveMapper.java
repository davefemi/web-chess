package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;

import nl.davefemi.data.dto.CastlingMoveDTO;
import nl.davefemi.data.dto.MoveDTO;
import nl.davefemi.data.dto.PromotionMoveDTO;
import nl.davefemi.data.dto.SingleMoveDTO;
import nl.davefemi.domain.game.move.CastlingMove;
import nl.davefemi.domain.game.move.Move;
import nl.davefemi.domain.game.move.PromotionMove;
import nl.davefemi.domain.game.move.SingleMove;
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
        if (move instanceof CastlingMove)
            return castlingMoveMapper.mapDomainToDTO((CastlingMove) move);
        if (move instanceof PromotionMove)
            return promotionMoveMapper.mapDomainToDTO((PromotionMove) move);
        return singleMoveMapper.mapDomainToDTO((SingleMove) move);
    }

    public Move mapDTOtoDomain(MoveDTO move){
        if (move instanceof CastlingMoveDTO)
            return castlingMoveMapper.mapDTOtoDomain((CastlingMoveDTO) move);
        if (move instanceof PromotionMoveDTO)
            return promotionMoveMapper.mapDTOtoDomain((PromotionMoveDTO) move);
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
