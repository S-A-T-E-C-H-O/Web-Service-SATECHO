package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.CropThresholdsResource;

public class CropThresholdsResourceFromCropTypeAssembler {
    private CropThresholdsResourceFromCropTypeAssembler() {}
    public static CropThresholdsResource toResourceFromCropType(CropType cropType) {
        return new CropThresholdsResource(cropType.getName(), cropType.getDisplayName(),
                cropType.getMinMoisture(), cropType.getMaxMoisture(),
                cropType.getMinEc(), cropType.getMaxEc(),
                cropType.getMinPh(), cropType.getMaxPh(),
                cropType.getMinTemperature(), cropType.getMaxTemperature());
    }
}