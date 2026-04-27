package nl.davefemi.webchess.data.dto.move;

import lombok.Data;

@Data
public class SingleMoveDTO implements MoveDTO {
    private Integer fromFile;
    private Integer fromRank;
    private Integer toFile;
    private Integer toRank;
}
