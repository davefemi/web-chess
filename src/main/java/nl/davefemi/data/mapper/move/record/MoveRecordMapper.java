package nl.davefemi.data.mapper.move.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.move.record.CastlingMoveRecordData;
import nl.davefemi.data.dto.move.record.MoveRecordData;
import nl.davefemi.data.dto.move.record.PromotionMoveRecordData;
import nl.davefemi.data.dto.move.record.SingleMoveRecordData;
import nl.davefemi.domain.game.actions.record.CastlingMoveRecord;
import nl.davefemi.domain.game.actions.record.MoveRecord;
import nl.davefemi.domain.game.actions.record.PromotionMoveRecord;
import nl.davefemi.domain.game.actions.record.SingleMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MoveRecordMapper {
    private final SingleMoveRecordMapper singleMoveRecordMapper;
    private final CastlingMoveRecordMapper castlingMoveRecordMapper;
    private final PromotionMoveRecordMapper promotionMoveRecordMapper;

    public MoveRecordData mapDomainToDTO(MoveRecord record){
        if (record instanceof CastlingMoveRecord r){
            return castlingMoveRecordMapper.mapDomainToData(r);
        }
        if (record instanceof PromotionMoveRecord r){
            return promotionMoveRecordMapper.mapDomainToData(r);
        }
        SingleMoveRecord r = (SingleMoveRecord) record;
        return singleMoveRecordMapper.mapDomainToData(r);
    }

    public MoveRecord mapDataToDomain(MoveRecordData data){
        if (data instanceof CastlingMoveRecordData e){
            return castlingMoveRecordMapper.mapDataToDomain (e);
        }
        if (data instanceof PromotionMoveRecordData e){
            return promotionMoveRecordMapper.mapDataToDomain(e);
        }
        SingleMoveRecordData r = (SingleMoveRecordData) data;
        return singleMoveRecordMapper.mapDataToDomain(r);
    }
}
