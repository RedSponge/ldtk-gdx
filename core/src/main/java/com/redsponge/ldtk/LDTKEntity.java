package com.redsponge.ldtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class LDTKEntity {

    private String id;
    private int x, y;
    private HashMap<String, Object> values;

    public LDTKEntity(JsonValue value, LDTKTypes types) {
        values = new HashMap<>();

        id = value.getString("__identifier");
        x = value.get("px").getInt(0);
        y = value.get("px").getInt(1);

        JsonValue fields = value.get("fieldInstances");
        for (int i = 0; i < fields.size; i++) {
            parseField(fields.get(i), types);
        }
    }

    private void parseField(JsonValue jsonValue, LDTKTypes types) {
        String valueName = jsonValue.getString("__identifier");
        values.put(valueName, types.convert(jsonValue));
    }
}
