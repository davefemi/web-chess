package nl.davidfemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davidfemi.data.dto.CastlingMoveDTO;
import nl.davidfemi.data.dto.MoveDTO;
import nl.davidfemi.domain.game.utility.CastlingMove;
import nl.davidfemi.domain.game.utility.Move;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CastlingMoveMapper {
    private final MoveMapper moveMapper;

    public CastlingMove mapDTOtoDomain(CastlingMoveDTO dto){
        return new CastlingMove(moveMapper.mapDTOtoDomain(dto.getKingMove()),
                moveMapper.mapDTOtoDomain(dto.getRookMove()));
    }

    public CastlingMoveDTO mapDomainToDTO(CastlingMove castlingMove){
        CastlingMoveDTO dto = new CastlingMoveDTO();
        dto.setKingMove(moveMapper.mapDomainToDTO(castlingMove.moveKing()));
        dto.setRookMove(moveMapper.mapDomainToDTO(castlingMove.moveRook()));
        return dto;
    }

    public List<CastlingMoveDTO> mapDomainToDTO(List<CastlingMove> moves){
        List<CastlingMoveDTO> dtos = new ArrayList<>();
        for (CastlingMove cm : moves){
            dtos.add(mapDomainToDTO(cm));
        }
        return dtos;
    }
}
