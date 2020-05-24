package com.mygdx.core.mechanics.playermech;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.core.Core;

/**Rendering is done by passing to the stage in HUD. Editing for rendering purposes
 * @see com.mygdx.core.HUD*/
public final class Consumable
{
    final private Image item;
    private int amount = 0;
    private boolean click = false;

    private Label amountText;

    public Consumable(String file, Core core, Stage stage)
    {
        item = new Image((Texture) core.assetmanager.getFile(file));
        setListener();
        stage.addActor(item);

        amountText = new Label("" + amount, new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("IndieFlower.ttf"), Color.WHITE));
        amountText.setTouchable(Touchable.disabled);
        //amountText.setColor(Color.GOLD);
        stage.addActor(amountText);
    }

    public void setNewDrawable(String file, Core core)
    {
        item.setDrawable(new SpriteDrawable(new Sprite((Texture) core.assetmanager.getFile(file))));
    }

    public void setSize(float width, float height)
    {
        item.setSize(width, height);
    }

    public void setPosition(float x, float y, float textOffsetX, float textOffsetY)
    {
        item.setPosition(x, y);
        amountText.setPosition(x + textOffsetX, y + textOffsetY);
    }

    private void setListener()
    {
        item.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                if(amount > 0)
                {
                    amount--;
                    click = true;
                }
                else
                    click = false;

                amountText.setText("" + amount);
            }
        });
    }

    /**For unlimited sets of consumables, just set to 0 or less.
     * @param n - amount to be added. Negative input for subtraction.
     * @param limit - should be a positive integer above 0 if a limit should be placed.
     *                Nothing will be added if the n added causes the amount to be above
     *                the limit.*/
    public void addAmount(int n, int limit)
    {
        if(n + amount > limit && limit > 0)
            return;
        amount += n;

        amountText.setText("" + amount);

        System.out.println(n);
    }

    public void reset()
    {
        click = false;
    }

    public boolean getClick()
    {
        return click;
    }
}
