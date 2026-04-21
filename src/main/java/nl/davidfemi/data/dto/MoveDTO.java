package nl.davidfemi.data.dto;

import lombok.Data;

@Data
public class MoveDTO {
    private Integer fromFile;
    private Integer fromRank;
    private Integer toFile;
    private Integer toRank;
}
