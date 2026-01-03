package com.ars.adminservice.properties;

import com.ars.adminservice.constants.PropertiesConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = PropertiesConstants.GATEWAY_PREFIX_PROPERTIES)
public class GatewayProps {
    private int redisDb = 1; // Default use database 1 in Redis
    private API api;

    public API getApi() {
        return api;
    }

    public void setApi(API api) {
        this.api = api;
    }

    public int getRedisDb() {
        return redisDb;
    }

    public void setRedisDb(int redisDb) {
        this.redisDb = redisDb;
    }

    public static class API {
        private String refreshPublicRequestPatternsApi;
        private String refreshRateLimitExcludedApi;
        private String refreshRateLimiter;
        private String getPublicRequestPatternsApi;
        private String getRateLimitExcludedApi;
        private String getRoutesConfig;

        public String getRefreshRateLimiter() {
            return refreshRateLimiter;
        }

        public void setRefreshRateLimiter(String refreshRateLimiter) {
            this.refreshRateLimiter = refreshRateLimiter;
        }

        public String getGetRoutesConfig() {
            return getRoutesConfig;
        }

        public void setGetRoutesConfig(String getRoutesConfig) {
            this.getRoutesConfig = getRoutesConfig;
        }

        public String getRefreshRateLimitExcludedApi() {
            return refreshRateLimitExcludedApi;
        }

        public void setRefreshRateLimitExcludedApi(String refreshRateLimitExcludedApi) {
            this.refreshRateLimitExcludedApi = refreshRateLimitExcludedApi;
        }

        public String getGetRateLimitExcludedApi() {
            return getRateLimitExcludedApi;
        }

        public void setGetRateLimitExcludedApi(String getRateLimitExcludedApi) {
            this.getRateLimitExcludedApi = getRateLimitExcludedApi;
        }

        public String getRefreshPublicRequestPatternsApi() {
            return refreshPublicRequestPatternsApi;
        }

        public void setRefreshPublicRequestPatternsApi(String refreshPublicRequestPatternsApi) {
            this.refreshPublicRequestPatternsApi = refreshPublicRequestPatternsApi;
        }

        public String getGetPublicRequestPatternsApi() {
            return getPublicRequestPatternsApi;
        }

        public void setGetPublicRequestPatternsApi(String getPublicRequestPatternsApi) {
            this.getPublicRequestPatternsApi = getPublicRequestPatternsApi;
        }
    }
}
