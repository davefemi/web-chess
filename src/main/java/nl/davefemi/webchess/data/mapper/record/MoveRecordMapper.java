package nl.davefemi.webchess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.entity.record.CastlingMoveRecordEntity;
import nl.davefemi.webchess.data.MoveRecordDTO;
import nl.davefemi.webchess.data.entity.record.MoveRecordEntity;
import nl.davefemi.webchess.data.entity.record.PromotionMoveRecordEntity;
import nl.davefemi.webchess.data.entity.record.SingleMoveRecordEntity;
import nl.davefemi.webchess.game.record.CastlingMoveRecord;
import nl.davefemi.webchess.game.record.MoveRecord;
import nl.davefemi.webchess.game.record.PromotionMoveRecord;
import nl.davefemi.webchess.game.record.SingleMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MoveRecordMapper {
    private final SingleMoveRecordMapper singleMoveRecordMapper;
    private final CastlingMoveRecordMapper castlingMoveRecordMapper;
    private final PromotionMoveRecordMapper promotionMoveRecordMapper;

    public MoveRecordEntity mapDomainToEntity(MoveRecord record){
        if (record instanceof CastlingMoveRecord r){
            return castlingMoveRecordMapper.mapDomainToEntity(r);
        }
        if (record instanceof PromotionMoveRecord r){
            return promotionMoveRecordMapper.mapDomainToEntity(r);
        }
        SingleMoveRecord r = (SingleMoveRecord) record;
        return singleMoveRecordMapper.mapDomainToEntity(r);
    }

    public MoveRecordDTO mapDomainToDTO(MoveRecord record){
        if (record instanceof CastlingMoveRecord r){
            return castlingMoveRecordMapper.mapDomainToDTO(r);
        }
        if (record instanceof PromotionMoveRecord r){
            return promotionMoveRecordMapper.mapDomainToDTO(r);
        }
        SingleMoveRecord r = (SingleMoveRecord) record;
        return singleMoveRecordMapper.mapDomainToDTO(r);
    }

    public MoveRecord mapDataToDomain(MoveRecordEntity data){
        if (data instanceof CastlingMoveRecordEntity e){
            return castlingMoveRecordMapper.mapDataToDomain (e);
        }
        if (data instanceof PromotionMoveRecordEntity e){
            return promotionMoveRecordMapper.mapDataToDomain(e);
        }
        SingleMoveRecordEntity r = (SingleMoveRecordEntity) data;
        return singleMoveRecordMapper.mapDataToDomain(r);
    }


}
