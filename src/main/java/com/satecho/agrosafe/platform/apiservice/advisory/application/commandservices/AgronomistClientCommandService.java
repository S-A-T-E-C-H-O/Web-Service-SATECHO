package com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.LinkClientCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.UnlinkClientCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface AgronomistClientCommandService {
    Result<AgronomistClient, ApplicationError> linkClient(LinkClientCommand command);
    Result<Void, ApplicationError> unlinkClient(UnlinkClientCommand command);
}
