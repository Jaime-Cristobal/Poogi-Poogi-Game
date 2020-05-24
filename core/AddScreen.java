package com.mygdx.core;

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
import com.mygdx.core.actor.Egg;

import java.util.Random;

public class AddScreen implements Screen
{
    final private Core core;
    private ExtendViewport viewport = new ExtendViewport(400, 600);
    private Image background;
    private Stage stage = new Stage(viewport);

    private Label doorAmount, totalEgg, eggs, death, playAgain, toMenu;
    private Button play, menu;

    private Image splash;
    private boolean toPlay = true;
    private float splashScale = 1.5f;

    private boolean toMapStage = false, toMenuStage = false;
    final private int mapStage;

    public AddScreen(Core core, int mapStage)
    {
        this.core = core;
        Gdx.input.setInputProcessor(stage);
        this.mapStage = mapStage;

        if(EggCount.doorAmount > 10)
            EggCount.deaths++;

        Random randomizer = new Random();
        Array<String> mapList = new Array<String>();
        mapList.add("menu_back2");
        mapList.add("menu_back3");
        mapList.add("menu_back4");
        mapList.add("menu_back5");
        background = new Image(core.assetmanager.getTextureFromAtlas("menu_back.atlas", mapList.get(randomizer.nextInt(mapList.size))));

        String font = "Chewy-Regular.ttf";

        doorAmount = new Label("Doors: " + EggCount.doorAmount, new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        doorAmount.setPosition(160, 400);

        totalEgg = new Label("Total Eggs: " + EggCount.totalEggs, new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        totalEgg.setPosition(135, 350);

        eggs = new Label("Eggs: " + EggCount.eggAmount, new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        eggs.setPosition(160, 300);

        death = new Label("Losses: " + EggCount.deaths, new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        death.setPosition(160, 250);

        playAgain = new Label("Play Again", new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        playAgain.setPosition(55, 160);
        playAgain.setTouchable(Touchable.disabled);

        toMenu = new Label("Menu", new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile(font), Color.WHITE));
        toMenu.setPosition(255, 160);
        toMenu.setTouchable(Touchable.disabled);

        play = new Button(core.bubbleSkin, "green");
        play.setTransform(true);
        play.setPosition(30, 139);
        play.setScale(0.40f);

        menu = new Button(core.bubbleSkin, "red");
        menu.setTransform(true);
        menu.setPosition(205, 139);
        menu.setScale(0.40f);

        stage.addActor(background);
        stage.addActor(doorAmount);
        stage.addActor(totalEgg);
        stage.addActor(eggs);
        stage.addActor(death);
        stage.addActor(play);
        stage.addActor(menu);
        stage.addActor(playAgain);
        stage.addActor(toMenu);

        splash = new Image(core.assetmanager.getTextureFromAtlas("misc.atlas","splash"));
        splash.setPosition(-200, -200);
        splash.setScale(splashScale);
        stage.addActor(splash);
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        core.assetmanager.manager.update();
        if(!core.music.ifGummaPlay() && !core.music.musicOff)
            core.music.playGumma();
        play.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                splash.setVisible(true);
                toMapStage = true;
            }
        });
        menu.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                splash.setVisible(true);
                toMenuStage = true;
            }
        });
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if(toPlay)
        {
            if (splashScale >= 0)
                splashScale -= 0.03f;
            else
            {
                splash.setVisible(false);
                toPlay = false;
            }
            splash.setScale(splashScale);
        }
        else if(toMapStage)
        {
            if (splashScale <= 2.0f)
                splashScale += 0.03f;
            else
            {
                splash.setVisible(false);
                toMapStage = false;
                EggCount.eggAmount = EggCount.previousEggs;
                EggCount.totalEggs = EggCount.previousTotalEgg;
                EggCount.doorAmount = EggCount.previousDoors;
                core.setScreen(new LoadScreen(core, mapStage));
                dispose();
            }
            splash.setScale(splashScale);
        }
        else if(toMenuStage)
        {
            if (splashScale <= 2.0f)
                splashScale += 0.03f;
            else
            {
                splash.setVisible(false);
                toMenuStage = false;
                EggCount.doorAmount = 0;
                EggCount.eggAmount = 0;
                if(core.assetmanager.univAtlasLoaded)
                    core.assetmanager.unloadGameAtlas();
                core.setScreen(new StartMenu(core));
                dispose();
            }
            splash.setScale(splashScale);
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /** @see ApplicationListener#resize(int, int) */
    public void resize (int width, int height)
    {
        viewport.update(width, height, false);
    }

    /** @see ApplicationListener#pause() */
    public void pause ()
    {

    }

    /** @see ApplicationListener#resume() */
    public void resume ()
    {

    }

    /** Called when this screen is no longer the current screen for a {@link Game}. */
    public void hide ()
    {

    }

    /** Called when this screen should release all resources. */
    public void dispose ()
    {
        stage.dispose();
    }
}
