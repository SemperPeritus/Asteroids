package com.platonefimov.asteroids;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.platonefimov.asteroids.managers.GameKeys;
import com.platonefimov.asteroids.managers.InputProcessor;
import com.platonefimov.asteroids.managers.Jukebox;
import com.platonefimov.asteroids.managers.StateManager;


public class Game implements ApplicationListener {

    public static int WIDTH;
    public static int HEIGHT;

    public static OrthographicCamera camera;

    private StateManager stateManager;


    public void create() {
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.translate(WIDTH / 2, HEIGHT / 2);
        camera.update();

        Gdx.input.setInputProcessor(new InputProcessor());

        Jukebox.init();

        stateManager = new StateManager();
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateManager.update(Gdx.graphics.getDeltaTime());
        stateManager.draw();

        GameKeys.update();
    }

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {

    }

}
