package com.platonefimov.asteroids.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import com.platonefimov.asteroids.Game;
import com.platonefimov.asteroids.entities.Asteroid;
import com.platonefimov.asteroids.managers.GameKeys;
import com.platonefimov.asteroids.managers.SaveData;
import com.platonefimov.asteroids.managers.StateManager;

import java.util.ArrayList;



public class MenuState extends GameState {

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont hyperspace;
    private BitmapFont hyperspaceBold;

    private int currentItem;
    private String[] menuItems;

    private ArrayList<Asteroid> asteroids;


    public MenuState(StateManager stateManager) {
        super(stateManager);
    }


    public void init() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        hyperspace = generator.generateFont(parameter);
        hyperspace.setColor(1, 1, 1, 1);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
        parameter.size = 56;
        hyperspaceBold = generator.generateFont(parameter);
        hyperspaceBold.setColor(1, 1, 1, 1);

        menuItems = new String[] {
                "Play",
                "Highscores",
                "Quit"
        };

        SaveData.load();

        asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < 6; i++)
            asteroids.add(new Asteroid(MathUtils.random(Game.WIDTH), MathUtils.random(Game.HEIGHT), Asteroid.LARGE));
    }


    public void update(float deltaTime) {
        handleInput();

        for (Asteroid asteroid : asteroids)
            asteroid.update(deltaTime);
    }


    public void draw() {
        shapeRenderer.setProjectionMatrix(Game.camera.combined);
        for (Asteroid asteroid : asteroids)
            asteroid.draw(shapeRenderer);

        spriteBatch.setProjectionMatrix(Game.camera.combined);
        spriteBatch.begin();

        String title = "Asteroids";
        float width = hyperspaceBold.getBounds(title).width;
        hyperspaceBold.draw(spriteBatch, title, (Game.WIDTH - width) / 2, 300);

        for (int i = 0; i < menuItems.length; i++) {
            width = hyperspace.getBounds(menuItems[i]).width;
            if (currentItem == i)
                hyperspace.setColor(1, 0, 0, 1);
            else
                hyperspace.setColor(1, 1, 1, 1);
            hyperspace.draw(spriteBatch, menuItems[i], (Game.WIDTH - width) / 2, 200 - 35 * i);
        }

        spriteBatch.end();
    }


    public void handleInput() {
        if (GameKeys.isPressed(GameKeys.UP) && currentItem > 0)
            currentItem--;
        if (GameKeys.isPressed(GameKeys.DOWN) && currentItem < menuItems.length - 1)
            currentItem++;
        if (GameKeys.isPressed(GameKeys.ENTER))
            select();
    }


    private void select() {
        if (currentItem == 0)
            stateManager.setState(StateManager.PLAY);
        if (currentItem == 1)
            stateManager.setState(StateManager.HIGHSCORES);
        if (currentItem == 2)
            Gdx.app.exit();
    }


    public void dispose() {
        super.dispose();

        spriteBatch.dispose();
        shapeRenderer.dispose();
        hyperspace.dispose();
        hyperspaceBold.dispose();
    }

}
