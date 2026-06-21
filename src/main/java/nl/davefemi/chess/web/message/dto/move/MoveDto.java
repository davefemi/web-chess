package nl.davefemi.chess.web.message.dto.move;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveDto.class, name = "single"),
        @JsonSubTypes.Type(value = EnPassantMoveDto.class, name = "enpassant"),
        @JsonSubTypes.Type(value = CastlingMoveDto.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveDto.class, name = "promotion")
})
public interface MoveDto {
}
