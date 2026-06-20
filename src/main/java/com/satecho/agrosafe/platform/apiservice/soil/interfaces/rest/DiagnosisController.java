package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.soil.application.commandservices.DiagnosisCommandService;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.DiagnosisQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.exceptions.DiagnosisNotFoundException;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.GenerateDiagnosisCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetDiagnosisQuery;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.DiagnosisResource;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.SoilHealthResource;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform.DiagnosisResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform.SoilHealthResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/soil", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Soil Diagnosis", description = "Soil diagnosis endpoints")
@PreAuthorize("isAuthenticated()")
public class DiagnosisController {

    private final DiagnosisCommandService diagnosisCommandService;
    private final DiagnosisQueryService diagnosisQueryService;

    public DiagnosisController(DiagnosisCommandService diagnosisCommandService, DiagnosisQueryService diagnosisQueryService) {
        this.diagnosisCommandService = diagnosisCommandService;
        this.diagnosisQueryService = diagnosisQueryService;
    }

    @PostMapping("/diagnosis/{zoneId}")
    public ResponseEntity<?> generateDiagnosis(@PathVariable Long zoneId) {
        var result = diagnosisCommandService.generateDiagnosis(new GenerateDiagnosisCommand(zoneId));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, DiagnosisResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @GetMapping("/diagnosis/{diagnosisId}")
    public ResponseEntity<DiagnosisResource> getDiagnosisById(@PathVariable Long diagnosisId) {
        var diagnosis = diagnosisQueryService.findById(new GetDiagnosisQuery(diagnosisId))
                .orElseThrow(() -> new DiagnosisNotFoundException(diagnosisId));
        return ResponseEntity.ok(DiagnosisResourceFromEntityAssembler.toResourceFromEntity(diagnosis));
    }

    @GetMapping("/diagnosis/zones/{zoneId}/latest")
    public ResponseEntity<SoilHealthResource> getLatestDiagnosisForZone(@PathVariable Long zoneId) {
        var diagnosis = diagnosisQueryService.findLatestByZoneId(zoneId)
                .orElseThrow(() -> new DiagnosisNotFoundException("No diagnosis found for zone ID: " + zoneId));
        return ResponseEntity.ok(SoilHealthResourceFromEntityAssembler.toResourceFromEntity(diagnosis));
    }
}
