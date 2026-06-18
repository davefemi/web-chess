package nl.davefemi.chess.data.mapper.record;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.data.entity.record.CastlingMoveRecordEntity;
import nl.davefemi.chess.http.dto.MoveRecordDto;
import nl.davefemi.chess.data.entity.MoveRecordEntity;
import nl.davefemi.chess.data.entity.record.EnPassantMoveRecordEntity;
import nl.davefemi.chess.data.entity.record.PromotionMoveRecordEntity;
import nl.davefemi.chess.data.entity.record.SingleMoveRecordEntity;
import nl.davefemi.chess.play.model.actions.record.CastlingMoveRecord;
import nl.davefemi.chess.play.model.actions.MoveRecord;
import nl.davefemi.chess.play.model.actions.record.EnPassantMoveRecord;
import nl.davefemi.chess.play.model.actions.record.PromotionMoveRecord;
import nl.davefemi.chess.play.model.actions.record.SingleMoveRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MoveRecordMapper {
    private final SingleMoveRecordMapper singleMoveRecordMapper;
    private final CastlingMoveRecordMapper castlingMoveRecordMapper;
    private final PromotionMoveRecordMapper promotionMoveRecordMapper;
    private final EnPassantMoveRecordMapper enPassantMoveRecordMapper;

    public MoveRecordEntity mapDomainToEntity(MoveRecord record){
        if (record instanceof CastlingMoveRecord r){
            return castlingMoveRecordMapper.mapDomainToEntity(r);
        }
        if (record instanceof PromotionMoveRecord r){
            return promotionMoveRecordMapper.mapDomainToEntity(r);
        }
        if (record instanceof EnPassantMoveRecord r){
            return enPassantMoveRecordMapper.mapDomainToEntity(r);
        }
        SingleMoveRecord r = (SingleMoveRecord) record;
        return singleMoveRecordMapper.mapDomainToEntity(r);
    }

    public MoveRecordDto mapDomainToDTO(MoveRecord record){
        if (record instanceof CastlingMoveRecord r){
            return castlingMoveRecordMapper.mapDomainToDTO(r);
        }
        if (record instanceof PromotionMoveRecord r){
            return promotionMoveRecordMapper.mapDomainToDTO(r);
        }
        if (record instanceof EnPassantMoveRecord r){
            return enPassantMoveRecordMapper.mapDomainToDTO(r);
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
        if (data instanceof EnPassantMoveRecordEntity r){
            return enPassantMoveRecordMapper.mapDataToDomain(r);
        }
        SingleMoveRecordEntity r = (SingleMoveRecordEntity) data;
        return singleMoveRecordMapper.mapDataToDomain(r);
    }


}
