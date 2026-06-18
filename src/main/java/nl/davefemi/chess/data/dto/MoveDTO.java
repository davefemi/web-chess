package nl.davefemi.chess.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.davefemi.chess.data.dto.move.CastlingMoveDTO;
import nl.davefemi.chess.data.dto.move.EnPassantMoveDTO;
import nl.davefemi.chess.data.dto.move.PromotionMoveDTO;
import nl.davefemi.chess.data.dto.move.SingleMoveDTO;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveDTO.class, name = "single"),
        @JsonSubTypes.Type(value = EnPassantMoveDTO.class, name = "enpassant"),
        @JsonSubTypes.Type(value = CastlingMoveDTO.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveDTO.class, name = "promotion")
})
public interface MoveDTO {
}
