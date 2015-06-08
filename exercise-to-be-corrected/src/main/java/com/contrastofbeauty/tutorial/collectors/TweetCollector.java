package com.contrastofbeauty.tutorial.collectors;

import com.contrastofbeauty.tutorial.api.collectors.Collector;
import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.domain.Tweet;
import com.contrastofbeauty.tutorial.domain.TweetTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TweetCollector implements Collector {

    public static final int PROCESSING_LIST_BUFFER_SIZE = 100;

    private Map<Long, List<Tweet>> processingList;

    private Callback callbackFunction;

    private int customBufferSize;

    public TweetCollector() {
    }

    @Override
    public boolean accept(Object object, long userId) {

        if (object instanceof Tweet) {

            processingList.get(userId).add((Tweet) object);

            if (customBufferSize != 0) {
                if (processingList.get(userId).size() == customBufferSize) {
                    flush(userId);
                }
            } else if (processingList.get(userId).size() == PROCESSING_LIST_BUFFER_SIZE) {
                flush(userId);
            }
        }

        return false;
    }

    @Override
    public void flush(long userId) {
        TweetTask tweetTask = new TweetTask(new ArrayList<>(processingList.get(userId)));

        // clean the list
        processingList.get(userId).clear();

        // create a new processing task
        if (callbackFunction != null) {
            callbackFunction.addTask(tweetTask, userId);
        } else {
            throw new IllegalArgumentException("Callback function is null, it must be set by the service.");
        }
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

    @Override
    public int getListSizeByUserId(long userId) {
        return 0;
    }

    protected TweetTask getTweetTask(long userId) {
        TweetTask tweetTask = new TweetTask(new ArrayList<>(processingList.get(userId)));
        processingList.get(userId).clear();
        return tweetTask;
    }
}
