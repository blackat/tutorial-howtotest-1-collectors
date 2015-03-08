package com.contrastofbeauty.tutorial.services;

import com.contrastofbeauty.tutorial.api.collectors.Collector;
import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.api.domain.AcknoledgeService;
import com.contrastofbeauty.tutorial.api.services.Service;
import com.contrastofbeauty.tutorial.collectors.TweetCollector;
import com.contrastofbeauty.tutorial.domain.CallbackImpl;
import com.contrastofbeauty.tutorial.domain.Tweet;
import com.contrastofbeauty.tutorial.domain.TweetTask;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CloudServiceTest {

    public static final long USER_ID = 1L;
    private Service cloudService;

    @Mock
    private TweetCollector tweetCollectorMock;

    @Spy
    private CallbackImpl spyOnCallbackFunction;

    @Mock
    private Callback callbackFunctionMock;

    @Mock
    private Callable tweetMock;

    @Mock
    private AcknoledgeService acknoledgeServiceMock;

    @Mock
    private TweetTask tweetTaskMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        spyOnCallbackFunction = new CallbackImpl();
        cloudService = new CloudService(spyOnCallbackFunction);
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
    public void testSaveObjectUserNotConnected() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User with id " + USER_ID + " has not open any connection, please open a connection " +
                "before trying to save.");

        cloudService.saveObject(mock(Callable.class), USER_ID);
    }

    @Test
    public void testSaveObjectWithOverrideGoldenPath() throws Exception {

        final AtomicBoolean accepted = new AtomicBoolean();

        Collector tweetCollector = new TweetCollector(){
            @Override
            public boolean accept(Object object, long userId) {

                if (object instanceof Tweet) {
                    accepted.set(true);
                    return true;
                }

                return false;
            }
        };

        cloudService.addCollector(tweetCollector);

        cloudService.openConnection(USER_ID);

        cloudService.saveObject(new Tweet("foo tweet"), USER_ID);

        assertTrue(accepted.get());
    }

    @Test
    public void testSaveObjectWithMockitoGoldenPath() throws Exception {

        Collector collectorMock = mock(TweetCollector.class);
        when(collectorMock.accept(any(Tweet.class), anyInt())).thenReturn(true);

        cloudService.addCollector(collectorMock);

        cloudService.openConnection(USER_ID);

        cloudService.saveObject(tweetMock, USER_ID);

        verify(collectorMock, times(1)).accept(any(Tweet.class), anyInt());
    }

    @Test
    public void testSaveObjectObjectNotAcceptedThrowException() throws Exception {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Entity of type " + new Object().getClass() + " cannot be accepted.");

        cloudService.addCollector(new TweetCollector());

        cloudService.openConnection(USER_ID);

        cloudService.saveObject(new Object(), USER_ID);
    }

    @Test
    public void testSaveObjectCompletedUserNotConnectedThrowException() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User with id " + USER_ID + " has not open any connection, please open a connection " +
                "before trying to save.");

        cloudService.saveObjectCompleted(null, USER_ID);
    }

    @Test
    public void testSaveObjectCompletedGoldenPath() throws Exception {

        doNothing().when(acknoledgeServiceMock).sendAckSuccess();

        TweetCollector spyOntweetCollector = spy(new TweetCollector());
        // if you call the real method there will be an exception, use doReturn for stubbing
        doReturn(tweetTaskMock).when(spyOntweetCollector).getTweetTask(USER_ID);
        when(tweetTaskMock.call()).thenReturn(1L);

        cloudService.addCollector(spyOntweetCollector);

        cloudService.openConnection(USER_ID);

        cloudService.saveObject(new Tweet("foo tweet"), USER_ID);

        cloudService.saveObjectCompleted(acknoledgeServiceMock, USER_ID);

        verify(tweetTaskMock, times(1)).call();
        verify(acknoledgeServiceMock, times(1)).sendAckSuccess();
    }

    @Test
    public void testSaveObjectCompletedExceptionThrown() throws Exception {
        doNothing().when(acknoledgeServiceMock).sendAckSuccess();

        TweetCollector spyOntweetCollector = spy(new TweetCollector());
        // if you call the real method there will be an exception, use doReturn for stubbing
        doReturn(tweetTaskMock).when(spyOntweetCollector).getTweetTask(USER_ID);
        when(tweetTaskMock.call()).thenThrow(InterruptedException.class);

        cloudService.addCollector(spyOntweetCollector);

        cloudService.openConnection(USER_ID);

        cloudService.saveObject(new Tweet("foo tweet"), USER_ID);

        cloudService.saveObjectCompleted(acknoledgeServiceMock, USER_ID);

        verify(tweetTaskMock, times(1)).call();
        verify(acknoledgeServiceMock, times(1)).sendAckFailed(any(RuntimeException.class));
    }
}
