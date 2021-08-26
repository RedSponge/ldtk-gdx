package com.redsponge.ldtkgdx;

import com.badlogic.gdx.utils.IntArray;

import java.util.HashMap;
import java.util.Map;

public class LDTKNeighbours {

    public enum NeighbourDirection {
        Left('w'),
        Right('e'),
        Up('n'),
        Down('s'),
        All('A')

        ;
        private final char dir;

        NeighbourDirection(char dir) {
            this.dir = dir;
        }

        public static NeighbourDirection getForLetter(char let) {
            for (NeighbourDirection value : values()) {
                if(value.dir == let) return value;
            }

            throw new IllegalArgumentException("No direction for letter '" + let + "'");
        }
    }

    private Map<NeighbourDirection, IntArray> neighbours;

    public LDTKNeighbours() {
        neighbours = new HashMap<>();

        for (NeighbourDirection value : NeighbourDirection.values()) {
            neighbours.put(value, new IntArray());
        }
    }

    public void add(int levelUid, NeighbourDirection dir) {
        if(dir == null) throw new IllegalArgumentException("Direction cannot be null!");
        neighbours.get(dir).add(levelUid);

        neighbours.get(NeighbourDirection.All).add(levelUid);
    }

    public void add(int levelUid, char let) {
        add(levelUid, NeighbourDirection.getForLetter(let));
    }

    public IntArray get(NeighbourDirection direction) {
        return neighbours.get(direction);
    }
}
