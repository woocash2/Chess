package com.github.woocash2.Chess.model.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface SoundProvider {

    public static Clip getMoveSound() {
        return clipByPath("src/main/resources/sounds/Move.wav");
    }

    public static Clip getCaptureSound() {
        return clipByPath("src/main/resources/sounds/Capture.wav");
    }

    public static Clip getNotifySound() {
        return clipByPath("src/main/resources/sounds/GenericNotify.wav");
    }

    public static Clip clipByPath(String path) {
        File file = new File(path);

        if (file.exists()) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                return clip;
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}
