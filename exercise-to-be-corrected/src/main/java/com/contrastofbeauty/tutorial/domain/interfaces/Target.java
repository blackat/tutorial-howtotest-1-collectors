package com.contrastofbeauty.tutorial.domain.interfaces;

public interface Target {

    void sendAckFailed(RuntimeException exception);

    void sendAckSuccess();
}
