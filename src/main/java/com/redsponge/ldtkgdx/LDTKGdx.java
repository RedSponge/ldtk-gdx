package com.redsponge.ldtkgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class LDTKGdx extends ApplicationAdapter {

    private SpriteBatch batch;
    private FitViewport viewport;
    private LDTKTileLayer tileLayer;
    private LDTKEntityLayer entityLayer;
    private TextureRegion region;
    private LDTKEntity entity;

//    private Array<EntityEnemy> enemies;

    private Texture zombie, skeleton, husk;

    private LDTKLevel level;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(640/2, 360/2);
        LDTKTypes types = new LDTKTypes();
//        types.addEnum("EnemyType", EnemyType.class);

        JsonValue root = new JsonReader().parse(Gdx.files.internal("AutoLayers_4_Advanced.ldtk"));
        level = new LDTKLevel(root.get("levels").get(0), types);
//        tileLayer = new LDTKTileLayer(root.get("levels").get(0).get("layerInstances").get(0), false);
//        enemies = level.getEntityLayers().first().getEntitiesConverted("Enemy", EntityEnemy.class);
        zombie = new Texture("zombie.png");
        skeleton = new Texture("skeleton.png");
        husk = new Texture("husk.png");

//        region = new TextureRegion(tileLayer.getTilemapTexture(), 16, 152, 8, 8);
    }

    @Override
    public void render() {
        super.render();

        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        level.render(batch);
//        for (EntityEnemy enemy : enemies) {
//            Texture tex;
//            switch (enemy.getType()) {
//                case Zombie:
//                    tex = zombie;
//                    break;
//                case Skeleton:
//                    tex = skeleton;
//                    break;
//                case Husk:
//                    tex = husk;
//                    break;
//                default:
//                    continue;
//            }
//            batch.draw(tex, enemy.getPos().x, enemy.getPos().y, 16, 16);
//        }
        batch.end();
    }

    private void update(float delta) {
        if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            viewport.getCamera().position.x -= 50 * delta;
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            viewport.getCamera().position.x += 50 * delta;
        }
        if(Gdx.input.isKeyPressed(Keys.DOWN)) {
            viewport.getCamera().position.y -= 50 * delta;
        }
        if(Gdx.input.isKeyPressed(Keys.UP)) {
            viewport.getCamera().position.y += 50 * delta;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        zombie.dispose();
        skeleton.dispose();
        husk.dispose();
        level.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}