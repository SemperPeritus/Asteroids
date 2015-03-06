package com.platonefimov.asteroids.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.platonefimov.asteroids.Game;
import com.platonefimov.asteroids.managers.GameKeys;
import com.platonefimov.asteroids.managers.SaveData;
import com.platonefimov.asteroids.managers.StateManager;



public class HighScoresState extends GameState {

    private SpriteBatch spriteBatch;

    private BitmapFont hyperspaceBold;

    private long[] highScores;
    private String[] names;


    public HighScoresState(StateManager stateManager) {
        super(stateManager);
    }


    public void init() {
        spriteBatch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        hyperspaceBold = generator.generateFont(parameter);
        hyperspaceBold.setColor(1, 1, 1, 1);

        SaveData.load();
        highScores = SaveData.highscoresData.getHighScores();
        names = SaveData.highscoresData.getNames();
    }


    public void update(float deltaTime) {
        handleInput();
    }


    public void draw() {
        spriteBatch.setProjectionMatrix(Game.camera.combined);

        spriteBatch.begin();

        String title = "HIGHSCORES";
        hyperspaceBold.draw(spriteBatch, title, (Game.WIDTH - hyperspaceBold.getBounds(title).width) / 2, 300);

        String print;
        float width;
        for (int i = 0; i < highScores.length; i++) {
            print = String.format("%2d. %7d %s", i+1, highScores[i], names[i]);
            width = hyperspaceBold.getBounds(print).width;
            hyperspaceBold.draw(spriteBatch, print, (Game.WIDTH - width) / 2, 270 - 20 * i);
        }

        spriteBatch.end();
    }


    public void handleInput() {
        if (GameKeys.isPressed(GameKeys.ENTER) || GameKeys.isPressed(GameKeys.ESCAPE))
            stateManager.setState(StateManager.MENU);
    }


    public void dispose() {
        super.dispose();

        spriteBatch.dispose();
        hyperspaceBold.dispose();
    }

}
