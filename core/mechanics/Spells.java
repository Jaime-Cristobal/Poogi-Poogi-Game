package com.mygdx.core.mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.core.Core;
import com.mygdx.core.actor.creation.CreateActor;
import com.mygdx.core.collision.EventManager;

/**DEPRECATED
 * Look at Magic class*/
public final class Spells
{
    final private CreateActor projectile;
    final private Image icon;
    private boolean fireProjectile;
    private Vector2 target, playerPos;
    private float direction = 1;

    /**@param spellAct can either be a lambda for textures or create a separate new object
     *                 for animations*/
    public Spells(Core core, Stage stage, String iconFile, CreateActor spellAct)
    {
        this.projectile = spellAct;
        icon = new Image((Texture) core.assetmanager.manager.get(iconFile));
        icon.setSize(52, 55);
        //icon.setPosition(-175, -290);
        stage.addActor(icon);

        setClickable();
    }

    private void setClickable()
    {
        icon.addListener(new ClickListener()
        {
           @Override
           public void clicked(InputEvent event, float x, float y)
           {
                fireProjectile = true;
                System.out.println("CLicked");
                projectile.getBody().setTransform(playerPos.x, playerPos.y + 5,
                        MathUtils.atan2(MathUtils.sin(target.y), MathUtils.cos(target.x)));
                projectile.getBody().setLinearVelocity(40 * direction, 0);
                //projectile.getBody().setLinearVelocity(MathUtils.cos(target.x) * 70, MathUtils.sin(target.y) * 70);
           }
        });
    }

    public void setIconPos(float x, float y)
    {
        icon.setPosition(x, y);
    }

    /**@param target is the vector2 coordinates from a box2D body. The target
     *        should be a box2D body.*/
    public void setTarget(Vector2 target)
    {
        this.target = target;
    }

    public void setDirection(float direction)
    {
        this.direction = direction;
    }

    public void display(float delta, Vector2 playerPos)
    {
        this.playerPos = playerPos;

        hitCondition();
        if(fireProjectile)
        {
            projectile.display(delta);
        }
    }

    private void hitCondition()
    {
        if(EventManager.inContact(projectile.getBody().getUserData()))
        {
            System.out.println("HIT");
            fireProjectile = false;
            //put after collision animation or texture or a condition for it here
        }
    }
}
