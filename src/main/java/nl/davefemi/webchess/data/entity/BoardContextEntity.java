package nl.davefemi.webchess.data.entity;

import lombok.Data;

import java.util.*;

@Data
public class BoardContextEntity {
    private List<PositionPieceEntity> positions = new ArrayList<>();
    private List<Integer> originalRooks = new ArrayList<>();
    private List<PositionPieceEntity> capturedPieces = new ArrayList<>();
    private int nextPieceId;
}
