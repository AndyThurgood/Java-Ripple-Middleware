package org.rippleosi.common.service;

import java.util.Map;

public abstract class AbstractPostQueryStrategy<T> implements QueryStrategy<T> {

    private final String patientId;

    protected AbstractPostQueryStrategy(String patientId) {
        this.patientId = patientId;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    public abstract Map<String, String> getQuery(String namespace, String patientId);
}
