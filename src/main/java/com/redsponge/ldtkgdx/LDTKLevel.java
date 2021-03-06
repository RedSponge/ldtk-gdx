package com.redsponge.ldtkgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class LDTKLevel {

    public static final String LAYER_TYPE_TILES = "Tiles";
    public static final String LAYER_TYPE_INTGRID = "IntGrid";
    public static final String LAYER_TYPE_ENTITY = "Entities";

    private String identifier;
    private int uid;
    private int width;
    private int height;
    private Array<LDTKLayer> layers;

    private Array<LDTKEntityLayer> entityLayers;
    private Array<LDTKTileLayer> tileLayers;
    private HashMap<String, LDTKLayer> layersByName;

    private Color backgroundColor;

    public LDTKLevel(JsonValue root, LDTKTypes types) {
        identifier = root.getString("identifier");
        uid = root.getInt("uid");
        backgroundColor = Color.valueOf(root.getString("__bgColor"));
        width = root.getInt("pxWid");
        height = root.getInt("pxHei");

        layers = new Array<>();
        entityLayers = new Array<>();
        tileLayers = new Array<>();

        layersByName = new HashMap<>();

        JsonValue layersJson = root.get("layerInstances");
        for (int i = 0; i < layersJson.size; i++) {
            parseLayer(layersJson.get(i), types);
        }

    }

    private void parseLayer(JsonValue layerJson, LDTKTypes types) {
        LDTKLayer layer;
        switch (layerJson.getString("__type")) {
            case LAYER_TYPE_TILES:
                layer = new LDTKTileLayer(layerJson, false);
                break;
            case LAYER_TYPE_INTGRID:
                layer = new LDTKTileLayer(layerJson, true);
                break;
            case LAYER_TYPE_ENTITY:
                layer = new LDTKEntityLayer(layerJson, types);
                break;
            default:
                return;
        }
        layers.add(layer);
        if(layer instanceof LDTKTileLayer) {
            tileLayers.add((LDTKTileLayer) layer);
        } else {
            entityLayers.add((LDTKEntityLayer) layer);
        }
        layersByName.put(layer.getId(), layer);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < tileLayers.size; i++) {
            tileLayers.get(i).render(batch);
        }
    }

    public LDTKLayer getLayerByName(String name) {
        if(layersByName.containsKey(name)) {
            return layersByName.get(name);
        }
        throw new LDTKException("Could not find layer with name " + name + " in level " + identifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getUid() {
        return uid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Array<LDTKEntityLayer> getEntityLayers() {
        return entityLayers;
    }

    public Array<LDTKLayer> getLayers() {
        return layers;
    }

    public Array<LDTKTileLayer> getTileLayers() {
        return tileLayers;
    }

    public void dispose() {
        for (int i = 0; i < tileLayers.size; i++) {
            tileLayers.get(i).dispose();
        }
        tileLayers.clear();
        entityLayers.clear();
        layers.clear();
    }
}
