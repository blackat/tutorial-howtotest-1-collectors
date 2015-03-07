package com.contrastofbeauty.tutorial.collectors.interfaces;

import com.contrastofbeauty.tutorial.domain.interfaces.Callback;

public interface Collector {

    boolean accept(Object object, long userId);

    void flush(long userId);

    void postFlush(long userId);

    void setCallbackFunction(Callback callback);

    void setNewBufferSize(int bufferSize);
}
