package nl.davefemi.chess.web.message.dto.record;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveRecordDto.class, name = "single"),
        @JsonSubTypes.Type(value = CastlingMoveRecordDto.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveRecordDto.class, name = "promotion"),
        @JsonSubTypes.Type(value = EnPassantMoveRecordDto.class, name = "enpassant")
})
public interface MoveRecordDto {
}
