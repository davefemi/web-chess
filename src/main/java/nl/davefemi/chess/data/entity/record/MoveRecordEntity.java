package nl.davefemi.chess.data.entity.record;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveRecordEntity.class, name = "single"),
        @JsonSubTypes.Type(value = CastlingMoveRecordEntity.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveRecordEntity.class, name = "promotion"),
        @JsonSubTypes.Type(value = EnPassantMoveRecordEntity.class, name = "enpassant")
})
public interface MoveRecordEntity {
}
