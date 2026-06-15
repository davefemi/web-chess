package nl.davefemi.webchess.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.davefemi.webchess.data.dto.record.CastlingMoveRecordDTO;
import nl.davefemi.webchess.data.dto.record.EnPassantMoveRecordDTO;
import nl.davefemi.webchess.data.dto.record.PromotionMoveRecordDTO;
import nl.davefemi.webchess.data.dto.record.SingleMoveRecordDTO;

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
