package com.contrastofbeauty.tutorial.domain;

import com.contrastofbeauty.tutorial.domain.interfaces.Target;
import org.apache.log4j.Logger;

public class TargetImpl implements Target {

    private final static Logger LOGGER = Logger.getLogger(TargetImpl.class);

    public TargetImpl() {

    }

    @Override
    public void sendAckFailed(RuntimeException exception) {
        LOGGER.error(exception);
        System.exit(1);
    }

    @Override
    public void sendAckSuccess() {
        LOGGER.info("shout down the cloud.");
        System.exit(0);
    }
}
