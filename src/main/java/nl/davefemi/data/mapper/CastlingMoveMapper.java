package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.CastlingMoveDTO;
import nl.davefemi.domain.game.move.CastlingMove;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CastlingMoveMapper {
    private final SingleMoveMapper moveMapper;

    public CastlingMove mapDTOtoDomain(CastlingMoveDTO dto){
        return new CastlingMove(moveMapper.mapDTOtoDomain(dto.getKing_move()),
                moveMapper.mapDTOtoDomain(dto.getRook_move()));
    }

    public CastlingMoveDTO mapDomainToDTO(CastlingMove castlingMove){
        CastlingMoveDTO dto = new CastlingMoveDTO();
        dto.setKing_move(moveMapper.mapDomainToDTO(castlingMove.moveKing()));
        dto.setRook_move(moveMapper.mapDomainToDTO(castlingMove.moveRook()));
        return dto;
    }
}
