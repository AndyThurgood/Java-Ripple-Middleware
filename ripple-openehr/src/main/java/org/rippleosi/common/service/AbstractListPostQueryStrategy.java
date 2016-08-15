package org.rippleosi.common.service;

import java.util.List;

public abstract class AbstractListPostQueryStrategy<T> extends AbstractPostQueryStrategy<List<T>> {

    protected AbstractListPostQueryStrategy(String patientId) {
        super(patientId);
    }
}
