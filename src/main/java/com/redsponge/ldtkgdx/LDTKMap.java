package com.redsponge.ldtkgdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.logging.FileHandler;

public class LDTKMap {

    private IntMap<LDTKLevel> levelsById;
    private HashMap<String, Integer> levelNamesToIds;
    private int worldGridWidth, worldGridHeight;
    private int maxWidth, maxHeight;

    private LDTKTypes types;

    public LDTKMap(LDTKTypes types) {
        this.levelsById = new IntMap<>();
        this.levelNamesToIds = new HashMap<>();
        this.types = types;
    }

    public LDTKMap(LDTKTypes types, FileHandle file) {
        this(types);
        load(new JsonReader().parse(file));
    }

    public void load(JsonValue root) {
        this.worldGridWidth = root.getInt("worldGridWidth");
        this.worldGridHeight = root.getInt("worldGridHeight");


        JsonValue levelRoot = root.get("levels");
        levelRoot.forEach(this::parseLevel);
    }

    private void parseLevel(JsonValue levelRoot) {
        LDTKLevel level = new LDTKLevel(levelRoot, types, this);
        levelsById.put(level.getUid(), level);
        levelNamesToIds.put(level.getIdentifier(), level.getUid());

        if(level.getX() + level.getWidth() > maxWidth) {
            maxWidth = level.getX() + level.getWidth();
        }

        if(level.getY() + level.getHeight() > maxHeight) {
            maxHeight = level.getY() + level.getHeight();
        }
    }

    public LDTKLevel getLevel(String name) {
        return levelsById.get(levelNamesToIds.get(name));
    }

    public LDTKLevel getLevel(int id) {
        return levelsById.get(id);
    }

    public Iterable<LDTKLevel> getLevels() {
        return levelsById.values();
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void dispose() {
        levelsById.values().forEach(LDTKLevel::dispose);
    }
}
