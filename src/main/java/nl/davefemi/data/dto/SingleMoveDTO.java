package nl.davefemi.data.dto;

import lombok.Data;

@Data
public class SingleMoveDTO implements MoveDTO {
    private Integer from_file;
    private Integer from_rank;
    private Integer to_file;
    private Integer to_rank;
}
