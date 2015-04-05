package com.contrastofbeauty.tutorial.domain;

import org.apache.log4j.Logger;
import com.contrastofbeauty.tutorial.api.domain.AcknoledgeService;

public class AcknoledgeServiceImpl implements AcknoledgeService {

    private final static Logger logger = Logger.getLogger(AcknoledgeServiceImpl.class);

    public AcknoledgeServiceImpl() {

    }

    @Override
    public void sendAckFailed(RuntimeException exception) {
        logger.error(exception);
        System.exit(1);
    }

    @Override
    public void sendAckSuccess() {
        logger.info("shout down the cloud.");
        System.exit(0);
    }
}
