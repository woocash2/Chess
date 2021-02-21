package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.utils.SoundProvider;

import javax.sound.sampled.Clip;

public class SoundPlayer {

    Clip moveClip;
    Clip captureClip;
    Clip notifyClip;

    public SoundPlayer() {
        moveClip = SoundProvider.getMoveSound();
        captureClip = SoundProvider.getCaptureSound();
        notifyClip = SoundProvider.getNotifySound();
    }

    public void playMove() {
        moveClip.setMicrosecondPosition(0);
        moveClip.start();
    }

    public void playCapture() {
        captureClip.setMicrosecondPosition(0);
        captureClip.start();
    }

    public void playNotify() {
        notifyClip.setMicrosecondPosition(0);
        notifyClip.start();
    }
}
