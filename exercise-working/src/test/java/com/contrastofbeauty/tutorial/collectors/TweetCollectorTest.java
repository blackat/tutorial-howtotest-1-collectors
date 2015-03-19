package com.contrastofbeauty.tutorial.collectors;

import com.contrastofbeauty.tutorial.api.collectors.Collector;
import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.domain.Tweet;
import com.contrastofbeauty.tutorial.domain.TweetTask;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TweetCollectorTest {

    public static final long USER_ID = 1L;
    private Collector collector;

    @Mock
    private Callback callbackFunctionMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        collector = new TweetCollector();
    }

    @Test
    public void testAcceptGoldenPath() throws Exception {
        assertTrue(collector.accept(new Tweet("foo tweet"), USER_ID));
    }

    @Test
    public void testAcceptObjectNotAcceptedBecauseDifferentType() throws Exception {
        assertFalse(collector.accept(new Object(), 1L));
    }

    @Test
    public void testFlushGoldenPath() throws Exception {

        collector.setCallbackFunction(callbackFunctionMock);
        collector.accept(new Tweet("foo tweet"), USER_ID);
        collector.flush(USER_ID);

        verify(callbackFunctionMock, times(1)).addTask(any(TweetTask.class), anyInt());
    }

    @Test
    public void testFlushExceptionThrownWithNullCallbackFunction() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Callback function is null, it must be set by the service.");
        collector.accept(new Tweet("foo tweet"), USER_ID);
        collector.flush(USER_ID);
    }

    /*@Test
    public void testFlushWithSpyGoldenPath() throws Exception {

        TweetCollector spyOnTweetCollector = Mockito.spy(TweetCollector.class);

        spyOnTweetCollector.setCallbackFunction(callbackFunctionMock);
        doReturn(new TweetTask(new ArrayList<Tweet>())).when(spyOnTweetCollector).getTweetTask(USER_ID);

        spyOnTweetCollector.flush(USER_ID);

        verify(callbackFunctionMock, times(1)).addTask(any(TweetTask.class), anyInt());
    }

    @Test
    public void testFlushWithOverrideGoldenPath() throws Exception {

        final AtomicBoolean taskAdded = new AtomicBoolean();

        Callback callbackFunction = new CallbackImpl(null){
            @Override
            public void addTask(TweetTask tweetTask, long userId) {
                taskAdded.set(true);
            }
        };

        TweetCollector collector = new TweetCollector(){
            @Override
            protected TweetTask getTweetTask(long userId) {
                return new TweetTask(new ArrayList<Tweet>());
            }
        };

        collector.setCallbackFunction(callbackFunction);
        collector.flush(USER_ID);

        assertTrue(taskAdded.get());
    }
*/
    @Test
    public void testPostFlush() throws Exception {

    }

    @Test
    public void testSetCallbackFunction() throws Exception {

    }

    @Test
    public void testSetNewBufferSize() throws Exception {

    }
}
