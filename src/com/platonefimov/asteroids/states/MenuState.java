package com.platonefimov.asteroids.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.platonefimov.asteroids.Game;
import com.platonefimov.asteroids.managers.GameKeys;
import com.platonefimov.asteroids.managers.StateManager;



public class MenuState extends GameState {

    private SpriteBatch spriteBatch;

    private BitmapFont hyperspace;
    private BitmapFont hyperspaceBold;

    private int currentItem;
    private String[] menuItems;


    public MenuState(StateManager stateManager) {
        super(stateManager);
    }


    public void init() {
        spriteBatch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        hyperspace = generator.generateFont(parameter);
        hyperspace.setColor(1, 1, 1, 1);
        parameter.size = 56;
        hyperspaceBold = generator.generateFont(parameter);
        hyperspaceBold.setColor(1, 1, 1, 1);

        menuItems = new String[] {
                "Play",
                "Highscores",
                "Quit"
        };
    }


    public void update(float deltaTime) {
        handleInput();
    }


    public void draw() {
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
        if (currentItem == 2)
            Gdx.app.exit();
    }


    public void dispose() {

    }

}
