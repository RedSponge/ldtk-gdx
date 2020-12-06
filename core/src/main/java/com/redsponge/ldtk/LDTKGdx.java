package com.redsponge.ldtk;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
    private TextureRegion region;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(640/2, 360/2);
        tileLayer = new LDTKTileLayer(new JsonReader().parse(Gdx.files.internal("AutoLayers_4_Advanced.ldtk")).get("levels").get(0).get("layerInstances").get(1), true);
        region = new TextureRegion(tileLayer.getTilemapTexture(), 16, 152, 8, 8);
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
//        batch.draw(tileLayer.getTilemapTexture(), 10, 10);
//        batch.draw(region, 300, 300);
        tileLayer.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        tileLayer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}