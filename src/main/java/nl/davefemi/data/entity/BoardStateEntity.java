package nl.davefemi.data.entity;

import lombok.Data;

import java.util.*;

@Data
public class BoardStateEntity {
    private String gameId;
    private List<PositionPieceEntity> positions = new ArrayList<>();
    private List<Integer> originalRooks = new ArrayList<>();
}
