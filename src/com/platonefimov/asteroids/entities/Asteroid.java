package com.platonefimov.asteroids.entities;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.platonefimov.asteroids.managers.Jukebox;

public class Asteroid extends Object {

    private int type;
    public static final int SMALL = 0;
    public static final int MEDIUM = 1;
    public static final int LARGE = 2;

    private int numPoints;
    private float[] distance;

    private boolean remove;

    private int score;


    public Asteroid(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        remove = false;

        if (type == SMALL) {
            numPoints = 8;
            width = height = 12;
            speed = MathUtils.random(70, 100);
            score = 100;
        }
        if (type == MEDIUM) {
            numPoints = 10;
            width = height = 20;
            speed = MathUtils.random(50, 60);
            score = 50;
        }
        if (type == LARGE) {
            numPoints = 12;
            width = height = 40;
            speed = MathUtils.random(20, 30);
            score = 20;
        }

        rotationSpeed = MathUtils.random(-1, 1);
        radians = MathUtils.random(2 * MathUtils.PI);
        deltaX = MathUtils.cos(radians) * speed;
        deltaY = MathUtils.sin(radians) * speed;

        shapeX = new float[numPoints];
        shapeY = new float[numPoints];
        distance = new float[numPoints];

        int radius = (int) (width / 2);
        for (int i = 0; i < numPoints; i++)
            distance[i] = MathUtils.random(radius / 2, radius);

        setShape();
    }


    public int getType() {
        return type;
    }

    public boolean shouldRemove() {
        return remove;
    }


    public void setShape() {
        float angle = 0;
        for (int i = 0; i < numPoints; i++) {
            shapeX[i] = x + MathUtils.cos(angle + radians) * distance[i];
            shapeY[i] = y + MathUtils.sin(angle + radians) * distance[i];
            angle += 2 * MathUtils.PI / numPoints;
        }
    }

    public int getScore() {
        return score;
    }


    public void playBang() {
        if (type == Asteroid.LARGE)
            Jukebox.play("bangLarge");
        if (type == Asteroid.MEDIUM)
            Jukebox.play("bangMedium");
        if (type == Asteroid.SMALL)
            Jukebox.play("bangSmall");
    }


    public void update(float deltaTime) {
        x += deltaX * deltaTime;
        y += deltaY * deltaTime;

        radians += rotationSpeed * deltaTime;
        setShape();

        wrap();
    }


    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int i = 0, j = shapeX.length - 1; i < shapeX.length; j = i++)
            shapeRenderer.line(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);

        shapeRenderer.end();
    }

}
