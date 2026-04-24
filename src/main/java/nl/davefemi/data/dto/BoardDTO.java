package nl.davefemi.data.dto;

import lombok.Data;
import nl.davefemi.data.dto.move.PositionPieceDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {
    private String gameId;
    private List<PositionPieceDTO> positionList = new ArrayList<>();
}
