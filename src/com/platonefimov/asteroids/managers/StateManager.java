package com.platonefimov.asteroids.managers;


import com.platonefimov.asteroids.states.GameState;
import com.platonefimov.asteroids.states.PlayState;


public class StateManager {

    private GameState gameState;

    public static final int MENU = 0;
    public static final int PLAY = 1;


    public StateManager() {
        setState(PLAY);
    }


    public void setState(int state) {
        if (gameState != null)
            gameState.dispose();
        if (state == MENU) {
            // gameState = new MenuState(this);
        }
        if (state == PLAY) {
            gameState = new PlayState(this);
        }
    }


    public void update(float deltaTime) {
        gameState.update(deltaTime);
    }


    public void draw() {
        gameState.draw();
    }
}
