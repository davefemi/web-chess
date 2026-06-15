package nl.davefemi.webchess.data.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.davefemi.webchess.data.entity.record.CastlingMoveRecordEntity;
import nl.davefemi.webchess.data.entity.record.EnPassantMoveRecordEntity;
import nl.davefemi.webchess.data.entity.record.PromotionMoveRecordEntity;
import nl.davefemi.webchess.data.entity.record.SingleMoveRecordEntity;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "move_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMoveRecordEntity.class, name = "single"),
        @JsonSubTypes.Type(value = CastlingMoveRecordEntity.class, name = "castling"),
        @JsonSubTypes.Type(value = PromotionMoveRecordEntity.class, name = "promotion"),
        @JsonSubTypes.Type(value = EnPassantMoveRecordEntity.class, name = "enpassant")
})
public interface MoveRecordEntity {
}
