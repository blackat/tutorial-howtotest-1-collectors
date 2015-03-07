package com.contrastofbeauty.tutorial.domain;

import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.api.services.Service;
import org.apache.log4j.Logger;

public class CallbackImpl implements Callback<TweetTask> {

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

    @Override
    public void setService(Service service) {

    }
}
