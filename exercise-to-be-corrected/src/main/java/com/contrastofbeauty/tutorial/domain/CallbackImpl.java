package com.contrastofbeauty.tutorial.domain;

import com.contrastofbeauty.tutorial.domain.interfaces.Callback;
import com.contrastofbeauty.tutorial.services.interfaces.Service;
import org.apache.log4j.Logger;

public class CallbackImpl implements Callback {

    private final static Logger LOGGER = Logger.getLogger(CallbackImpl.class);

    private Service cloudService;

    public CallbackImpl(Service service) {
        cloudService = service;
    }

    @Override
    public void addTask(TweetTask tweetTask, long userId) {

        if (cloudService != null) {
            cloudService.submitTask(tweetTask, userId);
            LOGGER.info("task submitted to the cloud service (" + tweetTask.toString() + ").");
        }
    }
}
