package com.mygdx.core.actor.creation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.core.BodyEditorLoader;
import com.mygdx.core.Scaler;

/**
 * Created by seacow on 10/27/2017.
 */

public class CreateBody
{
    private short category = 0, mask = 0;
    private boolean filterGroup = false;

    private Body body;
    private BodyDef.BodyType type;
    private BodyDef bodydef = new BodyDef();
    private FixtureDef fixDef = new FixtureDef();

    private float density = 0;
    private float restitution = 0;

    private Object ID = null;

    public CreateBody(BodyDef.BodyType type)
    {
        this.type = type;
    }

    /**Set filters for collision*/
    public void setFilter(short category, short mask)
    {
        this.category = category;
        this.mask = mask;
    }

    public void setAsFilterGroup(short category)
    {
        this.category = category;
        filterGroup = true;
    }

    /**Manually set the body's data such as density(weight) and restitution(bounciness)
     * Call before calling create() for it to take effect*/
    public void setData(float density, float restitution, boolean rotationLock)
    {
        this.density = density;
        this.restitution = restitution;

        bodydef.fixedRotation = rotationLock;
    }

    /**This is for cases where multiple objects will have the same name file and want
     * to distinguish the UserData inside the fixtures with a unique set of ID*/
    public void setID(Object ID)
    {
        this.ID = ID;
    }

    /**Bodydef.BodyType sets whether the body tpye is static, kinematic, or dynamic
     *
     * You still have to fix the resolution scaling for data
     *
     * @param isSensor if false -> the body will register a collision with another body
     *                 but will not be affected by physics (ex. non-sensored body will not
     *                 be stopped by another body)*/
    public void create(World world, float x, float y, float w, float h, boolean isSensor)
    {
        bodydef.type = type;
        bodydef.position.set(x / Scaler.PIXELS_TO_METERS,
                y / Scaler.PIXELS_TO_METERS);                  //box collision at the same dimension as the sprite

        body = world.createBody(bodydef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / 2 / Scaler.PIXELS_TO_METERS,
                h / 2 / Scaler.PIXELS_TO_METERS);      //box collision at the same dimension as the sprite

        fixDef.shape = shape;
        fixDef.restitution = restitution;
        fixDef.density = density;
        fixDef.isSensor = isSensor;
        if(filterGroup)
        {
            fixDef.filter.groupIndex = category;
        }
        else
        {
            fixDef.filter.categoryBits = category;
            fixDef.filter.maskBits = mask;
        }

        if(ID == null)
        {
            Gdx.app.error("Class CreateBody.java", "There is no UserData ID!");
        }

        body.createFixture(fixDef).setUserData(ID);
        body.setUserData(ID);
        body.setActive(false);      //will not move if the body is not active.
        shape.dispose();
    }

    public void createCircle(World world, float x, float y, float radius, boolean isSensor)
    {
        bodydef.type = type;
        bodydef.position.set(x / Scaler.PIXELS_TO_METERS,
                y / Scaler.PIXELS_TO_METERS);                  //box collision at the same dimension as the sprite

        body = world.createBody(bodydef);

        //shape = new PolygonShape();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / Scaler.PIXELS_TO_METERS);

        fixDef.shape = shape;
        fixDef.restitution = restitution;
        fixDef.density = density;
        fixDef.isSensor = isSensor;
        fixDef.filter.categoryBits = category;       //short something = CATEGORY
        fixDef.filter.maskBits = mask;

        if(ID == null)
        {
            Gdx.app.error("Class CreateBody.java", "There is no UserData ID!");
        }

        body.createFixture(fixDef).setUserData(ID);
        body.setUserData(ID);
        body.setActive(false);      //will not move if the body is not active.
        shape.dispose();
    }

    /**This is for custom bodies created from the Physics Body Editor by Aurelien Ribon.
     * -----------------------
     * Links:
     * http://www.aurelienribon.com/post/2012-04-physics-body-editor-pre-3-0-update
     * https://code.google.com/archive/p/box2d-editor/
     * -----------------------
     * @param loader is the class loader that loads the .json file that contains the
     *               custom body created from the Physics Body Editor
     * @param file is NOT THE NAME OF THE FILE (ex. body.json) but instead the name
     *             of the body given in the Physics Body Editor
     * */
    public void setBody(World world, BodyEditorLoader loader, String file, float x , float y, float scale, boolean isSensor)
    {
        bodydef.type = type;
        bodydef.position.set(x / Scaler.PIXELS_TO_METERS,
                y / Scaler.PIXELS_TO_METERS);                  //box collision at the same dimension as the sprite
        body = world.createBody(bodydef);

        //fixDef.shape = shape;
        fixDef.restitution = restitution;
        fixDef.density = density;
        fixDef.isSensor = isSensor;
        fixDef.filter.categoryBits = category;       //short something = CATEGORY
        fixDef.filter.maskBits = mask;

        loader.attachFixture(body, file, fixDef, Scaler.PIXELS_TO_METERS * scale);

        if(ID == null)
        {
            Gdx.app.error("Class CreateBody.java" + ", " + file, "There is no UserData ID!");
        }
        body.setUserData(ID);
        body.setActive(false);      //will not move if the body is not active.
    }

    /**UserData will get resetted every time a map is called
     *
     * Has to be called in show(). Failure to do so will result in a null UserData in
     * class event.*/
    public void resetUserData()
    {
        body.setUserData(ID);
    }

    /**The necessary filters can be called by referencing from the class public variables*/
    public Filter getFilter() {
        return fixDef.filter;
    }

    /**Bodies are */
    public void setActive(boolean active)
    {
        body.setActive(active);
    }

    public Body getBody()
    {
        return body;
    }
}
