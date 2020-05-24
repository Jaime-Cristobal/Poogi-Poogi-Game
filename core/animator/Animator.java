package com.mygdx.core.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.core.Core;
import com.mygdx.core.Scaler;

/**
 * Created by FlapJack on 7/22/2017.
 *
 * Only to be used for animated sprites packed into textureatlas.
 *
 */

public final class Animator
{
    private final Core core;
    private final Array<Animation> animation = new Array<Animation>();
    private ArrayMap<String, Float> region;
    private float elapsed_time = 0f;
    private float endTime = -1;
    private int playback = 0;

    private String file;
    private boolean loop = true;

    private float width, height, originWidth, originHeight = 0;
    private float box2dScale = 0;

    private boolean loopBack = false, backwards = false;

    public Animator(String file, Core core)
    {
        this.file = file;
        this.core = core;
    }

    /**reference an existing region*/
    public void setRegion(ArrayMap<String, Float> region)
    {
        this.region = region;
        create();
    }

    /**used for single region atlases; Warning: calling this will */
    public void setRegion(String regionName, float frameSpeed)
    {
        region = new ArrayMap<String, Float>();
        region.put(regionName, frameSpeed);
        create(regionName);
    }

    /**adds a region to the existing region created*/
    public void addRegion(String regionName, float frameSpeed)
    {
        if(region == null)
        {
            region = new ArrayMap<String, Float>();
            Gdx.app.log("Class Animator.java", "addRegion created a new region with: " + regionName);
        }
        region.put(regionName, frameSpeed);
        create(regionName);
    }

    /**Used when referencing another region*/
    private void create()
    {
        for(int n = 0; n < this.region.size; n++)
        {
            System.out.println("delta time at " + Gdx.graphics.getDeltaTime());
            animation.add(new Animation<TextureRegion>(region.getValueAt(n) * Math.min(Gdx.graphics.getDeltaTime(), 1/30f),
                    core.assetmanager.manager.get(this.file, TextureAtlas.class).findRegions(this.region.getKeyAt(n))));
        }

        width = core.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedWidth;
        height = core.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedHeight;

        originWidth = width / 2;
        originHeight = height / 2;
    }

    /**For setting region */
    private void create(String regionName)
    {
        System.out.println("delta time at " + Gdx.graphics.getDeltaTime());
        animation.add(new Animation<TextureRegion>(region.get(regionName) * Math.min(Gdx.graphics.getDeltaTime(), 1/30f),
                core.assetmanager.manager.get(this.file, TextureAtlas.class).findRegions(regionName)));

        if(width == 0 || height == 0)
        {
            width = core.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedWidth;
            height = core.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedHeight;

            originWidth = width / 2;
            originHeight = height / 2;
        }
    }

    public void setOrigin(float originWidth, float originHeight)
    {
        this.originWidth = originWidth;
        this.originHeight = originHeight;
    }

    /**
     * NOTE: Should be considered for a name change. Also consider creating
     *       region finder for sprites with one animation set.
     * @param call will set the sprite to the region animation. This should be
     *             the name of region in the format [name]_0, [name]_1.....etc.
     *             Note: Ideal for sprite packs with multiple animations.
     * */
    public void findRegion(String call)
    {
        int n = 0;

        for(ObjectMap.Entry<String, Float> iter : region)
        {
            if(iter.key.equals(call))
            {
                playback = n;
                return;
            }

            n++;
        }
    }

    public String getRegion()
    {
        return region.getKeyAt(playback);
    }

    public boolean checkRegion(String call)
    {
        if(region.getKeyAt(playback).equals(call))
            return true;
        return false;
    }

    /**playback is the type or set of animations you want to render from the atlas.
     * The ordering is heavily dependant on the order you passed the regions*/
    public void render(Batch batch, float x, float y)
    {
        if(loopBack)
        {
            if(animation.get(playback).isAnimationFinished(elapsed_time))
                if(!backwards)
                    backwards = true;
            if(elapsed_time < 0)
            {
                elapsed_time = 0;
                backwards = false;
            }

            if(backwards)
                elapsed_time -= Gdx.graphics.getDeltaTime();
            else
                elapsed_time += Gdx.graphics.getDeltaTime();
        }
        else
            elapsed_time += Gdx.graphics.getDeltaTime();

        batch.draw((TextureRegion) animation.get(playback).getKeyFrame(elapsed_time, loop),
                ((x * Scaler.PIXELS_TO_METERS) - width/2) + box2dScale,
                (y * Scaler.PIXELS_TO_METERS) - height/2,
                width, height);
    }

