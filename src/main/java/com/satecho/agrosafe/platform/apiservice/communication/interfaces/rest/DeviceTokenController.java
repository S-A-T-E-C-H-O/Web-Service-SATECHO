package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.communication.application.commandservices.DeviceTokenCommandService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.RegisterDeviceTokenCommand;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.RegisterDeviceTokenResource;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Registers the FCM token of the current device so push notifications can be
 * routed to it (called by the Mobile App right after login).
 */
@RestController
@RequestMapping(value = "/api/v1/notifications/device-tokens", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Device Tokens", description = "FCM push token registration")
public class DeviceTokenController {

    private final DeviceTokenCommandService deviceTokenCommandService;

    public DeviceTokenController(DeviceTokenCommandService deviceTokenCommandService) {
        this.deviceTokenCommandService = deviceTokenCommandService;
    }

    @PostMapping
    public ResponseEntity<?> registerDeviceToken(@RequestBody RegisterDeviceTokenResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var command = new RegisterDeviceTokenCommand(userId, resource.fcmToken(), resource.platform());
        var result = deviceTokenCommandService.registerToken(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, token -> token.getId(), HttpStatus.CREATED);
    }
}
