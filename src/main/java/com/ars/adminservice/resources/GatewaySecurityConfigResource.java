package com.ars.adminservice.resources;

import com.ars.adminservice.dto.RateLimiterConfigDTO;
import com.ars.adminservice.service.GatewaySecurityConfigService;
import com.dct.config.aop.annotation.CheckAuthorize;
import com.dct.model.constants.BaseRoleConstants;
import com.dct.model.constants.BaseSecurityConstants;
import com.dct.model.dto.response.BaseResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/gateway/securities")
public class GatewaySecurityConfigResource {
    private final GatewaySecurityConfigService gatewaySecurityConfigService;

    public GatewaySecurityConfigResource(GatewaySecurityConfigService gatewaySecurityConfigService) {
        this.gatewaySecurityConfigService = gatewaySecurityConfigService;
    }

    @GetMapping("/rate-limiter")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO getRateLimiterConfigs() {
        return gatewaySecurityConfigService.getRateLimiterConfigs();
    }

    @PutMapping("/rate-limiter")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO updateRateLimiterConfigs(HttpServletRequest request,
                                                    @Valid @RequestBody List<RateLimiterConfigDTO> rateLimiterConfigDTO) {
        String token = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_HEADER);
        BaseResponseDTO responseDTO = gatewaySecurityConfigService.updateRateLimiterConfigs(rateLimiterConfigDTO);

        if (responseDTO.getStatus()) {
            gatewaySecurityConfigService.notifyUpdateRateLimitForGateway(token);
        }

        return responseDTO;
    }

    @PostMapping("/rate-limiter/reset")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO resetRateLimiterConfigs(HttpServletRequest request) {
        String token = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_HEADER);
        BaseResponseDTO responseDTO = gatewaySecurityConfigService.resetRateLimiterConfigs(token);

        if (responseDTO.getStatus()) {
            gatewaySecurityConfigService.notifyUpdateRateLimitForGateway(token);
        }

        return responseDTO;
    }

    @GetMapping("/rate-limiter/excluded")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO getRateLimiterExcludedApiConfigs() {
        return gatewaySecurityConfigService.getRateLimiterExcludedApiConfigs();
    }

    @PutMapping("/rate-limiter/excluded")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO updateRateLimiterExcludedApiConfigs(HttpServletRequest request,
                                                               @Valid @RequestBody List<String> rateLimiterExcludedApis) {
        String token = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_HEADER);
        BaseResponseDTO responseDTO = gatewaySecurityConfigService.updateRateLimiterExcludedApiConfigs(rateLimiterExcludedApis);

        if (responseDTO.getStatus()) {
            gatewaySecurityConfigService.notifyUpdateRateLimitExcludedForGateway(token);
        }

        return responseDTO;
    }

    @PostMapping("/rate-limiter/excluded/reset")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO resetRateLimiterExcludedApiConfigs(HttpServletRequest request) {
        String token = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_HEADER);
        BaseResponseDTO responseDTO = gatewaySecurityConfigService.resetRateLimiterExcludedConfigs(token);

        if (responseDTO.getStatus()) {
            gatewaySecurityConfigService.notifyUpdateRateLimitExcludedForGateway(token);
        }

        return responseDTO;
    }

    @GetMapping("/public-request-patterns")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO getPublicRequestPatternsConfig() {
        return gatewaySecurityConfigService.getGatewayPublicRequestPatterns();
    }

    @PutMapping("/public-request-patterns")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO updatePublicRequestPatternsConfig(HttpServletRequest request,
                                                             @Valid @RequestBody List<String> publicRequestPatterns) {
        String token = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_HEADER);
        BaseResponseDTO responseDTO = gatewaySecurityConfigService.updateGatewayPublicRequestPatterns(publicRequestPatterns);

        if (responseDTO.getStatus()) {
            gatewaySecurityConfigService.notifyUpdatePublicRequestPatternsForGateway(token);
        }

        return responseDTO;
    }

    @PostMapping("/public-request-patterns/reset")
    @CheckAuthorize(authorities = BaseRoleConstants.System.SYSTEM)
    public BaseResponseDTO resetPublicRequestPatternsConfig(HttpServletRequest request) {
        String token = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_HEADER);
        BaseResponseDTO responseDTO = gatewaySecurityConfigService.resetGatewayPublicRequestPatterns(token);

        if (responseDTO.getStatus()) {
            gatewaySecurityConfigService.notifyUpdatePublicRequestPatternsForGateway(token);
        }

        return responseDTO;
    }
}
