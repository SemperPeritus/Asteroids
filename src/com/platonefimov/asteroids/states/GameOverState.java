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



public class GameOverState extends GameState {

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont hyperspace;
    private BitmapFont hyperspaceBold;

    private boolean isHighScore;
    private long score;
    private char[] name;
    private int currentChar;


    public GameOverState(StateManager stateManager) {
        super(stateManager);
    }


    public void init() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        score = SaveData.highscoresData.getTentativeScore();
        isHighScore = SaveData.highscoresData.isHighScore(score);
        if (isHighScore) {
            name = new char[] {'A', 'A', 'A'};
            currentChar = 0;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        hyperspace = generator.generateFont(parameter);
        hyperspace.setColor(1, 1, 1, 1);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
        parameter.size = 32;
        hyperspaceBold = generator.generateFont(parameter);
        hyperspaceBold.setColor(1, 1, 1, 1);
    }


    public void update(float deltaTime) {
        if (isHighScore)
            handleInput();
        else
            if (GameKeys.isPressed(GameKeys.ENTER) || GameKeys.isPressed(GameKeys.ESCAPE))
                stateManager.setState(StateManager.MENU);
    }


    public void draw() {
        spriteBatch.setProjectionMatrix(Game.camera.combined);

        spriteBatch.begin();

        String title = "Game Over";
        hyperspaceBold.draw(spriteBatch, title, (Game.WIDTH - hyperspaceBold.getBounds(title).width) / 2, 220);

        if (!isHighScore) {
            spriteBatch.end();
            return;
        }

        String newHighScore = "New High Score:" + score;
        hyperspace.draw(spriteBatch, newHighScore, (Game.WIDTH - hyperspace.getBounds(newHighScore).width) / 2, 180);

        for (int i = 0; i < name.length; i++)
            hyperspace.draw(spriteBatch, Character.toString(name[i]), 230 + 14 * i, 120);

        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.line(230 + 14 * currentChar, 100, 244 + 14 * currentChar, 100);

        shapeRenderer.end();
    }


    public void handleInput() {
        if (GameKeys.isPressed(GameKeys.ENTER)) {
            if (isHighScore) {
                SaveData.highscoresData.setHighScore(score, new String(name));
                SaveData.save();
            }
            stateManager.setState(StateManager.MENU);
        }

        if (GameKeys.isPressed(GameKeys.UP)) {
            if (name[currentChar] == ' ')
                name[currentChar] = 'Z';
            else {
                name[currentChar]--;
                if (name[currentChar] < 'A')
                    name[currentChar] = ' ';
            }
        }

        if (GameKeys.isPressed(GameKeys.DOWN)) {
            if (name[currentChar] == ' ')
                name[currentChar] = 'A';
            else {
                name[currentChar]++;
                if (name[currentChar] > 'Z')
                    name[currentChar] = ' ';
            }
        }

        if (GameKeys.isPressed(GameKeys.RIGHT))
            if (currentChar < name.length - 1)
                currentChar++;

        if (GameKeys.isPressed(GameKeys.LEFT))
            if (currentChar > 0)
                currentChar--;
    }


    public void dispose() {
        super.dispose();

        shapeRenderer.dispose();
        spriteBatch.dispose();
        hyperspace.dispose();
        hyperspaceBold.dispose();
    }

}
