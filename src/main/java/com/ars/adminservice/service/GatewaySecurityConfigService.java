package com.ars.adminservice.service;

import com.ars.adminservice.dto.RateLimiterConfigDTO;
import com.dct.model.dto.response.BaseResponseDTO;

import java.util.List;

public interface GatewaySecurityConfigService {
    BaseResponseDTO getGatewayPublicRequestPatterns();
    BaseResponseDTO updateGatewayPublicRequestPatterns(List<String> requestPatterns);
    BaseResponseDTO resetGatewayPublicRequestPatterns(String token);
    BaseResponseDTO getRateLimiterConfigs();
    BaseResponseDTO getRateLimiterExcludedApiConfigs();
    BaseResponseDTO updateRateLimiterConfigs(List<RateLimiterConfigDTO> rateLimiterConfigs);
    BaseResponseDTO updateRateLimiterExcludedApiConfigs(List<String> rateLimiterExcludedApiConfigs);
    BaseResponseDTO resetRateLimiterConfigs(String token);
    BaseResponseDTO resetRateLimiterExcludedConfigs(String token);
    void notifyUpdateRateLimitForGateway(String token);
    void notifyUpdateRateLimitExcludedForGateway(String token);
    void notifyUpdatePublicRequestPatternsForGateway(String token);
}
