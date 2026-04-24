package nl.davefemi.data.dto;

import lombok.Data;

@Data
public class GameResponseDTO {
    private String gameId;
    private String color;
    private Object message;
}
