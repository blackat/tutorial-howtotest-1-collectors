package com.contrastofbeauty.tutorial;

import com.contrastofbeauty.tutorial.collectors.TweetCollector;
import com.contrastofbeauty.tutorial.domain.TargetImpl;
import com.contrastofbeauty.tutorial.domain.Tweet;
import com.contrastofbeauty.tutorial.services.CloudService;
import com.contrastofbeauty.tutorial.api.services.Service;

public class RunMeToBeCorrected {

    public static void main(String[] args) {

        Service service = new CloudService();

        service.addCollector(new TweetCollector());

        service.openConnection(1L);
        service.saveObject(new Tweet("I am Felix the awesome cat."), 1L);
        service.saveObjectCompleted(new TargetImpl(), 1L);
    }
}
