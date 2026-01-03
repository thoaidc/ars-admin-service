package com.ars.adminservice.service.impl;

import com.ars.adminservice.properties.GatewayProps;
import com.ars.adminservice.constants.GatewaySecurityConstants;
import com.ars.adminservice.constants.ResultConstants;
import com.ars.adminservice.dto.RateLimiterConfigDTO;
import com.ars.adminservice.dto.RouteConfigDTO;
import com.ars.adminservice.entity.Config;
import com.ars.adminservice.repository.ConfigRepository;
import com.ars.adminservice.service.GatewaySecurityConfigService;
import com.dct.model.dto.response.BaseResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GatewaySecurityConfigServiceImpl implements GatewaySecurityConfigService {
    private static final Logger log = LoggerFactory.getLogger(GatewaySecurityConfigServiceImpl.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConfigRepository configRepository;
    private final LettuceConnectionFactory defaultRedisFactory;
    private final GatewayProps gatewayProps;

    public GatewaySecurityConfigServiceImpl(
        RestTemplate restTemplate,
        ObjectMapper objectMapper,
        ConfigRepository configRepository,
        LettuceConnectionFactory defaultRedisFactory,
        GatewayProps gatewayProps
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.configRepository = configRepository;
        this.defaultRedisFactory = defaultRedisFactory;
        this.gatewayProps = gatewayProps;
    }

    @Override
    public BaseResponseDTO getGatewayPublicRequestPatterns() {
        Optional<Config> configOptional = configRepository.findByCode(GatewaySecurityConstants.PUBLIC_REQUEST_PATTERN_CODE);
        List<String> publicRequestConfigs = new ArrayList<>();

        if (configOptional.isPresent()) {
            String oldPublicRequestPatterns = configOptional.get().getValue();
            TypeReference<List<String>> typeReference = new TypeReference<>() {};

            try {
                publicRequestConfigs = objectMapper.readValue(oldPublicRequestPatterns, typeReference);
            } catch (JsonProcessingException e) {
                log.error("[COULD_NOT_READ_PUBLIC_PATTERNS_CONFIG] - config: {}, error: ", oldPublicRequestPatterns, e);
            }
        }

        return BaseResponseDTO
            .builder()
            .success(Boolean.TRUE)
            .message(ResultConstants.SUCCESS_GET_LIST)
            .result(publicRequestConfigs)
            .total((long) publicRequestConfigs.size())
            .build();
    }

    @Override
    public BaseResponseDTO updateGatewayPublicRequestPatterns(List<String> requestPatterns) {
        try {
            String publicRequestPatterns = objectMapper.writeValueAsString(requestPatterns);
            return saveConfig(GatewaySecurityConstants.PUBLIC_REQUEST_PATTERN_CODE, publicRequestPatterns);
        } catch (Exception e) {
            log.error("[COULD_NOT_PUBLIC_REQUEST_PATTERNS_CONFIG] - error: ", e);
        }

        return BaseResponseDTO
                .builder()
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .success(Boolean.FALSE)
                .message(ResultConstants.CONFIG_SAVE_FAIL)
                .build();
    }

    @Override
    public BaseResponseDTO resetGatewayPublicRequestPatterns(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String[]> response = restTemplate.exchange(
                gatewayProps.getApi().getGetPublicRequestPatternsApi(),
                HttpMethod.GET,
                requestEntity,
                String[].class
            );

            List<String> publicRequestPatterns = Arrays.asList(Objects.requireNonNull(response.getBody()));
            updateGatewayPublicRequestPatterns(publicRequestPatterns);
            return BaseResponseDTO.builder().ok(publicRequestPatterns);
        } catch (Exception e) {
            log.error("[COULD_NOT_READ_PUBLIC_REQUEST_PATTERNS_CONFIG] - error: ", e);
        }

        return BaseResponseDTO
                .builder()
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .success(Boolean.FALSE)
                .message(ResultConstants.CONFIG_SAVE_FAIL)
                .build();
    }

    @Override
    public BaseResponseDTO getRateLimiterConfigs() {
        Optional<Config> rateLimiterConfigOptional = configRepository.findByCode(GatewaySecurityConstants.RATE_LIMITER_CODE);
        List<RateLimiterConfigDTO> rateLimiterConfigDTOs = new ArrayList<>();

        if (rateLimiterConfigOptional.isPresent()) {
            String rateLimiterConfigValue = rateLimiterConfigOptional.get().getValue();
            TypeReference<List<RateLimiterConfigDTO>> typeReference = new TypeReference<>() {};

            try {
                rateLimiterConfigDTOs = objectMapper.readValue(rateLimiterConfigValue, typeReference);
            } catch (JsonProcessingException e) {
                log.error("[COULD_NOT_READ_RATE_LIMITER_CONFIG] - config: {}, error: ", rateLimiterConfigValue, e);
            }
        }

        return BaseResponseDTO
            .builder()
            .success(Boolean.TRUE)
            .message(ResultConstants.SUCCESS_GET_LIST)
            .result(rateLimiterConfigDTOs)
            .total((long) rateLimiterConfigDTOs.size())
            .build();
    }

    @Override
    public BaseResponseDTO getRateLimiterExcludedApiConfigs() {
        Optional<Config> configOptional = configRepository.findByCode(GatewaySecurityConstants.RATE_LIMITER_EXCLUDED_API_CODE);
        List<String> rateLimiterExcludedApis = new ArrayList<>();

        if (configOptional.isPresent()) {
            String oldRateLimiterExcludedApis = configOptional.get().getValue();
            TypeReference<List<String>> typeReference = new TypeReference<>() {};

            try {
                rateLimiterExcludedApis = objectMapper.readValue(oldRateLimiterExcludedApis, typeReference);
            } catch (JsonProcessingException e) {
                log.error("[COULD_NOT_READ_RATE_LIMITER_EXCLUDED] - config: {}, error: ", oldRateLimiterExcludedApis, e);
            }
        }

        return BaseResponseDTO
            .builder()
            .success(Boolean.TRUE)
            .message(ResultConstants.SUCCESS_GET_LIST)
            .result(rateLimiterExcludedApis)
            .total((long) rateLimiterExcludedApis.size())
            .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO resetRateLimiterConfigs(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<RouteConfigDTO[]> response = restTemplate.exchange(
                gatewayProps.getApi().getGetRoutesConfig(),
                HttpMethod.GET,
                requestEntity,
                RouteConfigDTO[].class
            );

            List<RouteConfigDTO> routeConfigDTOS = Arrays.asList(Objects.requireNonNull(response.getBody()));
            List<RateLimiterConfigDTO> rateLimiterConfigs = routeConfigDTOS
                .stream()
                .map(routeConfig -> {
                    RateLimiterConfigDTO rateLimiterConfig = new RateLimiterConfigDTO();
                    rateLimiterConfig.setRouteId(routeConfig.getRouteId());
                    rateLimiterConfig.setBanThreshold(routeConfig.getRate().getBanThreshold());
                    rateLimiterConfig.setWindowSeconds(routeConfig.getRate().getWindowSeconds());
                    rateLimiterConfig.setBanDurationMinutes(routeConfig.getRate().getBanDurationMinutes());
                    return rateLimiterConfig;
                })
                .collect(Collectors.toList());

            updateRateLimiterConfigs(rateLimiterConfigs);
            return BaseResponseDTO.builder().ok(rateLimiterConfigs);
        } catch (Exception e) {
            log.error("[COULD_NOT_READ_RATE_LIMITER_CONFIG] - error: ", e);
        }

        return BaseResponseDTO
                .builder()
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .success(Boolean.FALSE)
                .message(ResultConstants.CONFIG_SAVE_FAIL)
                .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO resetRateLimiterExcludedConfigs(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String[]> response = restTemplate.exchange(
                gatewayProps.getApi().getGetRateLimitExcludedApi(),
                HttpMethod.GET,
                requestEntity,
                String[].class
            );

            List<String> routeExcludedAPIS = Arrays.asList(Objects.requireNonNull(response.getBody()));
            updateRateLimiterExcludedApiConfigs(routeExcludedAPIS);
            return BaseResponseDTO.builder().ok(routeExcludedAPIS);
        } catch (Exception e) {
            log.error("[COULD_NOT_READ_RATE_LIMITER_EXCLUDED_CONFIG] - error: ", e);
        }

        return BaseResponseDTO
                .builder()
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .success(Boolean.FALSE)
                .message(ResultConstants.CONFIG_SAVE_FAIL)
                .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO updateRateLimiterConfigs(List<RateLimiterConfigDTO> rateLimiterConfigs) {
        try {
            String rateLimiterConfigValue = objectMapper.writeValueAsString(rateLimiterConfigs);
            return saveConfig(GatewaySecurityConstants.RATE_LIMITER_CODE, rateLimiterConfigValue);
        } catch (Exception e) {
            log.error("[COULD_NOT_UPDATE_RATE_LIMITER_CONFIG] - error: ", e);
        }

        return BaseResponseDTO
                .builder()
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .success(Boolean.FALSE)
                .message(ResultConstants.CONFIG_SAVE_FAIL)
                .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO updateRateLimiterExcludedApiConfigs(List<String> rateLimiterExcludedApiConfigs) {
        try {
            String rateLimitExcludedApiValue = objectMapper.writeValueAsString(rateLimiterExcludedApiConfigs);
            return saveConfig(GatewaySecurityConstants.RATE_LIMITER_EXCLUDED_API_CODE, rateLimitExcludedApiValue);
        } catch (Exception e) {
            log.error("[COULD_NOT_UPDATE_RATE_LIMITER_EXCLUDED_API_CONFIG] - error: ", e);
        }

        return BaseResponseDTO
                .builder()
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .success(Boolean.FALSE)
                .message(ResultConstants.CONFIG_SAVE_FAIL)
                .build();
    }

    @Async
    @Override
    public void notifyUpdateRateLimitForGateway(String token) {
        String refreshRateLimiterUri = gatewayProps.getApi().getRefreshRateLimiter();
        notifyUpdateForGateway(token, refreshRateLimiterUri);
    }

    @Async
    @Override
    public void notifyUpdateRateLimitExcludedForGateway(String token) {
        String refreshRateLimiterUri = gatewayProps.getApi().getRefreshRateLimitExcludedApi();
        notifyUpdateForGateway(token, refreshRateLimiterUri);
    }

    @Override
    @Async
    public void notifyUpdatePublicRequestPatternsForGateway(String token) {
        String refreshPublicRequestPatternsUri = gatewayProps.getApi().getRefreshPublicRequestPatternsApi();
        notifyUpdateForGateway(token, refreshPublicRequestPatternsUri);
    }

    private void notifyUpdateForGateway(String token, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);
        log.info("[REFRESHED_RATE_LIMITER_CONFIG_FOR_GATEWAY] - response: {}", response.getBody());
    }

    private BaseResponseDTO saveConfig(String configCode, String value) {
        Optional<Config> rateLimiterConfig = configRepository.findByCode(configCode);
        Config rateLimiterConfigDB;

        if (rateLimiterConfig.isPresent()) {
            rateLimiterConfigDB = rateLimiterConfig.get();
        } else {
            rateLimiterConfigDB = new Config();
            rateLimiterConfigDB.setShopId(0);
            rateLimiterConfigDB.setCode(configCode);
        }

        rateLimiterConfigDB.setValue(value);
        configRepository.save(rateLimiterConfigDB);
        RedisTemplate<String, String> redisTemplate = createTempRedisTemplate(gatewayProps.getRedisDb());
        redisTemplate.opsForValue().set(configCode, value);
        return BaseResponseDTO.builder().ok(rateLimiterConfigDB);
    }

    private RedisTemplate<String, String> createTempRedisTemplate(int dbIndex) {
        // Reuse host, port, password from main factory
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(defaultRedisFactory.getHostName());
        config.setPort(defaultRedisFactory.getPort());
        config.setPassword(defaultRedisFactory.getPassword());
        config.setDatabase(dbIndex);
        // Create a separate factory for temporary connections
        return getStringStringRedisTemplate(config);
    }

    private RedisTemplate<String, String> getStringStringRedisTemplate(RedisStandaloneConfiguration config) {
        LettuceConnectionFactory tempFactory = new LettuceConnectionFactory(config);
        tempFactory.afterPropertiesSet();
        // Create a separate template (does not affect the main RedisTemplate)
        RedisTemplate<String, String> tempTemplate = new RedisTemplate<>();
        tempTemplate.setConnectionFactory(tempFactory);
        tempTemplate.afterPropertiesSet();
        tempTemplate.setKeySerializer(new StringRedisSerializer());
        tempTemplate.setValueSerializer(new StringRedisSerializer());
        return tempTemplate;
    }
}
