package com.redsponge.ldtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.redsponge.ldtk.test.EnemyType;

import java.util.HashMap;
import java.util.Vector;
import java.util.function.Consumer;
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
        addType("Point", j -> new Vector2(j.getInt("cx"), j.getInt("cy")));
        addType("FilePath", JsonValue::asString);
        addType("Color", j -> Color.valueOf(j.asString()));
      /*  converters.put("String", JsonValue::asString);
        converters.put("Bool", JsonValue::asBoolean);
        converters.put("Float", JsonValue::asFloat);
        converters.put("Int", JsonValue::asInt);
        converters.put("Point", j -> new Vector2(j.getInt("cx"), j.getInt("cy")));
        converters.put("FilePath", JsonValue::asString);
        converters.put("Color", j -> Color.valueOf(j.asString()));

        converters.put("Array<String>", JsonValue::asStringArray);
        converters.put("Array<Bool>", JsonValue::asBooleanArray);
        converters.put("Array<Float>", JsonValue::asFloatArray);
        converters.put("Array<Int>", JsonValue::asIntArray);
        converters.put("Array<FilePath>", JsonValue::asStringArray);
        converters.put("Array<Point>", j -> {
            Vector2[] arr = new Vector2[j.size];
            Function<JsonValue, ?> pointConverter = converters.get("Point");
            for (int i = 0; i < j.size; i++) {
                arr[i] = (Vector2) pointConverter.apply(j.get(i));
            }
            return arr;
        });
        converters.put("Array<Color>", j -> {
            Color[] arr = new Color[j.size];
            Function<JsonValue, ?> colorConverter = converters.get("Color");
            for (int i = 0; i < j.size; i++) {
                arr[i] = (Color) colorConverter.apply(j.get(i));
            }
            return arr;
        });*/

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
        addType(name, j -> T.valueOf(en, j.asString()));
    }

    public void removeEnum(String name) {
        converters.remove(name);
        converters.remove("Array<" + name + ">");
    }

    public <T> T convert(JsonValue value) {
        return (T) converters.get(value.getString("__type")).apply(value.get("__value"));
    }
}
