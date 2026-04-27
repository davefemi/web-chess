package nl.davefemi.webchess.data.dto.move;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveDTO.class, name = "single"),
        @JsonSubTypes.Type(value = CastlingMoveDTO.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveDTO.class, name = "promotion")
})
public interface MoveDTO {
}
