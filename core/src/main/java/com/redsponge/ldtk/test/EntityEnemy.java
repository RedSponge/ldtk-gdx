package com.redsponge.ldtk.test;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.ldtk.LDTKField;
import com.redsponge.ldtk.LDTKPositionField;

public class EntityEnemy {

    @LDTKPositionField
    private Vector2 pos;

    @LDTKField("integer")
    private int test;

    @LDTKField
    private String string;

    @LDTKField("boolean")
    private boolean myBoolean;

    @LDTKField("enemyType")
    private EnemyType type;

    @LDTKField
    private String multilines;

    @LDTKField
    private Vector2 point;

    @LDTKField("file_path")
    private String filePath;

    public EntityEnemy() {
    }

    public EntityEnemy(int test, String string, boolean myBoolean, EnemyType type, String multilines, Vector2 point, String filePath) {
        this.test = test;
        this.string = string;
        this.myBoolean = myBoolean;
        this.type = type;
        this.multilines = multilines;
        this.point = point;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "EntityEnemy{" +
                "test=" + test +
                ", string='" + string + '\'' +
                ", myBoolean=" + myBoolean +
                ", type=" + type +
                ", multilines='" + multilines + '\'' +
                ", point=" + point +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getTest() {
        return test;
    }

    public String getString() {
        return string;
    }

    public boolean isMyBoolean() {
        return myBoolean;
    }

    public EnemyType getType() {
        return type;
    }

    public String getMultilines() {
        return multilines;
    }

    public Vector2 getPoint() {
        return point;
    }

    public String getFilePath() {
        return filePath;
    }
}
