package com.ars.adminservice.constants;

/**
 * <ul>
 *  <li>{@link #BAN_THRESHOLD}: Threshold of number of requests exceeded in a period of time to ban user/IP</li>
 *  <li>{@link #WINDOW_SECONDS}: Time window (in seconds) to count requests compared to banThreshold</li>
 *  <li>{@link #BAN_DURATION_MINUTES}: Time to ban user/IP (in minutes) when exceeding the threshold</li>
 * </ul>
 * @author thoaidc
 */
public interface GatewaySecurityConstants {
    int WINDOW_SECONDS = 60; // 1 minute
    int BAN_THRESHOLD = 100;
    int BAN_DURATION_MINUTES = 15; // 15 minutes
    String RATE_LIMITER_CODE = "rate_limiter_config";
    String RATE_LIMITER_EXCLUDED_API_CODE = "rate_limiter_excluded_api_config";
    String PUBLIC_REQUEST_PATTERN_CODE = "gateway_public_request_config";
}
