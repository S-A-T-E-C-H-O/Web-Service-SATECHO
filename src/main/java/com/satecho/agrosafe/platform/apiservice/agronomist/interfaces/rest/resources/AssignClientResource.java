package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/**
 * Resource class representing the request to assign a client.
 * Contains the necessary information to associate a farmer with an agronomist.
 *
 * @param farmerId the unique identifier of the farmer to be assigned
 */
public record AssignClientResource(Long farmerId) {
}

