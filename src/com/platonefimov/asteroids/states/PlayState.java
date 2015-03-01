package com.platonefimov.asteroids.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import com.platonefimov.asteroids.Game;
import com.platonefimov.asteroids.entities.Asteroid;
import com.platonefimov.asteroids.entities.Bullet;
import com.platonefimov.asteroids.entities.Particle;
import com.platonefimov.asteroids.entities.Player;
import com.platonefimov.asteroids.managers.GameKeys;
import com.platonefimov.asteroids.managers.StateManager;

import java.util.ArrayList;



public class PlayState extends GameState {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private BitmapFont hyperspaceBold;

    private Player player;
    private Player hudPlayer;
    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Particle> particles;

    private int level;
    private int totalAsteroids;
    private int asteroidsLeft;


    public PlayState(StateManager stateManager) {
        super(stateManager);
    }

    public void init() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        bullets = new ArrayList<Bullet>();
        player = new Player(bullets);
        hudPlayer = new Player(null);

        asteroids = new ArrayList<Asteroid>();
        particles = new ArrayList<Particle>();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        hyperspaceBold = generator.generateFont(parameter);

        level = 1;
        spawnAsteroids();
    }


    private void createParticle(float x, float y, int num) {
        for(int i = 0; i < num; i++)
            particles.add(new Particle(x, y));
    }


    private void createParticle(float x, float y, int num, float green1, float green2) {
        for(int i = 0; i < num; i++)
            particles.add(new Particle(x, y, MathUtils.random(green1, green2)));
    }


    private void spawnAsteroids() {
        asteroids.clear();

        int numToSpawn = 4 + level - 1;
        asteroidsLeft = totalAsteroids = numToSpawn * 7;

        for (int i = 0; i < numToSpawn; i++) {
            float x = MathUtils.random(Game.WIDTH);
            float y = MathUtils.random(Game.HEIGHT);
            float deltaX = x - player.getX();
            float deltaY = y - player.getY();
            float dist = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            while (dist < 100) {
                x = MathUtils.random(Game.WIDTH);
                y = MathUtils.random(Game.HEIGHT);
                deltaX = x - player.getX();
                deltaY = y - player.getY();
                dist = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            }

            asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
        }
    }


    private void splitAsteroids(Asteroid asteroid) {
        asteroidsLeft--;
        if (asteroid.getType() == Asteroid.LARGE) {
            asteroids.add(new Asteroid(asteroid.getX(), asteroid.getY(), Asteroid.MEDIUM));
            asteroids.add(new Asteroid(asteroid.getX(), asteroid.getY(), Asteroid.MEDIUM));
            createParticle(asteroid.getX(), asteroid.getY(), 16, 0.3f, 0.5f);
        }
        else if (asteroid.getType() == Asteroid.MEDIUM) {
            asteroids.add(new Asteroid(asteroid.getX(), asteroid.getY(), Asteroid.SMALL));
            asteroids.add(new Asteroid(asteroid.getX(), asteroid.getY(), Asteroid.SMALL));
            createParticle(asteroid.getX(), asteroid.getY(), 12, 0.2f, 0.3f);
        }
        else
            createParticle(asteroid.getX(), asteroid.getY(), 8, 0.1f, 0.2f);
    }


    public void update(float deltaTime) {
        handleInput();

        if (asteroids.size() == 0) {
            level++;
            spawnAsteroids();
        }

        player.update(deltaTime);
        if (player.isDead()) {
            player.loseLive();
            player.reset();
            return;
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(deltaTime);
            if (bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(deltaTime);
            if (asteroids.get(i).shouldRemove()) {
                asteroids.remove(i);
                i--;
            }
        }

        checkCollisions();

        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).update(deltaTime);
            if (particles.get(i).shouldRemove()) {
                particles.remove(i);
                i--;
            }
        }
    }


    private void checkCollisions() {
        if (!player.isHit()) {
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid asteroid = asteroids.get(i);
                if (asteroid.intersects(player)) {
                    player.hit();
                    asteroids.remove(i);
                    splitAsteroids(asteroid);
                    break;
                }
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid asteroid = asteroids.get(j);
                if (asteroid.contains(bullet.getX(), bullet.getY())) {
                    bullets.remove(i);
                    i--;
                    asteroids.remove(j);
                    splitAsteroids(asteroid);
                    player.incrementScore(asteroid.getScore());
                    break;
                }
            }
        }
    }


    public void draw() {
        player.draw(shapeRenderer);

        for (Bullet bullet : bullets)
            bullet.draw(shapeRenderer);
        for (Asteroid asteroid : asteroids)
            asteroid.draw(shapeRenderer);
        for (Particle particle : particles)
            particle.draw(shapeRenderer);

        spriteBatch.setColor(1, 1, 1, 1);
        spriteBatch.begin();

        hyperspaceBold.draw(spriteBatch, Long.toString(player.getScore()), 16, 390);

        spriteBatch.end();

        for (int i = 0; i < player.getExtraLives(); i++) {
            hudPlayer.setPosition(16 + i * 10, 360);
            hudPlayer.draw(shapeRenderer);
        }
    }


    public void handleInput() {
        player.setUp(GameKeys.isDown(GameKeys.UP));
        player.setLeft(GameKeys.isDown(GameKeys.LEFT));
        player.setRight(GameKeys.isDown(GameKeys.RIGHT));

        if (GameKeys.isPressed(GameKeys.SPACE) && !player.isHit())
            player.shoot();
    }

    public void dispose() {

    }

}
