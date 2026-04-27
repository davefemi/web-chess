package nl.davefemi.webchess.data.dto;

import lombok.Data;
import nl.davefemi.webchess.data.dto.move.PositionPieceDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {
    private List<PositionPieceDTO> positionList = new ArrayList<>();
}
