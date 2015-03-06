package com.platonefimov.asteroids.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import com.platonefimov.asteroids.Game;
import com.platonefimov.asteroids.entities.*;
import com.platonefimov.asteroids.managers.GameKeys;
import com.platonefimov.asteroids.managers.Jukebox;
import com.platonefimov.asteroids.managers.SaveData;
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

    private Saucer saucer;
    private ArrayList<Bullet> enemyBullets;
    private float saucerTime;
    private float saucerTimer;

    private int level;
    private int totalAsteroids;
    private int asteroidsLeft;

    private float maxDelay;
    private float minDelay;
    private float currentDelay;
    private float musicTimer;
    private boolean isLowPulse;


    public PlayState(StateManager stateManager) {
        super(stateManager);
    }

    public void init() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        bullets = new ArrayList<Bullet>();
        enemyBullets = new ArrayList<Bullet>();
        player = new Player(bullets);
        hudPlayer = new Player(null);

        asteroids = new ArrayList<Asteroid>();
        particles = new ArrayList<Particle>();

        saucerTime = 15;
        saucerTimer = 0;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        hyperspaceBold = generator.generateFont(parameter);
        hyperspaceBold.setColor(1, 1, 1, 1);

        level = 1;
        spawnAsteroids();

        maxDelay = currentDelay = musicTimer = 1f;
        minDelay = 0.25f;
        isLowPulse = true;
    }


    private void createParticle(float x, float y, int num, float green1, float green2) {
        for(int i = 0; i < num; i++)
            particles.add(new Particle(x, y, MathUtils.random(green1, green2)));
    }


    private void spawnAsteroids() {
        asteroids.clear();

        int numToSpawn = 4 + level - 1;
        asteroidsLeft = totalAsteroids = numToSpawn * 7;

        currentDelay = maxDelay;

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

        currentDelay = ((maxDelay - minDelay) * asteroidsLeft / totalAsteroids) + minDelay;
    }


    public void playerHit() {
        player.hit();
        createParticle(player.getX(), player.getY(), 30, 0.5f, 0.2f);
    }

    public void saucerLeft() {
        saucer = null;
        Jukebox.stop("saucerLarge");
        Jukebox.stop("saucerSmall");
    }

    public void saucerHit() {
        createParticle(saucer.getX(), saucer.getY(), 20, 0.3f, 0.15f);
        Jukebox.play("bangLarge");
        saucerLeft();
    }


    public void update(float deltaTime) {
        handleInput();

        if (asteroids.size() == 0) {
            level++;
            spawnAsteroids();
        }

        player.update(deltaTime);
        if (player.isDead()) {
            if (player.getExtraLives() == 0) {
                SaveData.highscoresData.setTentativeScore(player.getScore());
                stateManager.setState(StateManager.GAME_OVER);
                return;
            }
            player.loseLive();
            player.reset();
            saucerLeft();
            return;
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(deltaTime);
            if (bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).update(deltaTime);
            if (enemyBullets.get(i).shouldRemove()) {
                enemyBullets.remove(i);
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

        if (saucer == null) {
            saucerTimer += deltaTime;
            if (saucerTimer > saucerTime) {
                saucerTimer = 0;
                int type = MathUtils.random() < 0.5 ? Saucer.SMALL : Saucer.LARGE;
                int direction = MathUtils.random() < 0.5 ? Saucer.RIGHT : Saucer.LEFT;
                saucer = new Saucer(type, direction, player, enemyBullets);
            }
        }
        else {
            saucer.update(deltaTime);
            if (saucer.shouldRemove())
                saucerLeft();
        }

        musicTimer += deltaTime;
        if (!player.isHit() && musicTimer >= currentDelay) {
            if (isLowPulse)
                Jukebox.play("beat1");
            else
                Jukebox.play("beat2");
            isLowPulse = !isLowPulse;
            musicTimer = 0f;
        }
    }


    private void checkCollisions() {
        if (!player.isHit()) {
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid asteroid = asteroids.get(i);
                if (asteroid.intersects(player)) {
                    playerHit();
                    asteroids.remove(i);
                    splitAsteroids(asteroid);
                    asteroid.playBang();
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
                    asteroid.playBang();
                    break;
                }
            }
        }

        for (int i = 0; i < enemyBullets.size(); i++) {
            Bullet bullet = enemyBullets.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid asteroid = asteroids.get(j);
                if (asteroid.contains(bullet.getX(), bullet.getY())) {
                    bullets.remove(i);
                    i--;
                    asteroids.remove(j);
                    splitAsteroids(asteroid);
                    asteroid.playBang();
                    break;
                }
            }
        }

        if (saucer != null)
            if (player.intersects(saucer)) {
                playerHit();
                saucerHit();
            }


        if (saucer != null)
            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                if (saucer.contains(bullet.getX(), bullet.getY())) {
                    bullets.remove(i);
                    player.incrementScore(saucer.getScore());
                    saucerHit();
                    break;
                }
            }

        if (saucer != null)
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid asteroid = asteroids.get(i);
                if (asteroid.intersects(saucer)) {
                    saucerHit();
                    asteroids.remove(i);
                    splitAsteroids(asteroid);
                    asteroid.playBang();
                    break;
                }
            }

        if (!player.isHit()) {
            for (int i = 0; i < enemyBullets.size(); i++) {
                Bullet bullet = enemyBullets.get(i);
                if (player.contains(bullet.getX(), bullet.getY())) {
                    playerHit();
                    enemyBullets.remove(i);
                    break;
                }
            }
        }
    }


    public void draw() {
        shapeRenderer.setProjectionMatrix(Game.camera.combined);
        spriteBatch.setProjectionMatrix(Game.camera.combined);

        player.draw(shapeRenderer);

        for (Bullet bullet : bullets)
            bullet.draw(shapeRenderer);
        for (Bullet enemyBullet : enemyBullets)
            enemyBullet.draw(shapeRenderer);
        for (Asteroid asteroid : asteroids)
            asteroid.draw(shapeRenderer);
        for (Particle particle : particles)
            particle.draw(shapeRenderer);

        if (saucer != null)
            saucer.draw(shapeRenderer);

        spriteBatch.begin();

        hyperspaceBold.draw(spriteBatch, Long.toString(player.getScore()), 16, 390);

        spriteBatch.end();

        for (int i = 0; i < player.getExtraLives(); i++) {
            hudPlayer.setPosition(16 + i * 10, 360);
            hudPlayer.draw(shapeRenderer);
        }
    }


    public void handleInput() {
        if (!player.isHit()) {
            player.setUp(GameKeys.isDown(GameKeys.UP));
            player.setLeft(GameKeys.isDown(GameKeys.LEFT));
            player.setRight(GameKeys.isDown(GameKeys.RIGHT));

            if (GameKeys.isPressed(GameKeys.SPACE))
                player.shoot();
        }
    }

    public void dispose() {
        super.dispose();

        shapeRenderer.dispose();
        spriteBatch.dispose();
        hyperspaceBold.dispose();
    }

}
