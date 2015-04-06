package com.contrastofbeauty.tutorial.collectors;

import com.contrastofbeauty.tutorial.api.collectors.Collector;
import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.domain.FacebookPost;
import com.contrastofbeauty.tutorial.domain.FacebookTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacebookCollector implements Collector {

    private static final int BUFFER_SIZE = 10;

    private int newBufferSize;

    private Map<Long, List<FacebookPost>> facebookPostMap;

    private Callback callbackFunction;

    public FacebookCollector() {
        facebookPostMap = new HashMap<>();
    }

    @Override
    public boolean accept(Object object, long userId) {

        if (object instanceof FacebookPost) {
            if (facebookPostMap.get(userId) == null) {
                facebookPostMap.put(userId, new ArrayList<FacebookPost>());
            }

            facebookPostMap.get(userId).add((FacebookPost) object);

            int bufferSize = newBufferSize != 0 ? newBufferSize : BUFFER_SIZE;

            if (facebookPostMap.get(userId).size() == bufferSize) {
                flush(userId);
            }

            return true;
        }

        return false;
    }


    @Override
    public void flush(long userId) {

        List<FacebookPost> list = new ArrayList<>(facebookPostMap.get(userId));
        facebookPostMap.get(userId).clear();

        FacebookTask facebookTask = new FacebookTask(list);

        callbackFunction.addTask(facebookTask, userId);
    }

    @Override
    public void postFlush(long userId) {

    }

    @Override
    public void setCallbackFunction(Callback callback) {
        callbackFunction = callback;
    }

    @Override
    public void setNewBufferSize(int bufferSize) {
        newBufferSize = bufferSize;
    }

    @Override
    public int getListSizeByUserId(long userId) {
        return facebookPostMap.get(userId).size();
    }
}
