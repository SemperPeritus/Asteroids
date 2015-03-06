package com.platonefimov.asteroids.entities;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import com.platonefimov.asteroids.Game;
import com.platonefimov.asteroids.managers.Jukebox;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Player extends Object {

    int MAX_BULLETS;
    private ArrayList<Bullet> bullets;

    private boolean up;
    private boolean left;
    private boolean right;

    private boolean hit;
    private boolean dead;

    private float maxSpeed;
    private float acceleration;
    private float deceleration;

    private float flameX[];
    private float flameY[];

    private float acceleratingTime;
    private float acceleratingTimer;

    private float hitTime;
    private float hitTimer;
    private Line2D.Float[] hitLines;
    private Point2D.Float[] hitLinesVector;

    private long score;
    private int extraLives;
    private long requiredScore;


    public Player(ArrayList<Bullet> bullets) {
        MAX_BULLETS = 4;
        this.bullets = bullets;

        x = Game.WIDTH / 2;
        y = Game.HEIGHT / 2;

        radians = MathUtils.PI / 2;
        rotationSpeed = 3;

        maxSpeed = 300;
        acceleration = 200;
        deceleration = 10;

        shapeX = new float[4];
        shapeY = new float[4];

        flameX = new float[3];
        flameY = new float[3];

        acceleratingTime = 0.1f;

        hit = false;
        dead = false;
        hitTime = 2.0f;
        hitTimer = 0.0f;

        score = 0;
        extraLives = 3;
        requiredScore = 10000;
    }


    public boolean isHit() {
        return hit;
    }

    public boolean isDead() {
        return dead;
    }

    public long getScore() {
        return score;
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        setShape();
    }

    public int getExtraLives() {
        return extraLives;
    }


    public void setShape() {
        shapeX[0] = x + MathUtils.cos(radians) * 8;
        shapeY[0] = y + MathUtils.sin(radians) * 8;
        shapeX[1] = x + MathUtils.cos(radians - 2.5132f) * 8;
        shapeY[1] = y + MathUtils.sin(radians - 2.5132f) * 8;
        shapeX[2] = x + MathUtils.cos(radians + 3.1416f) * 5;
        shapeY[2] = y + MathUtils.sin(radians + 3.1416f) * 5;
        shapeX[3] = x + MathUtils.cos(radians + 2.5132f) * 8;
        shapeY[3] = y + MathUtils.sin(radians + 2.5132f) * 8;
    }


    public void setUp(boolean bool) {
        if (bool && !up && !hit)
            Jukebox.loop("thrust");
        else if (!bool)
            Jukebox.stop("thrust");

        up = bool;
    }

    public void setLeft(boolean bool) {
        left = bool;
    }

    public void setRight(boolean bool) {
        right = bool;
    }

    public void loseLive() {
        extraLives--;
    }

    public void incrementScore(long score) {
        this.score += score;
    }


    private void setFlame() {
        flameX[0] = x + MathUtils.cos(radians - 2.618f) * 5;
        flameY[0] = y + MathUtils.sin(radians - 2.618f) * 5;
        flameX[1] = x + MathUtils.cos(radians - MathUtils.PI) * (6 + acceleratingTimer * 50);
        flameY[1] = y + MathUtils.sin(radians - MathUtils.PI) * (6 + acceleratingTimer * 50);
        flameX[2] = x + MathUtils.cos(radians + 2.618f) * 5;
        flameY[2] = y + MathUtils.sin(radians + 2.618f) * 5;
    }


    public void shoot() {
        if (bullets.size() == MAX_BULLETS)
            return;
        bullets.add(new Bullet(x, y, radians));
        Jukebox.play("fire");
    }


    public void hit() {
        if (hit)
            return;

        Jukebox.stop("thrust");

        hit = true;
        deltaX = deltaY = 0;
        up = left = right = false;

        hitLines = new Line2D.Float[4];
        for (int i = 0, j = hitLines.length - 1; i < hitLines.length; j = i++)
            hitLines[i] = new Line2D.Float(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);

        hitLinesVector = new Point2D.Float[4];
        hitLinesVector[0] = new Point2D.Float(MathUtils.cos(radians + 1.57f), MathUtils.sin(radians + 1.57f));
        hitLinesVector[1] = new Point2D.Float(MathUtils.cos(radians - 1.57f), MathUtils.sin(radians - 1.57f));
        hitLinesVector[2] = new Point2D.Float(MathUtils.cos(radians + 2.7489f), MathUtils.sin(radians + 2.7489f));
        hitLinesVector[3] = new Point2D.Float(MathUtils.cos(radians - 2.7489f), MathUtils.sin(radians - 2.7489f));
    }


    public void reset() {
        x = Game.WIDTH / 2;
        y = Game.HEIGHT / 2;
        hit = dead = false;
    }


    public void update(float deltaTime) {
        if (hit) {
            hitTimer += deltaTime;
            if (hitTimer > hitTime) {
                dead = true;
                hitTimer = 0;
            }
            for (int i = 0; i < hitLines.length; i++) {
                hitLines[i].setLine(hitLines[i].x1 + hitLinesVector[i].x * 10 * deltaTime,
                        hitLines[i].y1 + hitLinesVector[i].y * 10 * deltaTime,
                        hitLines[i].x2 + hitLinesVector[i].x * 10 * deltaTime,
                        hitLines[i].y2 + hitLinesVector[i].y * 10 * deltaTime);
            }
            return;
        }

        if (score >= requiredScore) {
            extraLives++;
            requiredScore += 10000;
            Jukebox.play("extraShip");
        }

        if (left)
            radians += rotationSpeed * deltaTime;
        if (right)
            radians -= rotationSpeed * deltaTime;

        if (up) {
            deltaX += MathUtils.cos(radians) * acceleration * deltaTime;
            deltaY += MathUtils.sin(radians) * acceleration * deltaTime;
            acceleratingTimer += deltaTime;
            if (acceleratingTimer > acceleratingTime)
                acceleratingTimer = 0;
        }
        else
            acceleratingTimer = 0;

        float vector = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (vector > 0) {
            deltaX -= (deltaX / vector) * deceleration * deltaTime;
            deltaY -= (deltaY / vector) * deceleration * deltaTime;
        }
        if (vector > maxSpeed) {
            deltaX = (deltaX / vector) * maxSpeed;
            deltaY = (deltaY / vector) * maxSpeed;
        }

        x += deltaX * deltaTime;
        y += deltaY * deltaTime;

        setShape();

        if (up) {
            setFlame();
        }

        wrap();
    }


    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        if (hit) {
            for (Line2D.Float hitLine : hitLines)
                shapeRenderer.line(hitLine.x1, hitLine.y1, hitLine.x2, hitLine.y2);
            shapeRenderer.end();
            return;
        }

        for (int i = 0, j = shapeX.length - 1; i < shapeX.length; j = i++)
            shapeRenderer.line(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);

        if (up)
            for (int i = 0, j = flameX.length - 1; i < flameX.length; j = i++)
                shapeRenderer.line(flameX[i], flameY[i], flameX[j], flameY[j]);

        shapeRenderer.end();
    }

}
