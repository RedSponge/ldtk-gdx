package com.redsponge.ldtk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;

public class LDTKTileLayer extends LDTKLayer implements Disposable {

    private LDTKTile[] regions;
    private Texture tilemapTexture;

    public LDTKTileLayer(JsonValue value) {
        super(value);
        tilemapTexture = new Texture(value.getString("__tilesetRelPath"));
        JsonValue tiles = value.get("gridTiles");
        regions = new LDTKTile[tiles.size];
        for (int i = 0; i < tiles.size; i++) {
            JsonValue tileValue = tiles.get(i);

            JsonValue pixelPosition = tileValue.get("px");
            JsonValue sourcePosition = tileValue.get("src");
            int flipFlags = tileValue.getInt("f");
            boolean flipX = (flipFlags & 1) == 1;
            boolean flipY = ((flipFlags >> 1) & 1) == 1;

            regions[i] = new LDTKTile(new TextureRegion(tilemapTexture, pixelPosition.getInt(0), pixelPosition.getInt(1), gridSize, gridSize), sourcePosition.getInt(0), sourcePosition.getInt(1));
            regions[i].getRegion().flip(flipX, flipY);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < regions.length; i++) {
            batch.draw(regions[i].getRegion(), regions[i].getX(), regions[i].getY(), gridSize, gridSize);
        }
    }


    @Override
    public void dispose() {
        tilemapTexture.dispose();
    }
}
