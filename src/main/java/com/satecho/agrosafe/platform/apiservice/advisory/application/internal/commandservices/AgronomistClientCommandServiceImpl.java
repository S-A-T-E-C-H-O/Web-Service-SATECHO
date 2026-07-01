package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices.AgronomistClientCommandService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.exceptions.ClientLinkNotFoundException;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.LinkClientCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.UnlinkClientCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events.ClientLinkedEvent;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.AgronomistClientRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AgronomistClientCommandServiceImpl implements AgronomistClientCommandService {

    private final AgronomistClientRepository agronomistClientRepository;

    public AgronomistClientCommandServiceImpl(AgronomistClientRepository agronomistClientRepository) {
        this.agronomistClientRepository = agronomistClientRepository;
    }

    @Override
    public Result<AgronomistClient, ApplicationError> linkClient(LinkClientCommand command) {
        Optional<AgronomistClient> existingLink = agronomistClientRepository
                .findByAgronomistIdAndFarmerIdAndActiveTrue(command.agronomistId(), command.farmerId());

        if (existingLink.isPresent()) {
            return Result.success(existingLink.get());
        }

        AgronomistClient client = new AgronomistClient(command.agronomistId(), command.farmerId(), command.notes());
        AgronomistClient savedClient = agronomistClientRepository.save(client);

        ClientLinkedEvent event = new ClientLinkedEvent(
                savedClient.getId(),
                savedClient.getAgronomistId(),
                savedClient.getFarmerId(),
                savedClient.getLinkedAt()
        );
        savedClient.addDomainEvent(event);

        return Result.success(savedClient);
    }

    @Override
    public Result<Void, ApplicationError> unlinkClient(UnlinkClientCommand command) {
        AgronomistClient client = agronomistClientRepository.findById(command.clientId())
                .orElseThrow(() -> new ClientLinkNotFoundException(command.clientId()));

        if (!client.getAgronomistId().equals(command.agronomistId())) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "CLIENT_LINK_OWNERSHIP", "Client link does not belong to this agronomist"));
        }

        client.unlink();
        agronomistClientRepository.save(client);
        return Result.success(null);
    }
}
