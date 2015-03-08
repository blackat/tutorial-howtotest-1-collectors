package com.contrastofbeauty.tutorial.api.domain;

public interface AcknoledgeService {

    void sendAckFailed(RuntimeException exception);

    void sendAckSuccess();
}
