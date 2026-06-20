package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources;

import java.util.List;

public record BatchIngestSummaryResource(int ingested, int failed, List<String> errors) {}
