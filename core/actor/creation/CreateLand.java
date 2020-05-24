package com.mygdx.core.actor.creation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.core.Core;

public class CreateLand
{
    final private Core core;
    private Array<CreateTexture> land = new Array<CreateTexture>();
    private Array<TextureRegion> decorations = new Array<TextureRegion>();

    public CreateLand(Core core)
    {
        this.core = core;
    }
}
