package nl.davefemi.data.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {
    private String gameId;
    private List<PositionPieceDTO> positionList = new ArrayList<>();
}
