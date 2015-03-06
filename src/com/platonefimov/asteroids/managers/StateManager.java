package com.platonefimov.asteroids.managers;


import com.platonefimov.asteroids.states.*;


public class StateManager {

    private GameState gameState;

    public static final int MENU = 0;
    public static final int PLAY = 1;
    public static final int HIGHSCORES = 2;
    //public static final int PAUSE = 10;
    public static final int GAME_OVER = 11;


    public StateManager() {
        setState(MENU);
    }


    public void setState(int state) {
        if (gameState != null)
            gameState.dispose();

        if (state == MENU)
            gameState = new MenuState(this);
        if (state == PLAY)
            gameState = new PlayState(this);
        if (state == HIGHSCORES)
            gameState = new HighScoresState(this);
        if (state == GAME_OVER)
            gameState = new GameOverState(this);
    }


    public void update(float deltaTime) {
        gameState.update(deltaTime);
    }


    public void draw() {
        gameState.draw();
    }
}
