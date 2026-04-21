package nl.davidfemi.data.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {
    private String gameId;
    private List<PiecePositionDTO> positionList = new ArrayList<>();
}
