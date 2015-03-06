package com.platonefimov.asteroids.managers;


import com.badlogic.gdx.Gdx;

import java.io.*;



public class SaveData {

    public static HighscoresData highscoresData;


    private static void init() {
        highscoresData = new HighscoresData();
        highscoresData.init();
        save();
    }


    public static void save() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("highscores.dat"));
            outputStream.writeObject(highscoresData);
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }


    public static void load() {
        try {
            if (!saveFileExists()) {
                init();
                return;
            }
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("highscores.dat"));
            highscoresData = (HighscoresData) inputStream.readObject();
            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }


    private static boolean saveFileExists() {
        File file = new File("highscores.dat");
        return file.exists();
    }

}
