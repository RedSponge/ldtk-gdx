package com.redsponge.ldtkgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Contains methods for converting between LDTK json values and their Java counterparts.
 * This class allows for user-defined types
 */
public class LDTKTypes {
    private final HashMap<String, Function<JsonValue, ?>> converters;

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

    /**
     * Registers a new type for conversion - subsequent calls to {@link LDTKTypes#convert(JsonValue) will use this type}
     * @param typeName The type-name in the LDTK json
     * @param converter A supplied function that converts the JSON value to a Java object
     * @param arrayConverter A function to convert a JSON array to its counterpart Java array
     * @param <T> The Java type
     */
    public <T> void addType(String typeName, Function<JsonValue, T> converter, Function<JsonValue, T[]> arrayConverter) {
        converters.put(typeName, converter);
        converters.put("Array<" + typeName + ">", arrayConverter);
    }

    /**
     * Registers a new type for conversion - subsequent calls to {@link LDTKTypes#convert(JsonValue)} will use this type
     * Also creates an array converter which creates an array of objects using the supplied function
    * @param typeName The type-name in the LDTK json
    * @param converter A supplied function that converts the JSON value to a Java object
    * @param <T> The Java type
   */
    public <T> void addType(String typeName, Function<JsonValue, T> converter) {
        addType(typeName, converter, j -> {
            T[] arr = (T[]) new Object[j.size];
            for (int i = 0; i < j.size; i++) {
                arr[i] = converter.apply(j.get(i));
            }
            return arr;
        });
    }

    /**
     * Registers a Java enum as the convertion for an LDTK-json enum. <b>Use this method and not {@link LDTKTypes#addType(String, Function)} when registering an enum!</b>
     * @param name - The name of the enum in LDTK
     * @param en - the Java enum
     */
    public <T extends Enum<T>> void addEnum(String name, Class<T> en) {
        if(!name.startsWith("LocalEnum.")) name = "LocalEnum." + name;
        addType(name, j -> j.isNull() ? null : T.valueOf(en, j.asString()));
    }

    /**
     * Un-registers a type.
     * @param name The registered name (the name in the LDTK json)
     */
    public void removeType(String name) {
        converters.remove(name);
        converters.remove("Array<" + name + ">");
    }

    /**
     * Converts a json value to a Java object using the supplied {@link LDTKTypes#converters}
     * @param value The json value
     * @param <T> The type to-be converted to
     * @return The converted object
     * @see LDTKTypes#addType(String, Function)
     */
    public <T> T convert(JsonValue value) {
        String type = value.getString("__type");
        if(converters.containsKey(type)) {
            return (T) converters.get(type).apply(value.get("__value"));
        } else {
            throw new LDTKException("Could not convert value of type " + type);
        }
    }
}
