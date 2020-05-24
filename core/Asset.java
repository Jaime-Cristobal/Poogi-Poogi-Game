package com.mygdx.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Created by FlapJack on 7/29/2017.
 */

public class Asset
{
    final public AssetManager manager;
    final private FileHandleResolver resolver = new InternalFileHandleResolver();
    final private String font = "Chewy-Regular.ttf";

    public boolean firstLoaded = false, thirdLoaded = false, fourthLoaded = false,
                    univAtlasLoaded = false;

    public Asset()
    {
        manager = new AssetManager();

        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, font, new FreetypeFontLoader(resolver));

        initialLoadfiles();
    }

    public Object getFile(String file)
    {
        return manager.get(file);
    }

    public Texture getTexture(String file)
    {
        return manager.get(file, Texture.class);
    }

    public TextureRegion getTextureFromAtlas(String atlas, String texture)
    {
        return manager.get(atlas, TextureAtlas.class).findRegion(texture);
    }

    private void initialLoadfiles()
    {
        manager.load("menu_back.atlas", TextureAtlas.class);
        manager.load("misc.atlas", TextureAtlas.class);

        manager.load("bubble/bubble.json", Skin.class);

        manager.load("Gunma-chanGambol.ogg", Music.class);
        manager.load("Rising.ogg", Music.class);

        manager.load("explodemini.ogg", Sound.class);
        manager.load("spawn.ogg", Sound.class);
        manager.load("egg_catch.ogg", Sound.class);
        manager.load("egg_bad.ogg", Sound.class);
        manager.load("disapear_sound.ogg", Sound.class);
        manager.load("entry_sound.ogg", Sound.class);
        manager.load("enemy_hit.ogg", Sound.class);
        manager.load("completetask_0.ogg", Sound.class);
        manager.load("pop.ogg", Sound.class);
        manager.load("beltHandle1.ogg", Sound.class);
        manager.load("pour_water.ogg", Sound.class);
        manager.load("metal-hammer-hit-01.ogg", Sound.class);
        manager.load("teleport.ogg", Sound.class);
        manager.load("laser4.ogg", Sound.class);
        manager.load("laser5.ogg", Sound.class);

        FreetypeFontLoader.FreeTypeFontLoaderParameter parms = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parms.fontFileName = font;    // path of .ttf file where that exist
        parms.fontParameters.size = 24;
        manager.load(font, BitmapFont.class, parms);

        manager.finishLoading();
    }

    public void loadMenu()
    {
        manager.load("menu_back.atlas", TextureAtlas.class);
    }

    public void unloadMenu()
    {
        manager.unload("menu_back.atlas");
    }

    public void load1stStage()
    {
        manager.load("1st stage/firstStage.atlas", TextureAtlas.class);
        manager.load("game_background_1.png", Texture.class);
        manager.load("game_background_2.png", Texture.class);
        manager.load("game_background_3.2.png", Texture.class);
        manager.load("game_background_4.png", Texture.class);

        manager.load("1st stage/Music/Children's March Theme.ogg", Music.class);
        manager.load("1st stage/Music/one_0.ogg", Music.class);
        manager.load("1st stage/Music/Runaround.ogg", Music.class);

        firstLoaded = true;
        univAtlasLoaded = true;     //TEMPORARY
    }

    public void unload1stStage()
    {
        manager.unload("1st stage/firstStage.atlas");
        manager.unload("game_background_1.png");
        manager.unload("game_background_2.png");
        manager.unload("game_background_3.2.png");
        manager.unload("game_background_4.png");

        manager.unload("1st stage/Music/Children's March Theme.ogg");
        manager.unload("1st stage/Music/one_0.ogg");
        manager.unload("1st stage/Music/Runaround.ogg");

        firstLoaded = false;
    }

    public void load2ndStage()
    {
        manager.load("platformer_background_1.png", Texture.class);
        manager.load("platformer_background_2.png", Texture.class);
    }

    public void unload2ndStage()
    {
        manager.unload("platformer_background_1.png");
        manager.unload("platformer_background_2.png");
    }

    public void load3rdStage()
    {
        manager.load("thirdStage/Music/Armadillo.ogg", Music.class);
        manager.load("thirdStage/Music/banana_track.ogg", Music.class);
        manager.load("thirdStage/Music/OsanpoRag.ogg", Music.class);

        manager.load("thirdStage/thirdStage.atlas", TextureAtlas.class);
        manager.load("desert_background1.png", Texture.class);
        manager.load("desert_background2.png", Texture.class);
        manager.load("desert_background3.png", Texture.class);
        manager.load("desert_background4.png", Texture.class);

        thirdLoaded = true;
    }

    public void unload3rdStage()
    {
        manager.unload("thirdStage/Music/Armadillo.ogg");
        manager.unload("thirdStage/Music/banana_track.ogg");
        manager.unload("thirdStage/Music/OsanpoRag.ogg");

        manager.unload("thirdStage/thirdStage.atlas");
        manager.unload("desert_background1.png");
        manager.unload("desert_background2.png");
        manager.unload("desert_background3.png");
        manager.unload("desert_background4.png");

        thirdLoaded = false;
    }

    public void load4thStage()
    {
        manager.load("fourthStage/Music/Happy town-02.ogg", Music.class);
        manager.load("fourthStage/Music/In Tranquil Spring.ogg", Music.class);
        manager.load("fourthStage/Music/spring day.ogg", Music.class);

        manager.load("fourthStage/fourthStage.atlas", TextureAtlas.class);
        manager.load("Cartoon_Forest_BG_01.png", Texture.class);
        manager.load("Cartoon_Forest_BG_02.png", Texture.class);
        manager.load("Cartoon_Forest_BG_03.png", Texture.class);
        manager.load("Cartoon_Forest_BG_04.png", Texture.class);

        fourthLoaded = true;
    }

    public void unload4thStage()
    {
        manager.unload("fourthStage/fourthStage.atlas");

        manager.unload("Cartoon_Forest_BG_01.png");
        manager.unload("Cartoon_Forest_BG_02.png");
        manager.unload("Cartoon_Forest_BG_03.png");
        manager.unload("Cartoon_Forest_BG_04.png");

        fourthLoaded = false;
    }

    public void loadGameAtlas()
    {
        manager.load("weather.atlas", TextureAtlas.class);
        manager.load("passages.atlas", TextureAtlas.class);
        manager.load("assets.atlas", TextureAtlas.class);
        manager.load("characters.atlas", TextureAtlas.class);
        manager.load("effects.atlas", TextureAtlas.class);

        univAtlasLoaded = true;
    }

    public void unloadGameAtlas()
    {
        manager.unload("assets.atlas");
        manager.unload("characters.atlas");
        manager.unload("effects.atlas");
        manager.unload("passages.atlas");

        univAtlasLoaded = false;
    }

    public void loadAll()
    {
        manager.load("assets.atlas", TextureAtlas.class);
        manager.load("characters.atlas", TextureAtlas.class);
        manager.load("effects.atlas", TextureAtlas.class);

        manager.load("platformer_background_1.png", Texture.class);
        manager.load("platformer_background_2.png", Texture.class);
        manager.load("game_background_1.png", Texture.class);
        manager.load("game_background_2.png", Texture.class);
        manager.load("game_background_3.2.png", Texture.class);
        manager.load("game_background_4.png", Texture.class);
        manager.load("Cartoon_Forest_BG_02.png", Texture.class);
        manager.load("Cartoon_Forest_BG_04.png", Texture.class);
        manager.load("desert_background1.png", Texture.class);
        manager.load("desert_background2.png", Texture.class);
        manager.load("desert_background3.png", Texture.class);
        manager.load("fairy_background1.png", Texture.class);
        manager.load("fairy_background2.png", Texture.class);
        manager.load("fairy_background3.png", Texture.class);
        manager.load("2_game_background.png", Texture.class);
    }

    public void unloadAll()
    {
        manager.unload("assets.atlas");
        manager.unload("characters.atlas");
        manager.unload("effects.atlas");

        manager.unload("platformer_background_1.png");
        manager.unload("platformer_background_2.png");
        manager.unload("game_background_1.png");
        manager.unload("game_background_2.png");
        manager.unload("game_background_3.2.png");
        manager.unload("game_background_4.png");
        manager.unload("Cartoon_Forest_BG_02.png");
        manager.unload("Cartoon_Forest_BG_04.png");
        manager.unload("desert_background1.png");
        manager.unload("desert_background2.png");
        manager.unload("desert_background3.png");
        manager.unload("fairy_background1.png");
        manager.unload("fairy_background2.png");
        manager.unload("fairy_background3.png");
        manager.unload("2_game_background.png");
    }

    public void Dispose()
    {
        manager.dispose();
    }
}