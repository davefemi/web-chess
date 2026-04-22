package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.BoardDTO;
import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.Position;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final PiecePositionMapper piecePositionMapper;

    public BoardDTO mapDomainToDTO(UUID gameId, Board board){
        BoardDTO dto = new BoardDTO();
        dto.setGameId(gameId.toString());
        for (Position p : board.getPositions()){
            dto.getPositionList().add(piecePositionMapper.
                    mapDomainToDTO(p, board.getPieceAt(p) != null? board.getPieceAt(p): null));
        }
        return dto;
    }
}
