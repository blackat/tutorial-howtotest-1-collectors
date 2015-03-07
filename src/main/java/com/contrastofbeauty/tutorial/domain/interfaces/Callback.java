package com.contrastofbeauty.tutorial.domain.interfaces;

import com.contrastofbeauty.tutorial.domain.TweetTask;

public interface Callback {

    void addTask(TweetTask tweetTask, long userId);
}
