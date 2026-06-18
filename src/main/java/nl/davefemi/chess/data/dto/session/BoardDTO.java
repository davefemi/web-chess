package nl.davefemi.chess.data.dto.session;

import lombok.Data;
import nl.davefemi.chess.data.dto.move.PositionPieceDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {
    private List<PositionPieceDTO> board = new ArrayList<>();
}
