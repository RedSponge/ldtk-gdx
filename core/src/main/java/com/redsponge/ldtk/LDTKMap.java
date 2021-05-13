package com.redsponge.ldtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Collection;
import java.util.HashMap;

public class LDTKMap {

    private final HashMap<String, LDTKLevel> levelsById;
    private final Color bgColor;
    private final LDTKTypes types;

    public LDTKMap(String file, LDTKTypes types) {
        this(new JsonReader().parse(Gdx.files.internal(file)), types);
    }

    public LDTKMap(JsonValue value, LDTKTypes types) {
        this.levelsById = new HashMap<>();
        this.types = types;
        this.bgColor = Color.valueOf(value.getString("bgColor"));

        JsonValue levelsRoot = value.get("levels");
        for (int i = 0; i < levelsRoot.size; i++) {
            LDTKLevel level = new LDTKLevel(levelsRoot.get(i), types);
            levelsById.put(level.getIdentifier(), level);
        }
    }

    public LDTKLevel getLevel(String id) {
        return levelsById.get(id);
    }

    public Collection<LDTKLevel> getLevels() {
        return levelsById.values();
    }

    public Color getBackgroundColor() {
        return bgColor;
    }

    public LDTKTypes getTypes() {
        return types;
    }
}
