package com.mygdx.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Credits implements Screen
{
    final private Core core;
    final private ExtendViewport viewport = new ExtendViewport(400, 600);
    final private Stage stage = new Stage(viewport);
    final private Label creditLine, backText;
    final private Button back;

    public Credits(Core core)
    {
        this.core = core;

        String credits = "      Powered by LibGDX\n\n" +
                "Assets, music, and sound effects used in this product\n" +
                "are provided to the game development community\n" +
                "by the content creators themselves for\n" +
                "usage either under Public Domain (CC0) or\n" +
                "Attribution-by (CC-By x.0) Creative\n" +
                "Commons licenses.\n\n" +
                "                   Assets \n" +
                "----------------------------------------------\n\n"+
                "Glitch Assets by Tiny Speck\n https://www.glitchthegame.com/\n\n" +
                "\n" +
                "Backgrounds by CraftPix\n https://craftpix.net\n\n" +
                "\n" +
                "\"Smoke Aura\" by Beast\n https://opengameart.org/users/beast\n\n" +
                "\n" +
                "Explosion and Collision Effects by Sinestesia\n https://opengameart.org/users/sinestesia\n\n" +
                "\n" +
                "\"Lasers and beams\" by Rawdanitsu\n https://opengameart.org/users/rawdanitsu\n\n" +
                "\n" +
                "\"Free Game Items Pack #2\" Olga Bikmullina\n (http://ahninniah.graphics) \n" +
                " Licensed under Creative Commons:\n By Attribution 3.0 \n" +
                " http://creativecommons.org/licenses/by/3.0/\n\n" +
                "\n" +
                "\"Transparent Bubble\" by aloknarula\n https://opengameart.org/users/aloknarula\n\n" +
                "\n" +
                "\"Free Bubble Game Button Pack\" by pzUH\n https://opengameart.org/users/pzuh\n\n\n\n" +
                "\t     Font\n" +
                "----------------------------------------------\n\n" +
                "\"Chewy\" by Side Show\n https://fonts.google.com/specimen/Chewy\n\n" +
                "\n" +
                "\"Luckiest Guy\" by Astigmatic\n" +
                "\thttps://fonts.google.com/specimen\n/Luckiest+Guy?selection.family=Luckiest+Guy\n" +
                "\n" +
                " The fonts above are licensed under the \n" +
                " Apache License 2.0, Please see the license \n" +
                " section beforehand for more\n" +
                " information.\n\n\n\n" +
                "       Music\n" +
                "----------------------------------------------\n\n" +
                "\"Children's March Theme\" \n" +
                "Music by Cleyton Kauffman\n" +
                " - https://soundcloud.com/cleytonkauffman\n\n" +
                "\n" +
                "\"One\" by pheonton is licensed under CC-BY 3.0\n" +
                "https://opengameart.org/users/pheonton\n\n" +
                "\n" +
                "\t\"Runaround\"\n" +
                "\t\"Armadillo\"\n" +
                "\t\"Osanpo Rag\"\n" +
                "\t\"Gunma-chan Gambol\"\n" +
                "Music by Yubatake from OpenGameArt.org\n (OGA) CC-BY 4.0\n" +
                "https://opengameart.org/users/yubatake\n\n" +
                "\n" +
                "\"Banana Track\" by skrjablin\n" +
                " https://opengameart.org/users/skrjablin\n\n" +
                "\n" +
                "\"Home Hamlet\" by Scott Elliott\n\n" +
                "\n" +
                "\"In Tranquil Spring\" Composed\n" +
                " by Jonathan Shaw (www.jshaw.co.uk)\n\n" +
                "\n" +
                "\"Selections from BGM Fun Vol.7\" by syncopika\n" +
                " https://opengameart.org/users/syncopika\n\n" +
                "\n" +
                "\"Game Music Loop - Rising\" by HorrorPen\n" +
                " from OpenGameArt.org (OGA) CC-BY 4.0\n" +
                "https://opengameart.org/users/horrorpen\n\n\n\n" +
                "Sound Effects \n" +
                "----------------------------------------------\n\n" +
                "\"Cartoony Jump and Bounce\" by YoFrankie! (c) 2008, \n" +
                "Blender Foundation, www.blender.org\n\n" +
                "\n" +
                "\"Cute Cartoon Jump Sound Effect\" by\n" +
                "Boing Raw Copyright 2005 cfork \n" +
                "<http://freesound.org/people/cfork/>\n" +
                "Boing Jump Copyright 2012 Iwan Gabovitch \n" +
                "<http://qubodup.net>\n" +
                "\n" +
                "\"2 High Quality Explosions\" \n" +
                "by Michel Baradari and Iwan Gabovitch\n" +
                "from OpenGameArt.org (OGA) CC-BY 4.0\n" +
                "https://opengameart.org/users/qubodup\n" +
                "\n" +
                "\"30 weird CC0 SFX\" by rubberduck\n" +
                "https://opengameart.org/users/rubberduck\n" +
                "\n" +
                "\"50 RPG sound effects\" by Kenney\n" +
                "https://www.kenney.nl/\n" +
                "\n" +
                "\"Fantasy Sound Effects (Tinysized SFX)\" by Vehicle\n" +
                "http://www.janschupke.eu/\n" +
                "https://opengameart.org/users/vehicle\n" +
                "\n" +
                "\"Teleport Spell\" by Ogrebane\n" +
                "https://opengameart.org/users/ogrebane\n" +
                "\n" +
                "\"Completion sound.\" by Brandon Morris\n" +
                "https://www.youtube.com/brandon75689\n" +
                "https://opengameart.org/users/haeldb\n\n\n\n" +
                "Thank you to all the content creators for\n" +
                "providing these free-to-use assets, \n" +
                "fonts, music, and sound effects.\n";

        creditLine = new Label(credits, new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile("Chewy-Regular.ttf"), Color.WHITE));
        creditLine.setPosition(25, -3900);
        creditLine.setFontScale(0.7f);

        backText = new Label("Back", new Label.LabelStyle((BitmapFont)
                this.core.assetmanager.getFile("Chewy-Regular.ttf"), Color.WHITE));
        backText.setPosition(60, 529);
        backText.setTouchable(Touchable.disabled);

        back = new Button(core.bubbleSkin, "red");
        back.setTransform(true);
        back.setPosition(25, 515);
        back.setScale(0.30f);

        stage.addActor(creditLine);
        stage.addActor(back);
        stage.addActor(backText);

        Gdx.input.setInputProcessor(stage);
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        back.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                core.setScreen(new StartMenu(core));
                dispose();
            }
        });
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        creditLine.setY(creditLine.getY() + 0.5f);

        if(creditLine.getY() >= -50)
        {
            core.setScreen(new StartMenu(core));
            dispose();
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
