package nl.davefemi.data.mapper;

import nl.davefemi.data.entity.AccessCodeEntity;
import nl.davefemi.session.AccessCode;
import org.springframework.stereotype.Component;

@Component
public class AccessCodeMapper {

    public AccessCodeEntity mapToEntity(AccessCode code){
        AccessCodeEntity entity = new AccessCodeEntity();
        entity.setToken(code.getToken());
        entity.setSessionId(code.getSessionId());
        entity.setExpiresAt(code.getExpiresAt());
        return entity;
    }
}
