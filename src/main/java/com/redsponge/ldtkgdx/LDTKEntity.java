package com.redsponge.ldtkgdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.redsponge.ldtkgdx.annotations.LDTKField;
import com.redsponge.ldtkgdx.annotations.LDTKPositionField;

import java.util.HashMap;

/**
 * Represents an LDTK map entity
 */
public class LDTKEntity {

    /**
     * The enemy's type
     */
    private final String id;

    /**
     * Position (in world-coordinates)
     */
    private final int x, y;
    private final HashMap<String, Object> values;

    /**
     * @param value - The JSON value to parse
     * @param types - The type-provider to use
     * @param layer - The entity's containing layer
     */
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
        try {
            values.put(valueName, types.convert(jsonValue));
        } catch (LDTKException e) {
            throw new LDTKException("Could not convert field `" + valueName + "` for entity `" + id + "`!", e);
        }
    }

    /**
     * @return The LDTK entity identifier (entity name)
     */
    public String getId() {
        return id;
    }

    /**
     * @return X in world coordinates
     */
    public int getX() {
        return x;
    }

    /**
     * @return Y in world coordinates
     */
    public int getY() {
        return y;
    }

    /**
     * Query an entity field and returns it. <i>if many fields are needed, perhaps {@link LDTKEntity#as(Class)} should be used instead</i>
     * @param name The name of the field
     * @param <T> The field type
     * @return The queried field, cast into the correct type
     * @throws LDTKException if the field doesn't exist or couldn't be cast
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        if(!values.containsKey(name)) {
            throw new LDTKException("Entity of type `" + id + "` does not contain a field named `" + name + "`");
        }
        try {
            return (T) values.get(name);
        } catch (ClassCastException e) {
            throw new LDTKException("Could not cast field `" + name + "` to given type!", e);
        }
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

            if(instance instanceof LDTKInjected) {
                ((LDTKInjected) instance).preInjection();
            }

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

            if(instance instanceof LDTKInjected) {
                ((LDTKInjected) instance).postInjection();
            }

            return instance;
        } catch (ReflectionException e) {
            throw new LDTKException(e);
        }
    }
}
