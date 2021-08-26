package com.redsponge.ldtkgdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;

public class LDTKTileLayer extends LDTKLayer implements Disposable {

    private LDTKTile[] regions;
    private Texture tilemapTexture;

    public LDTKTileLayer(JsonValue value, boolean intGrid) {
        super(value);
        tilemapTexture = new Texture(value.getString("__tilesetRelPath"));
        JsonValue tiles = value.get(intGrid ? "autoLayerTiles" : "gridTiles");
        regions = new LDTKTile[tiles.size];
        for (int i = 0; i < tiles.size; i++) {
            JsonValue tileValue = tiles.get(i);

            JsonValue pixelPosition = tileValue.get("px");
            JsonValue sourcePosition = tileValue.get("src");
            int flipFlags = tileValue.getInt("f");
            boolean flipX = (flipFlags & 1) == 1;
            boolean flipY = ((flipFlags >> 1) & 1) == 1;

            regions[i] = new LDTKTile(new TextureRegion(tilemapTexture, sourcePosition.getInt(0), sourcePosition.getInt(1), gridSize, gridSize), pixelPosition.getInt(0), height * gridSize - pixelPosition.getInt(1));
            regions[i].getRegion().flip(flipX, flipY);
        }
    }

    public void render(SpriteBatch batch) {
        render(batch, 0, 0);
    }

    public void render(SpriteBatch batch, float offsetX, float offsetY) {
        for (LDTKTile region : regions) {
            batch.draw(region.getRegion(), region.getX() + offsetX, region.getY() + offsetY);
        }
    }


    @Override
    public void dispose() {
        tilemapTexture.dispose();
    }

    public LDTKTile[] getRegions() {
        return regions;
    }

    public Texture getTilemapTexture() {
        return tilemapTexture;
    }
}
