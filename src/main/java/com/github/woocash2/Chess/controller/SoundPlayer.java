package com.github.woocash2.Chess.controller;

import com.github.woocash2.Chess.model.utils.SoundProvider;

import javax.sound.sampled.Clip;

public class SoundPlayer {

    int moveCnt = 0;
    int captureCnt = 0;

    // Two clips for move and capture so if the players move fast they can be played simultaneously.
    Clip[] moveClips;
    Clip[] captureClips;
    Clip notifyClip;

    public SoundPlayer() {
        moveClips = new Clip[2];
        captureClips = new Clip[2];
        moveClips[0] = SoundProvider.getMoveSound();
        moveClips[1] = SoundProvider.getMoveSound();
        captureClips[0] = SoundProvider.getCaptureSound();
        captureClips[1] = SoundProvider.getCaptureSound();
        notifyClip = SoundProvider.getNotifySound();
    }

    public void playMove() {
        moveClips[moveCnt].stop();
        moveClips[moveCnt].flush();
        moveClips[moveCnt].setMicrosecondPosition(0);
        moveClips[moveCnt].start();
        moveCnt = 1 - moveCnt;
    }

    public void playCapture() {
        captureClips[moveCnt].stop();
        captureClips[moveCnt].flush();
        captureClips[captureCnt].setMicrosecondPosition(0);
        captureClips[captureCnt].start();
        captureCnt = 1 - captureCnt;
    }

    public void playNotify() {
        notifyClip.stop();
        notifyClip.flush();
        notifyClip.setMicrosecondPosition(0);
        notifyClip.start();
    }
}
