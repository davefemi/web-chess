package nl.davefemi.chess.http.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.davefemi.chess.http.dto.move.CastlingMoveDto;
import nl.davefemi.chess.http.dto.move.EnPassantMoveDto;
import nl.davefemi.chess.http.dto.move.PromotionMoveDto;
import nl.davefemi.chess.http.dto.move.SingleMoveDto;

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
