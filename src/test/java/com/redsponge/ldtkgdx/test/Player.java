package com.redsponge.ldtkgdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Player implements Disposable {

    private Vector2 pos;
    private Texture[] textures;
    private Animation<Texture> anim;
    private float time;

    public Player() {
        pos = new Vector2();

        textures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            textures[i] =new Texture("player_" + (i + 1) + ".png");
        }
        anim = new Animation<>(0.1f, textures);
        anim.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        time += delta;
        float speed = 200;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pos.x += speed * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pos.x -= speed * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pos.y += speed * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pos.y -= speed * delta;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(anim.getKeyFrame(time), pos.x, pos.y);
    }

    @Override
    public void dispose() {

    }

    public Vector2 getPosition() {
        return pos;
    }
}
