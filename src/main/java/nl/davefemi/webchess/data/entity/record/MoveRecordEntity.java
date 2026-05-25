package nl.davefemi.webchess.data.entity.record;

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
        @JsonSubTypes.Type(value = PromotionMoveRecordEntity.class, name = "promotion")
})
public interface MoveRecordEntity {
}
