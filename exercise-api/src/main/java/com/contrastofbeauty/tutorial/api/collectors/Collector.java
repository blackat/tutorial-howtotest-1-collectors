package com.contrastofbeauty.tutorial.api.collectors;

import com.contrastofbeauty.tutorial.api.domain.Callback;

public interface Collector {

    boolean accept(Object object, long userId);

    void flush(long userId);

    void postFlush(long userId);

    void setCallbackFunction(Callback callback);

    void setNewBufferSize(int bufferSize);

    int getListSizeByUserId(long userId);
}
