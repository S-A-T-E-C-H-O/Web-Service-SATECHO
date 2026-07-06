package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

/**
 * Command record for assigning a client to an agronomist.
 *
 * @param agronomistUserId the unique identifier of the agronomist user
 * @param farmerUserId     the unique identifier of the farmer user
 * @author Colegio
 * @version 1.0
 */
public record AssignClientCommand(Long agronomistUserId, Long farmerUserId) {
}
