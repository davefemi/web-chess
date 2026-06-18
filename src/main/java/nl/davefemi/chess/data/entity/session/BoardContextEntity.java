package nl.davefemi.chess.data.entity.session;

import lombok.Data;

import java.util.*;

@Data
public class BoardContextEntity {
    private List<PieceEntity> positions = new ArrayList<>();
    private List<Integer> originalRooks = new ArrayList<>();
    private List<PieceEntity> capturedPieces = new ArrayList<>();
    private int nextPieceId;
}
