package com.contrastofbeauty.tutorial.collectors;

import com.contrastofbeauty.tutorial.api.collectors.Collector;
import com.contrastofbeauty.tutorial.domain.Tweet;
import com.contrastofbeauty.tutorial.domain.TweetTask;
import com.contrastofbeauty.tutorial.api.domain.Callback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class TweetCollector implements Collector {

    public static final int PROCESSING_LIST_BUFFER_SIZE = 100;

    private Map<Long, List<Tweet>> processingList;

    private Callback callbackFunction;

    private int customBufferSize;

    public TweetCollector() {
        // 0) Object not initialized
        processingList = new HashMap<>();
    }

    @Override
    public boolean accept(Object object, long userId) {

        if (object instanceof Tweet) {

            // 1) error: object not initialized - REMOVE THIS LINE TO HAVE THE ERROR
            if (processingList.get(userId) == null) {
                processingList.put(userId, new ArrayList<Tweet>());
            }

            processingList.get(userId).add((Tweet) object);

            if (customBufferSize != 0) {
                if (processingList.get(userId).size() == customBufferSize) {
                    flush(userId);
                }
            } else if (processingList.get(userId).size() == PROCESSING_LIST_BUFFER_SIZE) {
                flush(userId);
            }

            // 2) error: remove the return to have the exception - REMOVE THIS LINE TO HAVE THE ERROR
            return true;
        }

        return false;
    }

    @Override
    public void flush(long userId) {
        TweetTask tweetTask = (TweetTask) getTweetTask(userId);

        // clean the list
        processingList.get(userId).clear();

        // create a new processing task
        if (callbackFunction != null) {
            callbackFunction.addTask(tweetTask, userId);
        } else {
            throw new IllegalArgumentException("Callback function must be set by the service.");
        }
    }

    public Callable<Long> getTweetTask(long userId) {
        return new TweetTask(new ArrayList<>(processingList.get(userId)));
    }

    @Override
    public void postFlush(long userId) {
        return;
    }

    @Override
    public void setCallbackFunction(Callback callback) {
        callbackFunction = callback;
    }

    public void setNewBufferSize(int bufferSize) {
        customBufferSize = bufferSize;
    }
}
