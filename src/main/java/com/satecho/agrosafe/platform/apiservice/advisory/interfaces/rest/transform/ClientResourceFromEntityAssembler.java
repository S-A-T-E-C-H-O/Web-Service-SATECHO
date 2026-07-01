package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.ClientResource;

public class ClientResourceFromEntityAssembler {

    private ClientResourceFromEntityAssembler() {
    }

    public static ClientResource toResourceFromEntity(AgronomistClient entity) {
        return new ClientResource(
                entity.getId(),
                entity.getAgronomistId(),
                entity.getFarmerId(),
                entity.getLinkedAt(),
                entity.getActive(),
                entity.getNotes(),
                null
        );
    }
}
