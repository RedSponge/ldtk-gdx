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
import com.redsponge.ldtkgdx.LDTKLevel;
import com.redsponge.ldtkgdx.LDTKMap;
import com.redsponge.ldtkgdx.LDTKNeighbours;
import com.redsponge.ldtkgdx.LDTKTypes;

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

        Gdx.app.setLogLevel(Logger.DEBUG);
        Gdx.app.log("Hello!", "TEST!");
        for (Field field : ClassReflection.getDeclaredFields(Enemy.class)) {
            Gdx.app.log("Hello!", "For field "+ field.getName() + " the annotations are " + Arrays.toString(field.getDeclaredAnnotations()));
        }

        batch = new SpriteBatch();
        JsonValue mapJson = new JsonReader().parse(Gdx.files.internal("world-test.ldtk").readString());

        types = new LDTKTypes();
        map = new LDTKMap(types);
        map.load(mapJson);

        viewport = new FitViewport(320, 180);

        player = new Player();

//        types.addEnum("EnemyType", EnemyType.class);

//        try {
//            Enemy e = ClassReflection.newInstance(Enemy.class);
//            System.out.println(e);
//        } catch (ReflectionException reflectionException) {
//            reflectionException.printStackTrace();
//        }

//        enemies = level.getEntityLayers().get(0).getEntitiesConverted("Enemy", Enemy.class);

//        zombieTex = new Texture("zombie.png");
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
//        enemies.forEach(e -> {
//            Gdx.app.log("Heya", e.toString());
//            batch.draw(zombieTex, e.getPos().x, e.getPos().y);
//        });
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
//        zombieTex.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}