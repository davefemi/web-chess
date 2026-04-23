package nl.davefemi.data.dto.move;

import lombok.Data;

@Data
public class MoveRequestDTO <T extends MoveDTO>{
    private String color;
    private T move;
}
