package com.mygdx.core.mechanics;

import com.badlogic.gdx.math.Vector2;

public final class FSM
{
    private Vector2 playerPos = null;

    /**This func. manages the state of an actor (likely an enemy).
     *          ID | States
     *          ----------
     * @return  0 -> Combat
     *          1 -> Idle
     *          */
    public int getState(Vector2 actor)
    {
        if(playerPos == null)
            return 1;

        if((playerPos.x > actor.x - 25 && playerPos.x < actor.x + 25) &&
                (playerPos.y > actor.y - 10 && playerPos.y < actor.y + 10))
            return 0;

        return 1;
    }

    public void update(Vector2 playerPos)
    {
        this.playerPos = playerPos;
    }
}
