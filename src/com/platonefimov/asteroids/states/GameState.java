package com.platonefimov.asteroids.states;


import com.platonefimov.asteroids.managers.StateManager;


public abstract class GameState {

    protected StateManager stateManager;


    protected GameState(StateManager stateManager) {
        this.stateManager = stateManager;
        init();
    }

    public abstract void init();
    public abstract void update(float deltaTime);
    public abstract void draw();
    public abstract void handleInput();
    public abstract void dispose();

}
