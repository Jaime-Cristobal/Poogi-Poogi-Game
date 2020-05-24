package com.mygdx.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.core.Stages.FirstStage;

public class LoadScreen implements Screen
{
    final private Core core;
    final private ExtendViewport viewport = new ExtendViewport(400, 600);
    final private Stage stage = new Stage(viewport);
    final private Array<Image> title = new Array<Image>();
    private boolean justStart = true;
    private float titleScale = 0.1f, adder = 0.1f;
    private int mapStage = 0;

    public LoadScreen(Core core, int mapStage)
    {
        this.core = core;

        title.add(new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "title_slant")));
        title.get(0).setPosition(215, 75);
        title.add(new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "title")));
        title.get(1).setPosition(280, 50);
        title.get(1).setScale(0.5f);
        for(int n = 0; n < title.size; n++)
            stage.addActor(title.get(n));

        this.mapStage = mapStage;
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        if(!core.assetmanager.univAtlasLoaded)
            core.assetmanager.loadGameAtlas();
        switch (mapStage)
        {
            case 1:
                core.assetmanager.load1stStage();
                break;
            case  2:
                core.assetmanager.load2ndStage();
                break;
            case 3:
                core.assetmanager.load3rdStage();
                break;
            case 4:
                core.assetmanager.load4thStage();
                break;
            default:
                break;
        }
    }

    private void toFirstStage()
    {
        core.setScreen(new FirstStage(core, 1));
        dispose();
    }

    private void toThirdStage()
    {
        core.setScreen(new FirstStage(core, 3));
        dispose();
    }

    private void toFourthStage()
    {
        core.setScreen(new FirstStage(core, 4));
        dispose();
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        if(core.assetmanager.manager.update((int)delta))
        {
            switch (mapStage)
            {
                case 1:
                    core.assetmanager.unloadMenu();
                    toFirstStage();
                    break;
                case 3:
                    toThirdStage();
                    break;
                case 4:
                    toFourthStage();
                    break;
                default:
                    core.setScreen(new StartMenu(core));
                    dispose();
                    break;
            }
        }

        Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if(justStart)
        {
            if (titleScale <= 0.35f)
                titleScale += adder;
            else
                justStart = false;

            title.get(0).setScale(titleScale);
            title.get(1).setScale(titleScale);
        }
        else
        {
            if (titleScale >= 0.35f)
                adder = -0.0005f;
            else if (titleScale <= 0.34f)
                adder = 0.0005f;
            titleScale += adder;
            title.get(0).setScale(titleScale);
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
