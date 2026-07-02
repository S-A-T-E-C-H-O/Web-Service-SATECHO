package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

public record AdminFarmResource(Long id, Long userId, String name, String location, double hectares,
                                 String cropType, boolean active) {
}
