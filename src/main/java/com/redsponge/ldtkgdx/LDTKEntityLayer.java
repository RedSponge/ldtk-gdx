package com.redsponge.ldtkgdx;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class LDTKEntityLayer extends LDTKLayer {

    private final HashMap<String, Array<LDTKEntity>> entityMap;

    public LDTKEntityLayer(JsonValue value, LDTKTypes types) {
        super(value);
        entityMap = new HashMap<>();
        JsonValue entityInstanceList = value.get("entityInstances");
        for (int i = 0; i < entityInstanceList.size; i++) {
            parseEntity(entityInstanceList.get(i), types);
        }
    }

    private void parseEntity(JsonValue entityJson, LDTKTypes types) {
        LDTKEntity entity = new LDTKEntity(entityJson, types, this);
        if(entityMap.containsKey(entity.getId())) {
            entityMap.get(entity.getId()).add(entity);
        } else {
            Array<LDTKEntity> entityArray = new Array<>();
            entityArray.add(entity);
            entityMap.put(entity.getId(), entityArray);
        }
    }

    public Array<LDTKEntity> getEntitiesOfType(String type) {
        return entityMap.get(type);
    }

    public <T> Array<T> getEntitiesConverted(String type, Class<T> convertTo) {
        Array<LDTKEntity> entities = entityMap.get(type);
        Array<T> output = new Array<>();
        for (int i = 0; i < entities.size; i++) {
            output.add(entities.get(i).as(convertTo));
        }
        return output;
    }

}
