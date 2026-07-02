package com.satecho.agrosafe.platform.apiservice.activitylog.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.activitylog.application.queryservices.ActivityLogQueryService;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.aggregates.ActivityLogEntry;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.repositories.ActivityLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ActivityLogQueryServiceImpl implements ActivityLogQueryService {

    private static final int MAX_PAGE_SIZE = 100;

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogQueryServiceImpl(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    public Page<ActivityLogEntry> findByFarm(Long farmId, ActivityType type, int page, int size) {
        var pageable = PageRequest.of(Math.max(page, 0), Math.min(size <= 0 ? MAX_PAGE_SIZE : size, MAX_PAGE_SIZE));
        return type != null
                ? activityLogRepository.findByFarmIdAndType(farmId, type, pageable)
                : activityLogRepository.findByFarmId(farmId, pageable);
    }
}
