package com.contrastofbeauty.tutorial.services;

import com.contrastofbeauty.tutorial.api.collectors.Collector;
import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.api.domain.AcknoledgeService;
import com.contrastofbeauty.tutorial.api.services.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

public class CloudService implements Service {

    private final static Logger LOGGER = Logger.getLogger(CloudService.class);

    private static final int THREAD_POOL_SIZE = 10;

    private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private List<Collector> processingCollectors;

    private Map<Long, List<Future>> processingFutureList;

    private Callback callbackFunction;

    public CloudService(Callback callback) {

        // 3) error: NPE will be produce without init the map - - REMOVE THIS LINE TO HAVE THE ERROR
        processingFutureList = new HashMap<>();

        // 4) error: NPE will be produce without init the map - - REMOVE THIS LINE TO HAVE THE ERROR
        processingCollectors = new ArrayList<>();

        callbackFunction = callback;
        callbackFunction.setService(this);
    }

    @Override
    public void addCollector(Collector collector) throws RuntimeException {

        // 5) error: without this call no collector is added so an exception is thrown - - REMOVE THIS LINE TO HAVE THE
        // ERROR (Easy)
        processingCollectors.add(collector);

        // 6) error: without this call a collector cannot submit anything - - REMOVE THIS LINE TO HAVE THE ERROR (Hard)
        collector.setCallbackFunction(callbackFunction);
    }

    @Override
    public void submitTask(Callable<Long> task, long userId) {

        // 7) error: without the initialization of the map a NPE will be thrown - - REMOVE THIS LINE TO HAVE THE ERROR
        // (Easy)
        if (processingFutureList.get(userId) == null) {
            processingFutureList.put(userId, new ArrayList<Future>());
        }

        Future<Long> submit = executorService.submit(task);

        processingFutureList.get(userId).add(submit);
    }

    @Override
    public int getCollectorSize() {
        return processingCollectors.size();
    }

    @Override
    public boolean isUserConnected(long userId) throws RuntimeException {

        return processingFutureList.get(userId) != null;
    }

    @Override
    public void openConnection(long userId) throws RuntimeException {

        // 8) error: without the initialization list for a given user it want be connected - REMOVE THIS LINE TO HAVE
        // THE ERROR
        // (Easy)
        processingFutureList.put(userId, new ArrayList<Future>());
    }

    @Override
    public void saveObject(Object object, long userId) throws RuntimeException {

        boolean accepted = false;

        if (!isUserConnected(userId)) {
            throw new IllegalArgumentException("User with id " + userId + " has not open any connection, please open a connection before trying to save.");
        }

        for (Collector collector : processingCollectors) {
            accepted |= collector.accept(object, userId);
        }

        if (!accepted) {
            throw new IllegalArgumentException("Entity of type " + object.getClass() + " cannot be " +
                    "accepted.");
        }
    }

    @Override
    public void saveObjectCompleted(AcknoledgeService acknoledgeService, long userId) throws RuntimeException {

        if (!isUserConnected(userId)) {
            throw new IllegalArgumentException("User with id " + userId + " has not open any connection, please open a connection before trying to save.");
        }

        // call flush method to force not completed collections to be processed
        for (Collector collector : processingCollectors) {
            collector.flush(userId);
        }

        final List<Future> activeTasks = processingFutureList.get(userId);

        // wait that all the threads for a given process id have terminated
        for (Future<Long> activeTask : activeTasks) {
            try {
                Long taskResult = activeTask.get();
                LOGGER.info(taskResult + " tweets have been posted to Twitter.");
            } catch (InterruptedException e) {
                LOGGER.error(e);
                acknoledgeService.sendAckFailed(new RuntimeException(e));
            } catch (ExecutionException e) {
                LOGGER.error(e);
                acknoledgeService.sendAckFailed(new RuntimeException(e));
            }
        }

        // execute some optional post processing once data have been saved
        for (Collector collector : processingCollectors) {
            collector.postFlush(userId);
        }

        LOGGER.info("cloud service has finished the work for process id " + userId + ".");

        executorService.shutdown();
        acknoledgeService.sendAckSuccess();
    }
}
