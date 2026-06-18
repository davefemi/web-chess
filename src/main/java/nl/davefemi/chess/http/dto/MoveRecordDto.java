package nl.davefemi.chess.http.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.davefemi.chess.http.dto.record.CastlingMoveRecordDto;
import nl.davefemi.chess.http.dto.record.EnPassantMoveRecordDto;
import nl.davefemi.chess.http.dto.record.PromotionMoveRecordDto;
import nl.davefemi.chess.http.dto.record.SingleMoveRecordDto;

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
