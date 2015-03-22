package com.contrastofbeauty.tutorial.collectors;

import com.contrastofbeauty.tutorial.api.domain.Callback;
import com.contrastofbeauty.tutorial.domain.FacebookPost;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FacebookCollectorTddTest {

    private static final long USER_ID = 1L;

    private FacebookCollector collector;

    private FacebookPost facebookPost;

    @Mock
    private Callback callbackMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        collector = new FacebookCollector();
        facebookPost = new FacebookPost("Just a foo post");
    }

    @Test
    public void testAcceptGoldenPath() throws Exception {
        String message = "Object " + facebookPost + " for user " + USER_ID + " has not been accepted.";
        assertTrue(message, collector.accept(facebookPost, USER_ID));
    }

    @Test
    public void testAcceptWrongObjectType() throws Exception {
        Integer fooObject = new Integer(123);
        String message = "Object " + fooObject + " for user " + USER_ID + " must not be accepted.";
        assertFalse(message, collector.accept(fooObject, USER_ID));
    }

    @Test
    public void testAcceptCallFlushMethod() throws Exception {

        collector.setNewBufferSize(2);
        collector.setCallbackFunction(callbackMock);

        collector.accept(facebookPost, USER_ID);
        collector.accept(facebookPost, USER_ID);

        verify(callbackMock, times(1)).addTask(any(Callable.class), anyInt());
    }

    @Test
    public void testFlushGoldenPath() throws Exception {

        collector.setCallbackFunction(callbackMock);

        collector.accept(facebookPost, USER_ID);
        collector.accept(facebookPost, USER_ID);

        String message1 = "The list size for the user " + USER_ID + " must be equal to 2";
        assertEquals(message1, 2, collector.getListSizeByUserId(USER_ID));

        collector.flush(USER_ID);
        String message2 = "The list size after a call to flush() must be 0";
        assertEquals(message2, 0, collector.getListSizeByUserId(USER_ID));

        verify(callbackMock, times(1)).addTask(any(Callable.class), anyInt());
    }
}
