package nl.davefemi.webchess.data.dto.record;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveRecordData.class, name = "single"),
        @JsonSubTypes.Type(value = CastlingMoveRecordData.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveRecordData.class, name = "promotion")
})
public interface MoveRecordData {
}
