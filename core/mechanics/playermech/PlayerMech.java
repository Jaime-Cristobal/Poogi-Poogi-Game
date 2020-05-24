package com.mygdx.core.mechanics.playermech;

import com.badlogic.gdx.math.Vector2;

public interface PlayerMech
{
    /**Used for spawning for spawning purposes. Should be preferably be
     * activated through some kind of listener function (click, etc...)*/
    void setSpawn(float x, float y, int index, float direction);

    int getSize();

    int getState();
}
