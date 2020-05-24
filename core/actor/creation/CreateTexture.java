package com.mygdx.core.actor.creation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.BodyEditorLoader;
import com.mygdx.core.Core;
import com.mygdx.core.Scaler;
import com.mygdx.core.actor.Render;

/**
 * Created by seacow on 12/17/2017.
 */

public class CreateTexture implements CreateActor
{
    final private Core core;

    public CreateBody box2dBody;
    private Render renderer;
    private Sprite sprite;

    private String file;
    private float x;
    private float y;
    private float width;
    private float height;
    private float rotation;
    private float offSetX = 0, offSetY = 0;
    private boolean toDisplay = false;

    private CreateTexture(String name, Core core)
    {
        this.core = core;
        file = name;

        x = 0;
        y = 0;

        rotation = 0;
    }

    /**for empty/no texture box2d bodies - mostly for invisible platforms/walls*/
    public CreateTexture(BodyDef.BodyType type)
    {
        this(null, null);

        box2dBody = new CreateBody(type);
        renderer = new Render();

        file = null;
        sprite = null;
    }

    /**for texture sprites*/
    public CreateTexture(String file, Core core, BodyDef.BodyType type)
    {
        this(file, core);

        box2dBody = new CreateBody(type);
        renderer = new Render();

        sprite = new Sprite(core.assetmanager.getTexture(this.file));
    }

    public CreateTexture(String file, Core main, BodyDef.BodyType type, float rotation)
    {
        this(file, main, type);
        this.rotation = rotation;
    }

    public CreateTexture(TextureRegion texReg, Core core, BodyDef.BodyType type)
    {
        this.core = core;
        box2dBody = new CreateBody(type);
        renderer = new Render();
        sprite = new Sprite(texReg);
    }

    /**Use this with a contact filtering class*/
    public void setFilter(short category, short mask)
    {
        box2dBody.setFilter(category, mask);
    }

    public void setGroupFilter(short categoy)
    {
        box2dBody.setAsFilterGroup(categoy);
    }


    /**Manually set the body's density(heaviness) and restitution(bounciness)
     * values.
     * The default values are density = 0 and restitution = 0.1f*/
    public void setData(float density, float resitution, boolean lockRotation)
    {
        box2dBody.setData(density, resitution, lockRotation);
    }

    /**For IDing a container of objects containing the same name file. Have the same
     * name files means they all have same UserData ID*/
    public void setUniqueID(Object ID)
    {
        box2dBody.setID(ID);
    }

    public void setNewTexture(String atlas, String texture)
    {
        sprite.setRegion(core.assetmanager.getTextureFromAtlas(atlas, texture));
    }
    /**NOT TESTED*/

    public void create(World world, float xPos, float yPos, boolean isSensor)
    {
        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.create(world, x, y, width, height, isSensor);
        box2dBody.getBody().setActive(true);

        box2dBody.getBody().setAngularVelocity(rotation * Gdx.graphics.getDeltaTime());
    }

    public void create(World world, float xPos, float yPos, float w, float h, boolean isSensor)
    {
        width = w;
        height = h;

        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.create(world, x, y, width, height, isSensor);
        box2dBody.getBody().setActive(true);

        box2dBody.getBody().setAngularVelocity(rotation * Gdx.graphics.getDeltaTime());
    }

    public void createCustomBody(World world, BodyEditorLoader loader, String file, float xPos, float yPos,
                       float w, float h, float scale, boolean sensor)
    {
        width = w;
        height = h;

        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.setBody(world, loader, file, x, y, scale, sensor);
        box2dBody.getBody().setActive(true);
    }

    public void createOriginalCustom(World world, float xPos, float yPos, float w, float h,
                                   float renderW, float renderH, boolean isSensor)
    {
        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.create(world, x, y, w, h , isSensor);
        box2dBody.getBody().setActive(true);

        width = renderW;
        height = renderH;
    }

    public void setDisplay(boolean toDisplay)
    {
        this.toDisplay = toDisplay;
    }

    /**Only render if the file isn't null, empty box2D bodies do not need to rendered*/
    public void display()
    {
        //sprite.setRotation((float)Math.toDegrees(box2dBody.getBody().getAngle()));
        renderer.render(core, sprite, box2dBody.getBody(), width ,
                height);
    }

    /**Only render if the file isn't null, empty box2D bodies do not need to rendered*/
    public void display(float delta)
    {
        if(delta != 0)
            sprite.setRotation((float)Math.toDegrees(box2dBody.getBody().getAngle()));
        renderer.render(core, sprite, box2dBody.getBody(), width ,
                height);
    }

    public void display(float offSetX, float offSetY)
    {
        sprite.setRotation((float)Math.toDegrees(box2dBody.getBody().getAngle()));
        renderer.render(core, sprite, box2dBody.getBody(), width ,
                height, offSetX, offSetY);
    }

    public void setOffSet(float xOff, float yOff)
    {
        offSetX = xOff;
        offSetY = yOff;
    }

    public void displayCustom()
    {
        renderer.renderCustomBod(core, sprite, box2dBody.getBody(), width, height, offSetX, offSetY);
    }

    public void displayCustom(float delta, float offSetX, float offSetY)
    {
        renderer.renderCustomBod(core, sprite, box2dBody.getBody(), width, height, offSetX, offSetY);
    }

    /**false sets the box2D body to sleep (unmovable)*/
    public void setActive(boolean active)
    {
        box2dBody.getBody().setActive(active);
    }

    public boolean isActive()
    {
        return box2dBody.getBody().isActive();
    }

    /**speed is set on a x and y axis plane*/
    public void setSpeed(float xSpeed, float ySpeed)
    {
        box2dBody.getBody().setLinearVelocity(xSpeed, ySpeed);
    }

    public void applyForce(float xVal, float yVal)
    {
        box2dBody.getBody().applyLinearImpulse(xVal, yVal, getX(), getY(), true);
    }

    /**sets the position to whatever the values of xPos and yPos are*/
    public void setPosition(float xPos, float yPos)
    {
        box2dBody.getBody().setTransform(xPos, yPos, box2dBody.getBody().getAngle());
    }

    /**Sets the width as a negative value, flipping the rendered image to
     * the opposite side*/
    public void flip()
    {
        width *= -1;
    }

    public float getX()
    {
        return box2dBody.getBody().getPosition().x;
    }

    public float getY()
    {
        return box2dBody.getBody().getPosition().y;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getVelX()
    {
        return box2dBody.getBody().getLinearVelocity().x;
    }

    public float getVelY()
    {
        return box2dBody.getBody().getLinearVelocity().y;
    }

    public Body getBody()
    {
        return box2dBody.getBody();
    }

    public void setNoGravity()
    {
        box2dBody.getBody().setGravityScale(0f);
    }

    public void resetUserData()
    {
        box2dBody.resetUserData();
    }

    public boolean ifDisplayed()
    {
        return toDisplay;
    }

    /**Non functional; don't use*/
    public boolean hasOffSets()
    {
        return false;
    }
}
