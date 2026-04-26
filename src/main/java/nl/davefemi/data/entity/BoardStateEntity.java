package nl.davefemi.data.entity;

import lombok.Data;

import java.util.*;

@Data
public class BoardStateEntity {
    private List<PositionPieceEntity> positions = new ArrayList<>();
}
