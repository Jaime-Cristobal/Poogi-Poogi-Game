package com.mygdx.core.mechanics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.Scaler;
import com.mygdx.core.actor.creation.CreateBody;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.player.Player;

public class Teleporter
{
    final private CreateBody body = new CreateBody(BodyDef.BodyType.StaticBody);

    /**This will just create a body. The body should be placed where ever the
     * teleporter texture is placed on the map.*/
    public Teleporter(World world, Object ID, float x, float y, float rotation)
    {
        body.setID(ID);
        //body.setFilter(FilterID.teleporter_category, FilterID.player_category);
        body.setData(1, 0, false);
        body.create(world, x, y, 39 / Scaler.scaleX, 10 / Scaler.scaleY, true);
        body.getBody().setTransform(body.getBody().getPosition().x, body.getBody().getPosition().y, rotation);
        body.setActive(true);
    }

    public Vector2 getPos()
    {
        return body.getBody().getPosition();
    }

    /**The boolean returns should be used to trigger effects when using the teleporters.
     * @param player needed for positions to be changed.
     * @param target should be the teleporter the player will teleport to.
     * @param option will be true if the user clicks on the bubble prompt.*/
    public boolean teleportation(Player player, Teleporter target, boolean option)
    {
        if(option)
        {
            player.getBody().setTransform(target.body.getBody().getPosition().x,
                    target.body.getBody().getPosition().y + 5, player.getAngle());
            return true;
        }
        return false;
    }

    public void setPos(float x, float y)
    {
        body.getBody().setTransform(x, y, body.getBody().getAngle());
    }

    public void setPos(Vector2 pos)
    {
        body.getBody().setTransform(pos, body.getBody().getAngle());
    }
}
