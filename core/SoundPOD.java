package com.mygdx.core;

import com.badlogic.gdx.audio.Sound;

public final class SoundPOD
{
    public final Sound eggCatch, eggBad, entry, disapear, bounceHit, explode, spawn,
                        gemFound, gemPicked, buttonClicked, pickBucket, pourWater,
                        portalRift, laser4, laser5;

    public SoundPOD(Asset assetmanager)
    {
        eggCatch = (Sound) assetmanager.getFile("egg_catch.ogg");
        eggBad = (Sound) assetmanager.getFile("egg_bad.ogg");
        entry = (Sound) assetmanager.getFile("entry_sound.ogg");
        disapear = (Sound) assetmanager.getFile("disapear_sound.ogg");
        bounceHit = (Sound) assetmanager.getFile("enemy_hit.ogg");
        explode = (Sound) assetmanager.getFile("explodemini.ogg");
        spawn = (Sound) assetmanager.getFile("spawn.ogg");
        gemFound = (Sound) assetmanager.getFile("completetask_0.ogg");
        gemPicked = (Sound) assetmanager.getFile("beltHandle1.ogg");
        buttonClicked = (Sound) assetmanager.getFile("pop.ogg");
        pickBucket = (Sound) assetmanager.getFile("metal-hammer-hit-01.ogg");
        pourWater = (Sound) assetmanager.getFile("pour_water.ogg");
        portalRift = (Sound) assetmanager.getFile("teleport.ogg");
        laser4 = (Sound) assetmanager.getFile("laser4.ogg");
        laser5 = (Sound) assetmanager.getFile("laser5.ogg");
    }

    public void dispose()
    {
        eggCatch.dispose();
        eggBad.dispose();
        entry.dispose();
        disapear.dispose();
        bounceHit.dispose();
        explode.dispose();
        spawn.dispose();
        gemFound.dispose();
        gemPicked.dispose();
        buttonClicked.dispose();
        pickBucket.dispose();
        pourWater.dispose();
        portalRift.dispose();
        laser4.dispose();
        laser5.dispose();
    }
}
