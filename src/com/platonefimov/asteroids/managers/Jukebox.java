package com.platonefimov.asteroids.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;



public class Jukebox {

    private static HashMap<String, Sound> sounds;


    static {
        sounds = new HashMap<String, Sound>();
    }


    public static void init() {
        load("bangLarge.wav", "bangLarge");
        load("bangMedium.wav", "bangMedium");
        load("bangSmall.wav", "bangSmall");
        load("beat1.wav", "beat1");
        load("beat2.wav", "beat2");
        load("extraShip.wav", "extraShip");
        load("fire.wav", "fire");
        load("saucerLarge.wav", "saucerLarge");
        load("saucerSmall.wav", "saucerSmall");
        load("thrust.wav", "thrust");
    }


    public static void load(String path, String name) {
        String prefix = "sounds/";
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(prefix + path));
        sounds.put(name, sound);
    }


    public static void play(String name) {
        sounds.get(name).play();
    }

    public static void loop(String name) {
        sounds.get(name).loop();
    }

    public static void stop(String name) {
        sounds.get(name).stop();
    }


    public static void stopAll() {
        for (Sound sound : sounds.values())
            sound.stop();
    }

}
