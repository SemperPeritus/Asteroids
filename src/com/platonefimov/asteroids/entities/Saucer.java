package com.platonefimov.asteroids.entities;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import com.platonefimov.asteroids.Game;
import com.platonefimov.asteroids.managers.Jukebox;

import java.util.ArrayList;



public class Saucer extends Object {

    private ArrayList<Bullet> bullets;

    private int type;
    public static final int LARGE = 0;
    public static final int SMALL = 1;

    private int score;

    private float fireTime;
    private float fireTimer;

    private Player player;

    private float pathTime1;
    private float pathTime2;
    private float pathTimer;

    private int direction;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private boolean remove;


    public Saucer(int type, int direction, Player player, ArrayList<Bullet> bullets) {
        this.type = type;
        this.direction = direction;
        this.player = player;
        this.bullets = bullets;

        speed = 70;
        if (direction == LEFT) {
            deltaX = -speed;
            x = Game.WIDTH;
        }
        else if (direction == RIGHT) {
            deltaX = speed;
            x = 0;
        }
        y = MathUtils.random(Game.HEIGHT);

        shapeX = new float[6];
        shapeY = new float[6];
        setShape();

        if (type == LARGE) {
            score = 200;
            Jukebox.loop("saucerLarge");
        }
        else if (type == SMALL) {
            score = 1000;
            Jukebox.loop("saucerSmall");
        }

        fireTime = 1;
        fireTimer = 0;

        pathTime1 = 1;
        pathTime2 = 4;
        pathTimer = 0;
    }


    public int getScore() {
        return score;
    }

    public boolean shouldRemove() {
        return remove;
    }


    private void setShape() {
        if (type == LARGE) {
            shapeX[0] = x - 10;
            shapeY[0] = y;
            shapeX[1] = x - 3;
            shapeY[1] = y - 5;
            shapeX[2] = x + 3;
            shapeY[2] = y - 5;
            shapeX[3] = x + 10;
            shapeY[3] = y;
            shapeX[4] = x + 3;
            shapeY[4] = y + 5;
            shapeX[5] = x - 3;
            shapeY[5] = y + 5;
        }
        else if (type == SMALL) {
            shapeX[0] = x - 6;
            shapeY[0] = y;
            shapeX[1] = x - 2;
            shapeY[1] = y - 3;
            shapeX[2] = x + 2;
            shapeY[2] = y - 3;
            shapeX[3] = x + 6;
            shapeY[3] = y;
            shapeX[4] = x + 2;
            shapeY[4] = y + 3;
            shapeX[5] = x - 2;
            shapeY[5] = y + 3;
        }
    }


    public void update(float deltaTime) {
        if (!player.isHit()) {
            fireTimer += deltaTime;
            if (fireTimer > fireTime) {
                fireTimer = 0;
                if (type == LARGE)
                    radians = MathUtils.random(MathUtils.PI2);
                else if (type == SMALL)
                    radians = MathUtils.atan2(player.getY() - y, player.getX() - x);
                bullets.add(new Bullet(x, y, radians));
                Jukebox.play("fire");
            }
        }

        pathTimer += deltaTime;
        if (pathTimer < pathTime1)
            deltaY = 0;
        if (pathTimer > pathTime1 && pathTimer < pathTime2)
            deltaY = -speed;
        if (pathTimer > pathTime2) {
            deltaY = 0;
        }

        x += deltaX * deltaTime;
        y += deltaY * deltaTime;

        if (y < 0)
            y = Game.HEIGHT;

        setShape();

        if ((direction == RIGHT && x > Game.WIDTH) || (direction == LEFT && x < 0))
            remove = true;
    }


    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int i = 0, j = shapeX.length - 1; i < shapeX.length; j = i++) {
            shapeRenderer.line(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);
        }
        shapeRenderer.line(shapeX[0], shapeY[0], shapeX[3], shapeY[3]);

        shapeRenderer.end();
    }

}
