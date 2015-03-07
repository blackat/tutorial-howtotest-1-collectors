package com.contrastofbeauty.tutorial.services;

import com.contrastofbeauty.tutorial.collectors.TweetCollector;
import com.contrastofbeauty.tutorial.domain.interfaces.Callback;
import com.contrastofbeauty.tutorial.services.interfaces.Service;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CloudServiceTest {

    public static final long USER_ID = 1L;
    private Service cloudService;

    @Mock
    private TweetCollector tweetCollectorMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        cloudService = new CloudService();
    }

    @Test
    public void testAddCollectorGoldenPath() {

        cloudService.addCollector(new TweetCollector());

        assertEquals(1, cloudService.getCollectorSize());
    }

    @Test
    public void testAddCollectorVerifyCallbackFunctionAddedWithOverride() {

        final AtomicInteger functionCalled = new AtomicInteger();

        cloudService.addCollector(new TweetCollector(){

            @Override
            public void setCallbackFunction(Callback callback) {

                functionCalled.set(1);
            }
        });

        assertEquals(1, functionCalled.get());
    }

    @Test
    public void testAddCollectorVerifyCallbackFunctionAddedWithMockito() {

        cloudService.addCollector(tweetCollectorMock);
        verify(tweetCollectorMock, times(1)).setCallbackFunction(any(Callback.class));
    }

    @Test
    public void testOpenConnectionGoldenPath() {
        cloudService.openConnection(USER_ID);
        assertTrue(cloudService.isUserConnected(USER_ID));
    }

    @Test
    public void testOpenConnectionUserHasNotOpenConnection() {
        assertFalse(cloudService.isUserConnected(USER_ID));
    }

    @Test
    public void testSaveObjectGoldenPath() throws Exception {
        //TODO
        assertTrue(true);
    }

    @Test
    public void testSaveObjectUserNotConnected() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User with id " + USER_ID + " has not open any connection, please open a connection " +
                "before trying to save.");

        cloudService.saveObject(mock(Callable.class), USER_ID);
    }
}
