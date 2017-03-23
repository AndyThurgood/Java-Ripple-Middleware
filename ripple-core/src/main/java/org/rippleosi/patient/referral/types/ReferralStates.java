package org.rippleosi.patient.referral.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ReferralStates implements ReferralState {
    PLANNED("planned"),
    COMPLETED("completed");

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferralStates.class);

    private final String refState;

    private ReferralStates(final String refState) {
        this.refState = refState;
    }

    @Override
    public String getRefState() {
        return refState;
    }

    public static ReferralStates fromString(final String refState) {
        if (refState == null) {
            return null;
        }

        for (ReferralStates enumValue : ReferralStates.values()) {
            if (enumValue.getRefState().equalsIgnoreCase(refState)) {
                return enumValue;
            }
        }

        LOGGER.warn("Could not find an enumeration for '" + refState +"'");
        return null;
    }

    public String getReferralStateCode()
    {
        return getReferralStateCode(this);
    }

    public static String getReferralStateCode(final ReferralStates refState)
    {
        String code = null;

        switch (refState)
        {
            case PLANNED:
                code = "526";
                break;
            case COMPLETED:
                code = "532";
                break;
            default:
                code = null;
                break;
        }
        return  code;
    }

}
