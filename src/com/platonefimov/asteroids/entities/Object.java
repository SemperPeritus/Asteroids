package com.platonefimov.asteroids.entities;


import com.platonefimov.asteroids.Game;


public abstract class Object {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected float deltaX;
    protected float deltaY;
    protected float speed;

    protected float radians;
    protected float rotationSpeed;

    protected float shapeX[];
    protected float shapeY[];


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float[] getShapeX() {
        return shapeX;
    }

    public float[] getShapeY() {
        return shapeY;
    }


    public boolean intersects(Object object) {
        float[] sectX = object.getShapeX();
        float[] sectY = object.getShapeY();
        for (int i = 0; i < sectX.length; i++)
            if (contains(sectX[i], sectY[i]))
                return true;
        return false;
    }


    public boolean contains(float x, float y) {
        boolean bool = false;
        for (int i = 0, j = shapeX.length - 1; i < shapeX.length; j = i++) {
            if ((shapeY[i] > y) != (shapeY[j] > y) &&
                    (x < (shapeX[j] - shapeX[i]) * (y - shapeY[i]) / (shapeY[j] - shapeY[i]) + shapeX[i]))
                bool = !bool;
        }

        return bool;
    }


    protected void wrap() {
        if (x < 0)
            x = Game.WIDTH;
        if (y < 0)
            y = Game.HEIGHT;
        if (x > Game.WIDTH)
            x = 0;
        if (y > Game.HEIGHT)
            y = 0;
    }

}
