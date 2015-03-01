package com.platonefimov.asteroids.managers;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;


public class InputProcessor extends InputAdapter {

    public boolean keyDown(int key) {
        if (key == Input.Keys.UP)
            GameKeys.setKey(GameKeys.UP, true);
        if (key == Input.Keys.LEFT)
            GameKeys.setKey(GameKeys.LEFT, true);
        if (key == Input.Keys.DOWN)
            GameKeys.setKey(GameKeys.DOWN, true);
        if (key == Input.Keys.RIGHT)
            GameKeys.setKey(GameKeys.RIGHT, true);
        if (key == Input.Keys.ENTER)
            GameKeys.setKey(GameKeys.ENTER, true);
        if (key == Input.Keys.ESCAPE)
            GameKeys.setKey(GameKeys.ESCAPE, true);
        if (key == Input.Keys.SPACE)
            GameKeys.setKey(GameKeys.SPACE, true);
        if (key == Input.Keys.SHIFT_LEFT || key == Input.Keys.SHIFT_RIGHT)
            GameKeys.setKey(GameKeys.SHIFT, true);

        return true;
    }

    public boolean keyUp(int key) {
        if (key == Input.Keys.UP)
            GameKeys.setKey(GameKeys.UP, false);
        if (key == Input.Keys.LEFT)
            GameKeys.setKey(GameKeys.LEFT, false);
        if (key == Input.Keys.DOWN)
            GameKeys.setKey(GameKeys.DOWN, false);
        if (key == Input.Keys.RIGHT)
            GameKeys.setKey(GameKeys.RIGHT, false);
        if (key == Input.Keys.ENTER)
            GameKeys.setKey(GameKeys.ENTER, false);
        if (key == Input.Keys.ESCAPE)
            GameKeys.setKey(GameKeys.ESCAPE, false);
        if (key == Input.Keys.SPACE)
            GameKeys.setKey(GameKeys.SPACE, false);
        if (key == Input.Keys.SHIFT_LEFT || key == Input.Keys.SHIFT_RIGHT)
            GameKeys.setKey(GameKeys.SHIFT, false);

        return true;
    }

}
