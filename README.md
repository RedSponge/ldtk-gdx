## LDTK-Gdx

[![](https://jitpack.io/v/RedSponge/ldtk-gdx.svg)](https://jitpack.io/#RedSponge/ldtk-gdx)

## What is this?

This is a library which aims to allow easy integration of [LDTK](https://ldtk.io/) (Level Developer's Toolkit) in LibGDX projects. [LDTK](https://ldtk.io/) is an awesome, user friendly 2D tile-editor.

## Getting Started

### Dependency Stuff

###### Core dependency:

```groovy
implementation 'com.github.RedSponge:ldtk-gdx:v0.2'
```

###### GWT Dependency:

In html dependencies:

```groovy
implementation "com.github.RedSponge:ldtk-gdx:v0.2:sources"
```

In `GdxDefinitions.gwt.xml`:

```xml
<inherits name="com.redsponge.LDTKGdx"/>
```

### Writing some code

#### The type system (The bare minimum for map loading)

LDTK has its own type system, consisting of numbers, strings, positions, and even your own classes and enums. The `LDTKTypes` class is in charge of conversions between LDTK types and Java types. You must create and handle an instance of it to load maps.

```java
LDTKTypes types = new LDTKTypes();
```

#### Loading a map

To load an `ldtk` file (a map), use the `LDTKMap` class:

```java
LDTKTypes types = new LDTKTypes();
LDTKMap map = new LDTKMap(types, Gdx.files.internal("my_map.ldtk"));
```

Once loaded, you can access the different levels by name or uid:

```java
LDTKLevel myLevel = map.getLevel("Level_0");
LDTKLevel myOtherLevel = map.getLevel(123);
```

#### Drawing a level

Once you have access to an `LDTKLevel` instance, you can simply call its `render` method with an active `SpriteBatch` to draw it. It will be rendered at its specified position in the editor (So to draw an entire map, just loop over the levels and call `render` for each one).

```Java
// Drawing a specific level:
myLevel.render(batch);

// Drawing a whole map:
for(LDTKLevel level : map.getLevels()) {
	level.render(batch);
}
```

## Beyond the Getting Started part

### Accessing layers in a level

There are 2 essential types of layers in LDTK: entity layers and tile layers. Tile layers are in charge of drawing and entity layers hold extra information.

```java
// Access layers by type:
Array<LDTKEntityLayer> entityLayers = level.getEntityLayers();
Array<LDTKTileLayer> tileLayers = level.getTileLayers();

// Access a layer by its name
LDTKLayer layer = level.getLayerByName("BoundingBoxes");
// Or even more useful - downcast it
LDTKEntityLayer layerButDowncasted = (LDTKEntityLayer) level.getLayerByName("BoundingBoxes");
```

### Accessing Entities

#### The regular way

To access an entity layer's entities and their fields, simply use the `getEntitiesOfType` method on the layer to get the enemies, and the `get` method on each enemy to get its field contents.

```java
// Getting all entities of type "Enemy"
Array<LDTKEntity> enemies = entityLayer.getEntitiesOfType("Enemy");
```

```Java
// Accessing an enemy's fields
LDTKEntity myEnemy = enemies.get(0);
String enemyType = myEnemy.get("type");
int enemyHealth = myEnemy.get("health");
int enemyX = myEnemy.getX();
int enemyY = myEnemy.getY();
```

```Java
// Printing all of the enemies
for(LDTKEntity enemy : enemies) {
    String type = enemy.get("type");
    int hp = enemy.get("health");
    
    System.out.println("Oh look! An enemy of type " + type + " with " + hp + " HP at (" + enemy.getX() + ", " + enemy.getY() + ")!");
}
```



#### The cool Reflection-ey way

**(Thanks to [Lyze](https://github.com/lyze237/) for making this work with GWT (html)).**

Instead of using the generic `LDTKEntity` class, you can make your own class for each defined entity, and access the fields that way:

```java
package com.redsponge.ldtktest;
// Example enemy class
public class LoadedEnemy
{
    // The position of the enemy in the map (getX(), getY())
    @LDTKPositionField
    private Vector2 position;
    
    // Other fields
    @LDTKField
    private String type;
    
    @LDTKField("health")
    private int hp;
    
    public Vector2 getPosition() {
        return position;
    }
    
    // Other getters ...
}
```

Then, to get an array of the enemies from before, but converted to this new, cool class, just use the `getEntitiesConverted` method on the entity layer:


```java
Array<LoadedEnemy> enemies = entityLayer.getEntitiesConverted("Enemy", LoadedEnemy.class);
```


**Important:** The conversion is mildly expensive (All objects need to be constructed in the given type), and so the result should be cached.

##### For GWT projects:
For each one of the classes which use the annotations, add a line in your `GdxDefinitions.gwt.xml` which looks like this:
```xml
    <extend-configuration-property name="gdx.reflect.include" value="path.to.your.Class" />
```
i.e.
```xml
    <extend-configuration-property name="gdx.reflect.include" value="com.redsponge.ldtktest.LoadedEnemy" />
```

### Custom enums

LDTK allows you to create custom enums as entity fields (for example, an `EnemyType` enum would be much better than a simple string field). In order to use these enums in your code, you must create matching Java enum classes.

```java
public enum EnemyType {
    Zombie,
    Skeleton,
    Bat
}
```

Then, register then enum in your `LDTKTypes` instance (Yes, the one from before 😄) 

```java
// types.addEnum("Type name in LDTK", MatchingClass.class)
types.addEnum("EnemyType", EnemyType.class);
```

After that, just load your map(s) regularly. The enum will now be the type you receive when calling `get` on fields which should return the enum, and it will be recognized properly in annotated (reflection-y) classes.

### Neighbouring Levels

You might want to get the levels neighbouring a level, for transitions/drawing purposes. To do so, simply use the `getNeighbours` method a level, and then use `get` with the direction of your choosing:

```java
IntArray neighbours = level.getNeighbours().get(NeighbourDirection.Left);
```

This will return an `IntArray` containing the level ids of the neighbours. (Reminder: `map.getLevel(id)` to get a level by its id 😉)

To get all neighbours around a level, regardless of direction, just use `NeighbourDirection.All`.


## Acknowledgements

 - [LDTK](https://ldtk.io/) by Sebastian Benard ("deepnight").
 - Huge thanks to [Lyze237](https://github.com/lyze237/) for making GWT support work.
 
