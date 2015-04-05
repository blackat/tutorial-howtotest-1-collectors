package com.contrastofbeauty.tutorial.domain;

import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.api.services.Service;
import org.apache.log4j.Logger;

public class CallbackImpl implements Callback<TweetTask> {

    private final static Logger logger = Logger.getLogger(CallbackImpl.class);

    private Service cloudService;

    @Override
    public void addTask(TweetTask tweetTask, long userId) {

        if (cloudService != null) {
            cloudService.submitTask(tweetTask, userId);
            logger.info("task submitted to the cloud service (" + tweetTask.toString() + ").");
        }
    }

    @Override
    public void setService(Service service) {
        cloudService = service;
    }
}
