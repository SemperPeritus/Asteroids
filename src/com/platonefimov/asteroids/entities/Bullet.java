package com.platonefimov.asteroids.entities;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends Object {

    private float lifeTime;
    private float lifeTimer;

    private boolean remove;


    public Bullet(float x, float y, float radians) {
        this.x = x;
        this.y = y;
        this.radians = radians;

        float speed = 350;

        deltaX = MathUtils.cos(radians) * speed;
        deltaY = MathUtils.sin(radians) * speed;

        width = height = 2;

        lifeTime = 1;
        lifeTimer = 0;
    }


    public boolean shouldRemove() {
        return remove;
    }


    public void update(float deltaTime) {
        x += deltaX * deltaTime;
        y += deltaY * deltaTime;

        wrap();

        lifeTimer += deltaTime;
        if (lifeTimer > lifeTime)
            remove = true;
    }


    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.circle(x - width / 2, y - height / 2, width / 2);

        shapeRenderer.end();
    }

}
