package com.ars.adminservice.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RouteConfigDTO {
    private String routeId;
    private String uri;
    private List<String> predicates = new ArrayList<>();
    private RateLimiterConfigDTO rate = new RateLimiterConfigDTO();

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<String> predicates) {
        this.predicates = predicates;
    }

    public RateLimiterConfigDTO getRate() {
        return rate;
    }

    public void setRate(RateLimiterConfigDTO rate) {
        this.rate = rate;
    }
}
