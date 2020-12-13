package com.redsponge.ldtk;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;


import java.util.HashMap;

public class LDTKEntity {

    private final String id;
    private final int x, y;
    private final HashMap<String, Object> values;

    public LDTKEntity(JsonValue value, LDTKTypes types, LDTKLayer layer) {
        values = new HashMap<>();

        id = value.getString("__identifier");
        x = value.get("px").getInt(0);
        y = layer.getHeight() * layer.getGridSize() - value.get("px").getInt(1);

        JsonValue fields = value.get("fieldInstances");
        for (int i = 0; i < fields.size; i++) {
            parseField(fields.get(i), types);
        }
    }

    private void parseField(JsonValue jsonValue, LDTKTypes types) {
        String valueName = jsonValue.getString("__identifier");
        values.put(valueName, types.convert(jsonValue));
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public <T> T get(String name) {
        return (T) values.get(name);
    }

    /**
     * Converts the LDTKEntity to a given type.
     *
     * @param type The type to convert the LDTKEntity to. Looks for {@link LDTKField} annotated fields within the class to fill with data.
     *              <b>Must have a non-parameter constructor!</b>
     * @return The converted object
     * @throws LDTKException if a non-parameter constructor is not present, or the field couldn't be set.
     */
    public <T> T as(Class<T> type) {
        try {
            T instance = ClassReflection.newInstance(type);
            for (Field declaredField : ClassReflection.getDeclaredFields(type)) {
                if (declaredField.isAnnotationPresent(LDTKField.class)) {
                    String entityFieldName = declaredField.getDeclaredAnnotation(LDTKField.class).getAnnotation(LDTKField.class).value();
                    if (entityFieldName.isEmpty()) {
                        entityFieldName = declaredField.getName();
                    }

                    declaredField.setAccessible(true);
                    declaredField.set(instance, values.get(entityFieldName));
                } else if(declaredField.isAnnotationPresent(LDTKPositionField.class)) {
                    Vector2 vec = new Vector2(x, y);
                    declaredField.setAccessible(true);
                    declaredField.set(instance, vec);
                }
            }
            return instance;
        } catch (ReflectionException e) {
            throw new LDTKException(e);
        }
    }
}
