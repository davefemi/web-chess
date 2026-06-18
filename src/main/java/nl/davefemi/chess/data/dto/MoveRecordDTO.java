package nl.davefemi.chess.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.davefemi.chess.data.dto.record.CastlingMoveRecordDTO;
import nl.davefemi.chess.data.dto.record.EnPassantMoveRecordDTO;
import nl.davefemi.chess.data.dto.record.PromotionMoveRecordDTO;
import nl.davefemi.chess.data.dto.record.SingleMoveRecordDTO;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveRecordDTO.class, name = "single"),
        @JsonSubTypes.Type(value = CastlingMoveRecordDTO.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveRecordDTO.class, name = "promotion"),
        @JsonSubTypes.Type(value = EnPassantMoveRecordDTO.class, name = "enpassant")
})
public interface MoveRecordDTO {
}
