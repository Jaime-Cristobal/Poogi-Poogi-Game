package com.mygdx.core.Stages;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.core.Core;
import com.mygdx.core.EggCount;
import com.mygdx.core.GameStat;
import com.mygdx.core.LoadScreen;

import java.util.Random;

public class Intermission implements Screen
{
    final private Core core;
    private ExtendViewport viewport = new ExtendViewport(400, 600);
    private Image background;
    private Stage stage = new Stage(viewport);

    private Label finalScore, eggAmount, doorAmount, contText;
    private Button button;

    private Image splash;
    private float splashScale = 3f;

    private int mapStage = 0;

    private Image transition;
    private boolean startTransition = true;

    public Intermission(Core core, int mapStage)
    {
        this.core = core;
        Gdx.input.setInputProcessor(stage);
        this.mapStage = mapStage;

        EggCount.previousEggs = EggCount.eggAmount;
        EggCount.previousTotalEgg = EggCount.totalEggs;
        EggCount.previousDoors = EggCount.doorAmount;
        EggCount.maxDoors += 10;
        GameStat.treeGift = false;

        Random randomizer = new Random();
        Array<String> mapList = new Array<String>();
        mapList.add("menu_back1");
        mapList.add("menu_back2");
        mapList.add("menu_back3");
        mapList.add("menu_back4");
        mapList.add("menu_back5");
        background = new Image(core.assetmanager.getTextureFromAtlas("menu_back.atlas", mapList.get(randomizer.nextInt(mapList.size))));

        String font = "Chewy-Regular.ttf";
        eggAmount = new Label("Intermission\n\nEggs: " + EggCount.eggAmount, new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        eggAmount.setPosition(160, 350);

        doorAmount = new Label("Doors: " + EggCount.doorAmount, new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        doorAmount.setPosition(160, 300);

        finalScore = new Label("Score: 0", new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        finalScore.setPosition(160, 250);

        contText = new Label("Continue", new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        contText.setPosition(160, 100);
        contText.setTouchable(Touchable.disabled);

        button = new Button(core.bubbleSkin, "green");
        button.setTransform(true);
        button.setPosition(125, 79);
        button.setScale(0.40f);

        transition = new Image(core.assetmanager.getTextureFromAtlas("misc.atlas","transition"));
        transition.setPosition(-110, 0);
        transition.setScale(4f);
        transition.setVisible(false);

        stage.addActor(background);
        stage.addActor(eggAmount);
        stage.addActor(doorAmount);
        stage.addActor(finalScore);
        stage.addActor(button);
        stage.addActor(contText);
        stage.addActor(transition);

        splash = new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "splash"));
        splash.setPosition(-300, -300);
        splash.setScale(splashScale);
        splash.setVisible(true);
        stage.addActor(splash);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    public void show()
    {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if(startTransition)
        {
            if (splashScale >= 0.1)
                splashScale -= 0.05f;
            else
            {
                splash.setVisible(false);
                startTransition = false;
            }
            splash.setScale(splashScale);
        }


        if (splashScale <= 2.0f)
            splashScale += 0.03f;
        else
        {
            splash.setVisible(false);

            switch (mapStage)
            {
                case 1: //1st stage to 2nd stage
                    core.setScreen(new LoadScreen(core, 3));
                    dispose();
                    break;
                    /**
                case 2: //2nd stage to 3rd stage
                    core.setScreen(new LoadScreen(core, 3));
                    dispose();
                    break;*/
                case 3: //3rd stage to 4th stage
                    core.setScreen(new LoadScreen(core, 4));
                    dispose();
                    break;
                case 4: //3rd stage to 4th stage
                    core.setScreen(new LoadScreen(core, 1));
                    dispose();
                    break;
            }
        }
        splash.setScale(splashScale);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * @see ApplicationListener#resize(int, int)
     */
    public void resize(int width, int height)
    {
        viewport.update(width, height, false);
    }

    /**
     * @see ApplicationListener#pause()
     */
    public void pause()
    {

    }

    /**
     * @see ApplicationListener#resume()
     */
    public void resume()
    {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    public void hide()
    {

    }

    /**
     * Called when this screen should release all resources.
     */
    public void dispose() {
        stage.dispose();
    }
}
