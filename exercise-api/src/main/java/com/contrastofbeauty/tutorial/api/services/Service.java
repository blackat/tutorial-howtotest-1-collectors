package com.contrastofbeauty.tutorial.api.services;

import com.contrastofbeauty.tutorial.api.collectors.Collector;
import com.contrastofbeauty.tutorial.api.domain.AcknoledgeService;
import java.util.concurrent.Callable;

public interface Service {

    void openConnection(long processId) throws RuntimeException;

    void saveObject(Object object, long processId) throws RuntimeException;

    void saveObjectCompleted(AcknoledgeService acknoledgeService, long processId) throws RuntimeException;

    void addCollector(Collector collector) throws RuntimeException;

    void submitTask(Callable<Long> task, long userId);

    int getCollectorSize();

    boolean isUserConnected(long userId) throws RuntimeException;
}
