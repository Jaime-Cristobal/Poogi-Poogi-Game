package com.mygdx.core.mechanics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class Spawner
{
    private float minX, maxX, minY, maxY = 0;

    public void setRange(float minX, float maxX, float minY, float maxY)
    {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public Vector2 getRandomize(float minX, float maxX, float minY, float maxY)
    {
        setRange(minX, maxX, minY, maxY);

        return new Vector2(MathUtils.random(this.minX, this.maxX), MathUtils.random(this.minY, this.maxY));
    }

    public float getRandX()
    {
        return MathUtils.random(this.minX, this.maxX);
    }

    public float getRandY()
    {
        return MathUtils.random(this.minY, this.maxY);
    }

    /**Ignores the values initialized in setRandge().*/
    public float getRandX(float minX, float maxX)
    {
        return MathUtils.random(minX, maxX);
    }

    /**@see Spawner#getRandX(float, float)*/
    public float getRandY(float minY, float maxY)
    {
        return MathUtils.random(minY, maxY);
    }
}
