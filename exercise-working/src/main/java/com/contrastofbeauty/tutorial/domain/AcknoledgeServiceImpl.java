package com.contrastofbeauty.tutorial.domain;

import com.contrastofbeauty.tutorial.api.domain.AcknoledgeService;
import org.apache.log4j.Logger;

public class AcknoledgeServiceImpl implements AcknoledgeService {

    private final static Logger LOGGER = Logger.getLogger(AcknoledgeServiceImpl.class);

    public AcknoledgeServiceImpl() {

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
