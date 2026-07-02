package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

/** Platform-wide counters for the admin dashboard (EP-011-US003). */
public record PlatformMetricsResource(long totalUsers, long farmerCount, long agronomistCount, long adminCount,
                                       long blockedUserCount, long totalFarms, long activeFarms, long totalDevices,
                                       long onlineDevices) {
}
