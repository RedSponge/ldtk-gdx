package com.redsponge.ldtkgdx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LDTKTile {

    private TextureRegion region;
    private int x, y;

    public LDTKTile(TextureRegion region, int x, int y) {
        this.region = region;
        this.x = x;
        this.y = y;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public LDTKTile setRegion(TextureRegion region) {
        this.region = region;
        return this;
    }

    public int getX() {
        return x;
    }

    public LDTKTile setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public LDTKTile setY(int y) {
        this.y = y;
        return this;
    }
}
