package com.redsponge.ldtk;

import com.badlogic.gdx.graphics.Color;

public class LDTKLevel {

    private String identifier;
    private int uid;
    private int width;
    private int height;
    private LDTKLayer[] layers;
    private Color backgroundColor;

    public LDTKLevel(String identifier, int uid, int width, int height, LDTKLayer[] layers) {
        this.identifier = identifier;
        this.uid = uid;
        this.width = width;
        this.height = height;
        this.layers = layers;
    }
}
