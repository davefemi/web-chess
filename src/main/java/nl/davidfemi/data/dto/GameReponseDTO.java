package nl.davidfemi.data.dto;

import lombok.Data;

@Data
public class GameReponseDTO {
    private String gameId;
    private String color;
    private String piece;
    private Object message;
}
