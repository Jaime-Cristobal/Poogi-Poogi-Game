package com.mygdx.core.actor.engineer;

import com.badlogic.gdx.math.Vector2;

public interface EngCreatable
{
    /**Used for spawning for spawning purposes. Should be preferably be
     * activated through some kind of listener function (click, etc...)*/
    void setSpawn(Vector2 playerPos, int index, float speedX, float speedY);

    int getSize();

    Vector2 getPos();
}
