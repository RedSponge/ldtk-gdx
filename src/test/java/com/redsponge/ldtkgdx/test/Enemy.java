package com.redsponge.ldtkgdx.test;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.ldtkgdx.LDTKField;
import com.redsponge.ldtkgdx.LDTKPositionField;

public class Enemy {

    @LDTKPositionField
    private Vector2 pos;

    @LDTKField("enemyType")
    private EnemyType type;

    public Enemy() {
    }

    public Vector2 getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "pos=" + pos +
                ", type=" + type +
                '}';
    }
}
