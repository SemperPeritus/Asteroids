package com.platonefimov.asteroids.entities;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;


public class Particle extends Object {

    private float lifetime;
    private float lifetimer;
    private boolean remove;

    private float green;


    public Particle(float x, float y) {
        this.x = x;
        this.y = y;
        width = height = 2;
        green = 0f;
        speed = 50;
        lifetime = 1f;

        radians = MathUtils.random(6,2832f);
        deltaX = MathUtils.cos(radians) * speed;
        deltaY = MathUtils.sin(radians) * speed;
    }

    public Particle(float x, float y, float green) {
        this.x = x;
        this.y = y;
        width = height = 2;
        this.green = green;
        speed = MathUtils.random(40, 60);
        lifetime = 1f;

        radians = MathUtils.random(6,2832f);
        deltaX = MathUtils.cos(radians) * speed;
        deltaY = MathUtils.sin(radians) * speed;
    }


    public boolean shouldRemove() {
        return remove;
    }


    public void update(float deltaTime) {
        x += deltaX * deltaTime;
        y += deltaY * deltaTime;

        lifetimer += deltaTime;
        if (lifetimer > lifetime)
            remove = true;
    }


    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, green, 0, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.circle(x - width / 2, y - width / 2, width / 4);

        shapeRenderer.end();
    }

}
