package com.contrastofbeauty.tutorial.collectors;

import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.domain.CallbackImpl;
import com.contrastofbeauty.tutorial.domain.Tweet;
import com.contrastofbeauty.tutorial.domain.TweetTask;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class TweetCollectorTest {

    private TweetCollector collector;

    @Mock
    private Callback callbackMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        collector = new TweetCollector();
    }

    @Test
    public void testAcceptGoldenPath() throws Exception {
        assertTrue(collector.accept(new Tweet("foo tweet"), 1L));
    }

    @Test
    public void testAcceptWrongObject() throws Exception {
        assertFalse(collector.accept(new Object(), 1L));
    }

    @Test
    public void testFlushGoldenPath() throws Exception {

        collector.setCallbackFunction(callbackMock);
        collector.accept(new Tweet("foo tweet"), 1L);
        collector.flush(1L);

        verify(callbackMock, times(1)).addTask(any(TweetTask.class), anyLong());
    }

    @Test
    public void testFlushExceptionThrownWithNullCallbackFunction() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Callback function is null, it must be set by the service.");
        collector.accept(new Tweet("foo tweet"), 1L);
        collector.flush(1L);
    }

    @Test
    public void testFlushWithSpyGoldenPath() throws Exception {

        TweetCollector spyOnTweetCollector = Mockito.spy(TweetCollector.class);

        spyOnTweetCollector.setCallbackFunction(callbackMock);
        doReturn(new TweetTask(new ArrayList<Tweet>())).when(spyOnTweetCollector).getTweetTask(1L);

        spyOnTweetCollector.flush(1L);

        verify(callbackMock, times(1)).addTask(any(TweetTask.class), anyInt());


        TweetCollector collector = new TweetCollector() {
            @Override
            protected TweetTask getTweetTask(long userId) {
                return new TweetTask(new ArrayList<Tweet>());
            }
        };
    }

    @Test
    public void testFlushWithOverrideGoldenPath() throws Exception {

        final AtomicBoolean taskAdded = new AtomicBoolean();

        Callback callbackFunction = new CallbackImpl() {
            @Override
            public void addTask(TweetTask tweetTask, long userId) {
                taskAdded.set(true);
            }
        };

        TweetCollector collector = new TweetCollector() {
            @Override
            protected TweetTask getTweetTask(long userId) {
                return new TweetTask(new ArrayList<Tweet>());
            }
        };

        collector.setCallbackFunction(callbackFunction);
        collector.flush(1L);

        assertTrue(taskAdded.get());
    }
}