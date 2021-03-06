package com.redsponge.ldtkgdx;

import com.badlogic.gdx.utils.JsonValue;

public class LDTKLayer {

    protected String id;
    protected int width;
    protected int height;
    protected int gridSize;
    protected float opacity;
    protected int levelId;
    protected int pixelOffsetX;
    protected int pixelOffsetY;
    protected int randomSeed;

    public LDTKLayer(JsonValue value) {
        this(value.getString("__identifier"), value.getInt("__cWid"), value.getInt("__cHei"), value.getInt("__gridSize"), value.getFloat("__opacity"), value.getInt("levelId"), value.getInt("pxOffsetX"), value.getInt("pxOffsetY"), value.getInt("seed"));
    }

    public LDTKLayer(String id, int width, int height, int gridSize, float opacity, int levelId, int pixelOffsetX, int pixelOffsetY, int randomSeed) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.gridSize = gridSize;
        this.opacity = opacity;
        this.levelId = levelId;
        this.pixelOffsetX = pixelOffsetX;
        this.pixelOffsetY = pixelOffsetY;
        this.randomSeed = randomSeed;
    }

    public String getId() {
        return id;
    }

    public LDTKLayer setId(String id) {
        this.id = id;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public LDTKLayer setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public LDTKLayer setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getGridSize() {
        return gridSize;
    }

    public LDTKLayer setGridSize(int gridSize) {
        this.gridSize = gridSize;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }

    public LDTKLayer setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public int getLevelId() {
        return levelId;
    }

    public LDTKLayer setLevelId(int levelId) {
        this.levelId = levelId;
        return this;
    }

    public int getPixelOffsetX() {
        return pixelOffsetX;
    }

    public LDTKLayer setPixelOffsetX(int pixelOffsetX) {
        this.pixelOffsetX = pixelOffsetX;
        return this;
    }

    public int getPixelOffsetY() {
        return pixelOffsetY;
    }

    public LDTKLayer setPixelOffsetY(int pixelOffsetY) {
        this.pixelOffsetY = pixelOffsetY;
        return this;
    }

    public int getRandomSeed() {
        return randomSeed;
    }

    public LDTKLayer setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
        return this;
    }
}
