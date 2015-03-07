package com.contrastofbeauty.tutorial.api.domain;

import com.contrastofbeauty.tutorial.api.services.Service;
import java.util.concurrent.Callable;

public interface Callback<T extends Callable> {

    void addTask(T task, long userId);

    void setService(Service service);
}
