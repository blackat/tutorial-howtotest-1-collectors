package com.contrastofbeauty.tutorial.api.domain;

public interface Callback<T> {

    void addTask(T task, long userId);
}
