package com.contrastofbeauty.tutorial.domain;

import java.util.List;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;

public class TweetTask implements Callable<Long> {

    private final static Logger LOGGER = Logger.getLogger(TweetTask.class);

    private List<Tweet> processingList;

    public TweetTask(List<Tweet> list) {
        processingList = list;
    }

    @Override
    public Long call() throws Exception {

        LOGGER.info("task is going to be executed, tweets are going to be posted.");

        for(Tweet t : processingList) {
            LOGGER.info("tweet " + "\"" + t.getText() + "\"" + " has been posted.");
        }

        return Long.valueOf(processingList.size());
    }

    @Override
    public String toString() {
        return "This tweet task is a collection of " + processingList.size() + " tweets.";
    }
}
