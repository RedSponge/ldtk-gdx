package com.redsponge.ldtkgdx.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.ldtkgdx.*;

import java.util.Arrays;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class LDTKTest extends ApplicationAdapter {

    private LDTKMap map;
    private LDTKLevel level;
    private SpriteBatch batch;
    private LDTKTypes types;
    private Array<Enemy> enemies;

    private Texture zombieTex;
    private FitViewport viewport;
    private Player player;

    private LDTKLevel currentLevel;

    @Override
    public void create() {
        batch = new SpriteBatch();

        types = new LDTKTypes();
        types.addEnum("EnemyType", EnemyType.class);
        map = new LDTKMap(types, Gdx.files.internal("world-test.ldtk"));

        viewport = new FitViewport(320, 180);

        player = new Player();

        LDTKLevel level_0 = map.getLevel("Level_0");
        Array<LDTKEntity> enemies = ((LDTKEntityLayer)level_0.getLayerByName("RoomChanges")).getEntitiesOfType("Enemy");
        LDTKEntity ldtkEntity = enemies.get(0);

        System.out.println(((LDTKEntityLayer)level_0.getLayerByName("RoomChanges")).getEntitiesConverted("Enemy", Enemy.class));

        EnemyType entityType = ldtkEntity.get("Type");
        System.out.println(entityType);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(30 / 255.0f, 10 / 255.0f, 0 / 255.0f, 255 / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(Gdx.graphics.getDeltaTime());

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        if(currentLevel != null) {
            System.out.println(currentLevel.getIdentifier());
            currentLevel.render(batch);
            IntArray neighbours = currentLevel.getNeighbours().get(LDTKNeighbours.NeighbourDirection.All);
            for (int i = 0; i < neighbours.size; i++) {
                map.getLevel(neighbours.get(i)).render(batch);
            }
        } else {
            for (LDTKLevel mapLevel : map.getLevels()) {
                mapLevel.render(batch);
            }
        }

        System.out.println(player.getPosition());

        player.render(batch);
        batch.end();

    }

    private void update(float delta) {


        player.update(delta);

        viewport.getCamera().position.set(player.getPosition(), 0);

        for (LDTKLevel mapLevel : map.getLevels()) {
            if(isPlayerInLevel(player, mapLevel)) {
                currentLevel = mapLevel;
                break;
            }
        }
    }

    private boolean isPlayerInLevel(Player player, LDTKLevel level) {
        return level.getX() < player.getPosition().x && player.getPosition().x < level.getX() + level.getWidth()
            && level.getFlippedY() < player.getPosition().y && player.getPosition().y < level.getFlippedY() + level.getHeight();
    }

    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}