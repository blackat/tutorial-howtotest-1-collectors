package com.contrastofbeauty.tutorial.api.domain;

public interface Target {

    void sendAckFailed(RuntimeException exception);

    void sendAckSuccess();
}
