package com.redsponge.ldtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.function.Function;

public class LDTKTypes {
    private HashMap<String, Function<JsonValue, ?>> converters;

    public LDTKTypes() {
        converters = new HashMap<>();
        setDefaults();
    }

    private void setDefaults() {
        addType("String", JsonValue::asString, JsonValue::asStringArray);
        addType("Bool", JsonValue::asBoolean);
        addType("Float", JsonValue::asFloat);
        addType("Int", JsonValue::asInt);
        addType("Point", j -> j.isNull() ? null : new Vector2(j.getInt("cx"), j.getInt("cy")));
        addType("FilePath", JsonValue::asString);
        addType("Color", j -> j.isNull() ? null : Color.valueOf(j.asString()));
    }

    public <T> void addType(String typeName, Function<JsonValue, T> converter, Function<JsonValue, T[]> arrayConverter) {
        converters.put(typeName, converter);
        converters.put("Array<" + typeName + ">", arrayConverter);
    }

    public <T> void addType(String typeName, Function<JsonValue, T> converter) {
        addType(typeName, converter, j -> {
            T[] arr = (T[]) new Object[j.size];
            for (int i = 0; i < j.size; i++) {
                arr[i] = converter.apply(j.get(i));
            }
            return arr;
        });
    }

    public <T extends Enum<T>> void addEnum(String name, Class<T> en) {
        if(!name.startsWith("LocalEnum.")) name = "LocalEnum." + name;
        addType(name, j -> j.isNull() ? null : T.valueOf(en, j.asString()));
    }

    public void removeEnum(String name) {
        converters.remove(name);
        converters.remove("Array<" + name + ">");
    }

    public <T> T convert(JsonValue value) {
        return (T) converters.get(value.getString("__type")).apply(value.get("__value"));
    }
}
