package org.rippleosi.common.model;

import java.util.Map;

public class QueryPost {

    private String query;
    private Map queryParams;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map queryParams) {
        this.queryParams = queryParams;
    }
}
