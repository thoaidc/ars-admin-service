package com.ars.adminservice.dto;

import com.ars.adminservice.constants.ExceptionConstants;
import com.ars.adminservice.constants.GatewaySecurityConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class RateLimiterConfigDTO {
    @NotBlank(message = ExceptionConstants.RATE_LIMITER_ROUTE_ID_NOT_BLANK)
    private String routeId;

    @Min(value = 1, message = ExceptionConstants.RATE_LIMITER_MIN_BAN_THRESHOLD)
    @NotNull(message = ExceptionConstants.RATE_LIMITER_MIN_BAN_THRESHOLD)
    private Integer banThreshold = GatewaySecurityConstants.BAN_THRESHOLD;

    @Min(value = 1, message = ExceptionConstants.RATE_LIMITER_WINDOW_SECOND)
    @NotNull(message = ExceptionConstants.RATE_LIMITER_WINDOW_SECOND)
    private Integer windowSeconds = GatewaySecurityConstants.WINDOW_SECONDS;

    @Min(value = 0, message = ExceptionConstants.RATE_LIMITER_BAN_DURATION_MINUTES)
    @NotNull(message = ExceptionConstants.RATE_LIMITER_BAN_DURATION_MINUTES)
    private Integer banDurationMinutes = GatewaySecurityConstants.BAN_DURATION_MINUTES;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Integer getBanThreshold() {
        return banThreshold;
    }

    public void setBanThreshold(Integer banThreshold) {
        this.banThreshold = banThreshold;
    }

    public Integer getWindowSeconds() {
        return windowSeconds;
    }

    public void setWindowSeconds(Integer windowSeconds) {
        this.windowSeconds = windowSeconds;
    }

    public Integer getBanDurationMinutes() {
        return banDurationMinutes;
    }

    public void setBanDurationMinutes(Integer banDurationMinutes) {
        this.banDurationMinutes = banDurationMinutes;
    }
}
