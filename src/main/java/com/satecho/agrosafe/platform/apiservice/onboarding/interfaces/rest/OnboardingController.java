package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.OnboardingCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.OnboardingQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CompleteOnboardingCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.CompleteOnboardingResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.CropThresholdsResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.OnboardingStatusResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform.CropThresholdsResourceFromCropTypeAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform.OnboardingStatusResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Onboarding", description = "Onboarding and crop type endpoints")
@PreAuthorize("isAuthenticated()")
public class OnboardingController {

    private final OnboardingCommandService onboardingCommandService;
    private final OnboardingQueryService onboardingQueryService;

    public OnboardingController(OnboardingCommandService onboardingCommandService,
                                OnboardingQueryService onboardingQueryService) {
        this.onboardingCommandService = onboardingCommandService;
        this.onboardingQueryService = onboardingQueryService;
    }

    @GetMapping("/crops/types")
    public ResponseEntity<List<CropThresholdsResource>> getCropTypes() {
        var resources = Arrays.stream(CropType.values())
                .map(CropThresholdsResourceFromCropTypeAssembler::toResourceFromCropType)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/onboarding/status")
    public ResponseEntity<OnboardingStatusResource> getOnboardingStatus() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var progress = onboardingCommandService.ensureProgress(userId);
        return ResponseEntity.ok(OnboardingStatusResourceFromEntityAssembler.toResourceFromEntity(progress));
    }

    @PostMapping("/onboarding/complete")
    public ResponseEntity<?> completeOnboarding(@RequestBody(required = false) CompleteOnboardingResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var command = new CompleteOnboardingCommand(userId);
        var result = onboardingCommandService.completeOnboarding(command);
        if (result.isSuccess()) {
            var statusResource = OnboardingStatusResourceFromEntityAssembler.toResourceFromEntity(result.toOptional().orElseThrow());
            return ResponseEntity.ok(statusResource);
        }
        return ResponseEntity.badRequest().build();
    }
}