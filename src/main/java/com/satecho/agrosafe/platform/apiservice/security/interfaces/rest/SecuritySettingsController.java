package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecuritySettingsCommandService;
import com.satecho.agrosafe.platform.apiservice.security.application.queryservices.SecuritySettingsQueryService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.UpdateSecuritySettingsCommand;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.queries.GetSecuritySettingsByFarmQuery;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform.*;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Security Settings", description = "Security settings endpoints")
public class SecuritySettingsController {

    private final SecuritySettingsCommandService securitySettingsCommandService;
    private final SecuritySettingsQueryService securitySettingsQueryService;

    public SecuritySettingsController(SecuritySettingsCommandService securitySettingsCommandService,
                                      SecuritySettingsQueryService securitySettingsQueryService) {
        this.securitySettingsCommandService = securitySettingsCommandService;
        this.securitySettingsQueryService = securitySettingsQueryService;
    }

    @GetMapping("/farms/{farmId}/security/settings")
    public ResponseEntity<SecuritySettingsResource> getSettings(@PathVariable Long farmId) {
        SecurityContextUtil.getCurrentUserId();
        var settings = securitySettingsQueryService.handle(new GetSecuritySettingsByFarmQuery(farmId))
                .orElseGet(() -> {
                    var defaults = new com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings(farmId);
                    var cmd = new UpdateSecuritySettingsCommand(farmId, defaults.getMotionSensitivity(),
                            defaults.getAlertMode(), defaults.getDetectionScheduleStart(),
                            defaults.getDetectionScheduleEnd(), defaults.getNotificationContacts());
                    return securitySettingsCommandService.updateSettings(cmd).toOptional().orElseThrow();
                });
        return ResponseEntity.ok(SecuritySettingsResourceFromEntityAssembler.toResource(settings));
    }

    @PutMapping("/farms/{farmId}/security/settings")
    public ResponseEntity<?> updateSettings(@PathVariable Long farmId, @RequestBody UpdateSecuritySettingsResource resource) {
        SecurityContextUtil.getCurrentUserId();
        var command = UpdateSecuritySettingsCommandFromResourceAssembler.toCommand(farmId, resource);
        var result = securitySettingsCommandService.updateSettings(command);
        if (result.isSuccess()) return ResponseEntity.ok(SecuritySettingsResourceFromEntityAssembler.toResource(result.toOptional().orElseThrow()));
        return ResponseEntity.badRequest().build();
    }
}
