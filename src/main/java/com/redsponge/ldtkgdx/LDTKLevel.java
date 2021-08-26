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
    private int x;
    private int y;
    private int width;
    private int height;
    private Array<LDTKLayer> layers;

    private Array<LDTKEntityLayer> entityLayers;
    private Array<LDTKTileLayer> tileLayers;
    private HashMap<String, LDTKLayer> layersByName;

    private LDTKMap mapIn;

    private Color backgroundColor;
    private LDTKNeighbours neighbours;

    public LDTKLevel(JsonValue root, LDTKTypes types, LDTKMap mapIn) {
        this.identifier = root.getString("identifier");
        this.uid = root.getInt("uid");
        this.backgroundColor = Color.valueOf(root.getString("__bgColor"));
        this.x = root.getInt("worldX");
        this.y = root.getInt("worldY");
        this.width = root.getInt("pxWid");
        this.height = root.getInt("pxHei");
        this.mapIn = mapIn;

        this.layers = new Array<>();
        this.entityLayers = new Array<>();
        this.tileLayers = new Array<>();
        this.neighbours = new LDTKNeighbours();

        layersByName = new HashMap<>();

        JsonValue layersJson = root.get("layerInstances");
        for (int i = 0; i < layersJson.size; i++) {
            parseLayer(layersJson.get(i), types);
        }

        parseNeighbours(root.get("__neighbours"));

    }

    private void parseNeighbours(JsonValue neighboursJson) {
        for (int i = 0; i < neighboursJson.size; i++) {
            JsonValue neighbourJson = neighboursJson.get(i);
            neighbours.add(neighbourJson.getInt("levelUid"), neighbourJson.getString("dir").charAt(0));
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
            tileLayers.get(i).render(batch, getX(), getFlippedY());
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFlippedY() {
        return mapIn.getMaxHeight() - (y + height);
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

    public LDTKNeighbours getNeighbours() {
        return neighbours;
    }
}
