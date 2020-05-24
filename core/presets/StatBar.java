package com.mygdx.core.presets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.core.Core;

public class StatBar
{
    final Image frame, statBar;
    private float originWidth, originHeight;

    public StatBar(Core core, Stage stage, String atlas, String frame, String stat)
    {
        this.frame = new Image(core.assetmanager.getTextureFromAtlas(atlas, frame));
        statBar = new Image(core.assetmanager.getTextureFromAtlas(atlas, stat));
        this.frame.setSize(100, this.frame.getHeight());
        statBar.setSize(100, statBar.getHeight());
        stage.addActor(statBar);
        stage.addActor(this.frame);

        originWidth = statBar.getImageWidth();
        originHeight = statBar.getImageHeight();
    }

    public void setPosition(float x, float y)
    {
        frame.setPosition(x, y);
        statBar.setPosition(x, y);
    }

    public void addValue(int value)
    {
        statBar.setSize(statBar.getImageWidth() + value, statBar.getHeight());
    }

    public void addValue(float value)
    {
        statBar.setSize(statBar.getImageWidth() + value, statBar.getHeight());
    }

    public void resetValue()
    {
        statBar.setSize(originWidth, originHeight);
    }

    /**Found to be cause errors where the bar rendered will never go up but the amount will*/
    public void setToZero()
    {
        statBar.setSize(0, originHeight);
    }

    public float getAmount()
    {
        return statBar.getImageWidth();
    }
}
