package com.mygdx.core.mechanics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateAnimation;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.collision.FilterID;

public class Portal
{
    //final private CreateAnimation teleporter;
    final private CreateTexture teleporter;

    public Portal(Core core, World world)
    {
        teleporter = new CreateTexture("select.png", core, BodyDef.BodyType.DynamicBody);
        teleporter.setFilter(FilterID.mine_catageory, FilterID.floor_category);
        teleporter.setData(0.9f, 0, true);
        teleporter.create(world, -1000, -1000, 20, 20, false);
        teleporter.setActive(false);
    }

    public void spawn(Vector2 playerPos, float xOffSet, float yOffSet, float xForce, float yForce)
    {
        teleporter.getBody().setActive(true);
        teleporter.getBody().setLinearVelocity(0, teleporter.getBody().getGravityScale());
        teleporter.setPosition(playerPos.x + xOffSet, playerPos.y + yOffSet);
        teleporter.getBody().applyLinearImpulse(xForce, yForce, teleporter.getBody().getPosition().x,
                teleporter.getBody().getPosition().y, true);
    }
}
