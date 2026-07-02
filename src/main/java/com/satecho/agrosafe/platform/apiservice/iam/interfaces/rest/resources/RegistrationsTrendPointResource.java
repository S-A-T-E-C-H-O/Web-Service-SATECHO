package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import java.time.LocalDate;

/** One day of the 30-day registrations trend (EP-011-US003 Scenario 2). */
public record RegistrationsTrendPointResource(LocalDate date, long farmerCount, long agronomistCount) {
}