    public void render(Batch batch, float x, float y, float delta)
    {
        if(loopBack)
        {
            if(animation.get(playback).isAnimationFinished(elapsed_time))
                if(!backwards)
                    backwards = true;
            if(elapsed_time <= 0)
                backwards = false;

            if(backwards)
                elapsed_time -= delta;
            else
                elapsed_time += delta;

            if(elapsed_time < 0)
                elapsed_time = 0;
        }
        else
            elapsed_time += delta;

        batch.draw((TextureRegion) animation.get(playback).getKeyFrame(elapsed_time, loop),
                ((x * Scaler.PIXELS_TO_METERS) - width/2) + box2dScale,
                (y * Scaler.PIXELS_TO_METERS) - height/2,
                width, height);
    }

    public void setLoopBack(boolean val)
    {
        loopBack = val;
        loop = !loopBack;
    }

    public void renderIndividual(Batch batch, float x, float y, float delta)
    {
        if(loopBack)
        {
            if(animation.get(playback).isAnimationFinished(elapsed_time))
                if(!backwards)
                    backwards = true;
            if(elapsed_time <= 0)
            {
                backwards = false;
                elapsed_time = 0;
            }

            if(backwards)
                elapsed_time -= delta;
            else
                elapsed_time += delta;
        }
        else
            elapsed_time += delta;

        batch.draw((TextureRegion) animation.get(playback).getKeyFrame(elapsed_time, loop),
                x, y, width, height);
    }

    public void renderStill(Batch batch, float x, float y)
    {
        batch.draw((TextureRegion) animation.get(playback).getKeyFrame(0, false),
                x, y, width, height);
    }

    /**
     * @param angle should be in DEGREES!
     * */
    public void render(Batch batch, float x, float y, float delta, float angle)
    {
        if(loopBack)
        {
            if(animation.get(playback).isAnimationFinished(elapsed_time))
                if(!backwards)
                    backwards = true;
            if(elapsed_time <= 0)
                backwards = false;

            if(backwards)
                elapsed_time -= delta;
            else
                elapsed_time += delta;

            if(elapsed_time < 0)
                elapsed_time = 0;
        }
        else
            elapsed_time += delta;

        if(angle < 0)
        {
            angle += 360;
        }

        batch.draw((TextureRegion) animation.get(playback).getKeyFrame(elapsed_time, loop),
                ((x * Scaler.PIXELS_TO_METERS) - width/2) + box2dScale,
                (y * Scaler.PIXELS_TO_METERS) - height/2,
                width / 2, height / 2, width, height, 1f, 1f, angle, false);
    }

    /**Only works for the rock_01.atlas for now. Not tested on other sprites.*/
    public void renderCustomBod(Batch batch, float delta, float x, float y, float offSetX, float offSetY)
    {
        if(loopBack)
        {
            if(animation.get(playback).isAnimationFinished(elapsed_time))
                if(!backwards)
                    backwards = true;
            if(elapsed_time <= 0)
                backwards = false;

            if(backwards)
                elapsed_time -= delta;
            else
                elapsed_time += delta;

            if(elapsed_time < 0)
                elapsed_time = 0;
        }
        else
            elapsed_time += delta;

        batch.draw((TextureRegion) animation.get(playback).getKeyFrame(elapsed_time, loop),
                ((x * Scaler.PIXELS_TO_METERS) - width/2) + box2dScale + offSetX,
                (y * Scaler.PIXELS_TO_METERS) - height/2 + offSetY,
                width, height);
    }

    public void setLoop(boolean loop)
    {
        this.loop = loop;
    }

    /**
     * Used mainly with function ifFrameEnd() below. Will set the endTime to
     * what time the animation ended.
     *
     * Obsolete
     * */
    public void recordEndTime()
    {
        if(animation.get(playback).isAnimationFinished(elapsed_time))
        {
            endTime = elapsed_time;
        }
    }

    /**
     * Best paired with func ifFinished() below. For non-looping animations, placing ifFinished
     * inside an if statement with resetTime() inside will ensure the animation plays again.
     * */
    public void resetTime()
    {
        endTime = -1;
        elapsed_time = 0;
    }

    /**
     * @return true -> if the elapse_time has reach the end of the animation
     *
     * Obsolete, use ifFinished()
     * */
    public boolean ifFrameEnd()
    {
        if(endTime == elapsed_time)
            return true;

        return false;
    }

    /**For manually upscaling and downscaling resolutions*/
    public void setScale(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public void flip(float val)
    {
        width *= -1;

        box2dScale = val;
    }

    public float getFlipVal()
    {
        if(width < 0)
            return -1;
        else if(width > 0)
            return 1;

        return 0;
    }

    public boolean ifFinished()
    {
        return animation.get(playback).isAnimationFinished(elapsed_time);
    }

    /**if facing to the right -> return positive, left -> return negative*/
    public float getDirection()
    {
        return Math.signum(width);
    }

    public void setPosScale(int val)
    {
        box2dScale = val;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public boolean isBackwards()
    {
        return backwards;
    }
}
