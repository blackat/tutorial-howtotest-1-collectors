package com.contrastofbeauty.tutorial.domain;

import java.util.List;
import java.util.concurrent.Callable;

public class FacebookTask implements Callable<Long> {

    public FacebookTask(List<FacebookPost> list) {
    }

    @Override
    public Long call() throws Exception {
        return null;
    }
}
