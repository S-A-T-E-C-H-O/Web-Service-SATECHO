package com.satecho.agrosafe.platform.apiservice.soil.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.soil.application.commandservices.DiagnosisCommandService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.GenerateDiagnosisCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.events.DiagnosisGeneratedEvent;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.DiagnosisRepository;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.SensorReadingRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiagnosisCommandServiceImpl implements DiagnosisCommandService {

    private final DiagnosisRepository diagnosisRepository;
    private final SensorReadingRepository sensorReadingRepository;

    public DiagnosisCommandServiceImpl(DiagnosisRepository diagnosisRepository, SensorReadingRepository sensorReadingRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public Result<Diagnosis, ApplicationError> generateDiagnosis(GenerateDiagnosisCommand command) {
        List<SensorReading> latestReadings = sensorReadingRepository.findLatestByZoneGroupedByMetricType(command.zoneId());
        Map<MetricType, SensorReading> readingsByType = latestReadings.stream()
                .collect(Collectors.toMap(SensorReading::getMetricType, r -> r, (a, b) -> b));

        Double moisture = getValue(readingsByType, MetricType.SOIL_MOISTURE);
        Double ec = getValue(readingsByType, MetricType.ELECTRICAL_CONDUCTIVITY);
        Double ph = getValue(readingsByType, MetricType.SOIL_PH);
        Double soilTemp = getValue(readingsByType, MetricType.SOIL_TEMPERATURE);

        double waterStressIndex = computeWaterStressIndex(moisture, ec);
        double soilHealthScore = computeSoilHealthScore(moisture, ec, ph, soilTemp);
        String recommendations = generateRecommendations(moisture, ec, ph, soilTemp);
        String moistureStatus = classifyMoisture(moisture);
        String ecStatus = classifyEC(ec);
        String phStatus = classifyPH(ph);
        String temperatureStatus = classifyTemperature(soilTemp);

        var diagnosis = new Diagnosis(command.zoneId(), waterStressIndex, soilHealthScore,
                recommendations, moistureStatus, ecStatus, phStatus, temperatureStatus);
        var saved = diagnosisRepository.save(diagnosis);
        saved.addDomainEvent(new DiagnosisGeneratedEvent(saved.getId(), saved.getZoneId(),
                saved.getWaterStressIndex(), saved.getSoilHealthScore(), saved.getGeneratedAt()));
        return Result.success(saved);
    }

    private Double getValue(Map<MetricType, SensorReading> readings, MetricType type) {
        var reading = readings.get(type);
        return reading != null ? reading.getValue() : null;
    }

    private double computeWaterStressIndex(Double moisture, Double ec) {
        if (moisture == null && ec == null) return 50.0;
        double stressScore = 0;
        if (moisture != null) {
            if (moisture < 20) stressScore += 50; else if (moisture < 40) stressScore += 35;
            else if (moisture < 60) stressScore += 20; else if (moisture < 80) stressScore += 5;
        }
        if (ec != null) {
            if (ec > 15) stressScore += 50; else if (ec > 10) stressScore += 35;
            else if (ec > 5) stressScore += 20; else if (ec > 2) stressScore += 5;
        }
        return Math.min(100.0, Math.max(0.0, stressScore));
    }

    private double computeSoilHealthScore(Double moisture, Double ec, Double ph, Double soilTemp) {
        if (moisture == null && ec == null && ph == null && soilTemp == null) return 60.0;
        double score = 100.0;
        if (moisture != null) {
            if (moisture < 20) score -= 30; else if (moisture < 40) score -= 15;
            else if (moisture < 60) score -= 5; else if (moisture <= 80) score -= 0; else score -= 10;
        } else score -= 10;
        if (ec != null) {
            if (ec > 15) score -= 30; else if (ec > 10) score -= 20;
            else if (ec > 5) score -= 10; else if (ec <= 2) score -= 0; else score -= 5;
        } else score -= 10;
        if (ph != null) {
            if (ph < 5.0 || ph > 8.5) score -= 25; else if (ph < 5.5 || ph > 8.0) score -= 15;
            else if (ph < 6.0 || ph > 7.5) score -= 5;
        } else score -= 10;
        if (soilTemp != null) {
            if (soilTemp < 5 || soilTemp > 45) score -= 20;
            else if (soilTemp < 10 || soilTemp > 35) score -= 10;
            else if (soilTemp < 15 || soilTemp > 30) score -= 5;
        } else score -= 5;
        return Math.min(100.0, Math.max(0.0, score));
    }

    private String generateRecommendations(Double moisture, Double ec, Double ph, Double soilTemp) {
        StringBuilder sb = new StringBuilder();
        if (moisture != null) {
            if (moisture < 20) sb.append("CRITICAL: Soil moisture extremely low (").append(String.format("%.1f", moisture)).append("%). Immediate irrigation. ");
            else if (moisture < 40) sb.append("WARNING: Soil moisture below optimal (").append(String.format("%.1f", moisture)).append("%). Schedule irrigation. ");
            else if (moisture > 80) sb.append("WARNING: Soil moisture high (").append(String.format("%.1f", moisture)).append("%). Reduce irrigation. ");
            else sb.append("Soil moisture optimal (").append(String.format("%.1f", moisture)).append("%). ");
        }
        if (ec != null) {
            if (ec > 10) sb.append("CRITICAL: EC very high (").append(String.format("%.2f", ec)).append(" dS/m). Leaching recommended. ");
            else if (ec > 5) sb.append("WARNING: EC elevated (").append(String.format("%.2f", ec)).append(" dS/m). Monitor salinity. ");
            else sb.append("EC acceptable (").append(String.format("%.2f", ec)).append(" dS/m). ");
        }
        if (ph != null) {
            if (ph < 5.5) sb.append("Soil pH acidic (").append(String.format("%.1f", ph)).append("). Consider lime. ");
            else if (ph > 8.0) sb.append("Soil pH alkaline (").append(String.format("%.1f", ph)).append("). Consider sulfur. ");
            else sb.append("Soil pH optimal (").append(String.format("%.1f", ph)).append("). ");
        }
        if (soilTemp != null) {
            if (soilTemp < 10) sb.append("Soil temp low (").append(String.format("%.1f", soilTemp)).append("C). Consider row covers. ");
            else if (soilTemp > 35) sb.append("Soil temp high (").append(String.format("%.1f", soilTemp)).append("C). Consider mulching. ");
            else sb.append("Soil temp optimal (").append(String.format("%.1f", soilTemp)).append("C). ");
        }
        return sb.length() > 0 ? sb.toString().trim() : "All parameters normal. Continue monitoring.";
    }

    private String classifyMoisture(Double v) { if (v == null) return "UNKNOWN"; if (v < 20) return "CRITICALLY_LOW"; if (v < 40) return "LOW"; if (v < 60) return "OPTIMAL"; if (v < 80) return "ADEQUATE"; return "HIGH"; }
    private String classifyEC(Double v) { if (v == null) return "UNKNOWN"; if (v > 15) return "CRITICAL_SALINITY"; if (v > 10) return "HIGH_SALINITY"; if (v > 5) return "MODERATE_SALINITY"; if (v > 2) return "SLIGHTLY_SALINE"; return "NON_SALINE"; }
    private String classifyPH(Double v) { if (v == null) return "UNKNOWN"; if (v < 5.5) return "STRONGLY_ACIDIC"; if (v < 6.0) return "MODERATELY_ACIDIC"; if (v < 6.5) return "SLIGHTLY_ACIDIC"; if (v < 7.5) return "NEUTRAL"; if (v < 8.0) return "SLIGHTLY_ALKALINE"; return "MODERATELY_ALKALINE"; }
    private String classifyTemperature(Double v) { if (v == null) return "UNKNOWN"; if (v < 5) return "VERY_COLD"; if (v < 10) return "COLD"; if (v < 15) return "COOL"; if (v < 25) return "OPTIMAL"; if (v < 30) return "WARM"; if (v < 35) return "HOT"; return "VERY_HOT"; }
}
