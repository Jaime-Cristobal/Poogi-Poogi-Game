package com.mygdx.core.Stages;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.core.Apparition;
import com.mygdx.core.Core;
import com.mygdx.core.EggCount;
import com.mygdx.core.EggPOD;
import com.mygdx.core.GameStat;
import com.mygdx.core.HUD;
import com.mygdx.core.Inventory;
import com.mygdx.core.MusicPOD;
import com.mygdx.core.actor.Clouds;
import com.mygdx.core.actor.EggTrader;
import com.mygdx.core.actor.FMapGenerator;
import com.mygdx.core.actor.Gems;
import com.mygdx.core.actor.Portal;
import com.mygdx.core.Scaler;
import com.mygdx.core.UnloadScreen;
import com.mygdx.core.actor.MagicalDoor;
import com.mygdx.core.actor.TreeItems;
import com.mygdx.core.actor.creation.CreateTexture;
import com.mygdx.core.actor.enemy.Crossers;
import com.mygdx.core.actor.enemy.MushroomBoss;
import com.mygdx.core.actor.enemy.Rubble;
import com.mygdx.core.actor.enemy.SpecialEnemy;
import com.mygdx.core.actor.enemy.SpiritOrbs;
import com.mygdx.core.actor.enemy.TikiBomb;
import com.mygdx.core.actor.enemy.TikiBombManager;
import com.mygdx.core.actor.enemy.TikiBoss;
import com.mygdx.core.actor.engineer.Laser;
import com.mygdx.core.animator.Animator;
import com.mygdx.core.collision.EventManager;
import com.mygdx.core.collision.FilterID;
import com.mygdx.core.collision.ObjectID;
import com.mygdx.core.mechanics.ActionButton;
import com.mygdx.core.mechanics.GrowableTree;
import com.mygdx.core.mechanics.playermech.Engineer;
import com.mygdx.core.player.Player;

import java.util.Random;

/**Test World*/

public final class FirstStage implements Screen
{
    final private float width = 400;
    final private float height = 600;
    final private Core core;
    final private World world = new World(new Vector2(0, -50f), true);
    final private OrthographicCamera worldCamera = new OrthographicCamera(width, height);
    final private ExtendViewport viewport = new ExtendViewport(width, height, worldCamera);
    final private FillViewport changeView = new FillViewport(width, height, worldCamera);
    final private InputMultiplexer inputGroup = new InputMultiplexer();      //for multiple input processors needed
    final private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private Matrix4 debugMatrix;
    final private Player player;
    final private HUD hud;
    final private float lerp  = 0.1f;
    private Vector3 camPosition;
    final private Engineer eng;
    final private ActionButton engIcon;
    final private ActionButton laserIcon;
    final private Laser laser;
    private MagicalDoor door;
    private CreateTexture doorLand, startPad, portalLand;
    private boolean reset = false, doorCollide = false, landGenerate = false,
                        enemyHit = false, explode = false, lose = false;
    private Animator hitFx;
    final private Stage background = new Stage(viewport);
    final private Array<String> maps = new Array<String>();
    final private Random mapRandomizer = new Random();
    final private Array<Image> mapDisplay = new Array<Image>();
    private int indexMap = 0;
    private boolean nextScreen = false, start = false, toIntermission = false, interScreen = false;
    final private Stage stageTransition = new Stage(changeView);
    private Image splash;
    private boolean toPlay = true;
    private float splashScale = 2.0f;
    private boolean menuSplash = false, toMenu = false;
    private boolean mapTransition = false, startTransition = false;
    final private Array<Image> title = new Array<Image>();
    private float titleScale = 0.37f;
    private FMapGenerator generator;
    //0 -> find the door, 1 -> find hidden portal, 2 -> grow tree
    private int chosenCondition = -1;
    private Portal portal;
    private boolean portalOn = false, portalCollide = false;
    final private Array<Image> conditionSet = new Array<Image>();
    private float conditionScale = 0;
    private Apparition apparition;
    private Animator explosion;
    private float explodeX = -700, explodeY = -700;
    private boolean nonGeneratedCol = false;
    private int randomChance = 0;
    private boolean updater = true;
    private Animator smoke;
    private boolean ifSmoke = false;
    private float smokeX = 0, smokeY = 0;
    final private Array<CreateTexture> block = new Array<CreateTexture>();
    private GrowableTree tree;
    final private ArrayMap<String, Float> treeRegion = new ArrayMap<String, Float>();
    final private TreeItems irrigator, irrigatorDrop;
    final private Array<SpecialEnemy> hunter = new Array<SpecialEnemy>();
    private int specItemRoll = 0;
    final private Gems ruby, moonstone;
    private int outTimer = 0, second = 0;
    final private CreateTexture shield;
    private boolean shieldOn = false, doubleEggsOn = false, hyperSpeed = false;
    private int butterflyChance = 0;
    private Animator doubleQuo;
    final private int maxState = 4;
    private int maxDoors = 0;
    final private Array<Label> minePrompt = new Array<Label>();
    final private Array<Button> mineButton = new Array<Button>();
    final private Array<Label> mineLabel = new Array<Label>();
    final private Label giftLabel;

    private Animator windChill;
    private int windX = 0, windY = 0;
    private Animator windWave;
    private int waveX = 0, waveY = 0;
    private Animator dust;
    private float xStorm = -250, yStorm = 40;
    private boolean apparitionSpawn = false;
    private boolean stopBosses = false;

    private float camOffX = 0, camOffY = 0;

    private int currentLevel = 0;
    private String startingText, doorLandText, portalLandText;

    private Array<TikiBoss> tiki = new Array<TikiBoss>();
    private boolean tikiOn = false, tikiDefeated = false;
    private int amntDesetTiki = 0;
    private Label doorWarning;

    private Rubble rubbles = new Rubble(BodyDef.BodyType.DynamicBody, false);
    private Array<Crossers> crossers = new Array<Crossers>();
    private Rubble fireStorm = new Rubble(BodyDef.BodyType.KinematicBody, true);
    private boolean fireStormGen = false, spiritHit = false, rubbleSpawn = false;
    private SpiritOrbs orbs;
    private int amountOrbs = 0;

    private MushroomBoss mushroomBoss = new MushroomBoss();
    private boolean mushroomOn = false, mushroomDefeat = false;
    private TikiBombManager tikiBomb;

    private boolean turnCrow = false;
    private float crowCounter = 0;
    private Animator rookHead;

    private boolean giftTree = true;
    private float giftCounter = 0;

    private int maxEggs = 50;

    private ImageButton musicButton;

    public FirstStage(Core core, int currentLevel)
    {
        this.currentLevel = currentLevel;
        this.core = core;
        switch (this.currentLevel)
        {
            case 1:
                this.core.music.createFirstStage(core.assetmanager);
                startingText = "platform3"; doorLandText = "platform3"; portalLandText = "platform3";
                break;
            case 3:
                this.core.music.createThirdStage(core.assetmanager);
                startingText = "dirt4"; doorLandText = "dirt5"; portalLandText = "dirt4";
                break;
            case 4:
                this.core.music.createFourthStage(core.assetmanager);
                startingText = "color_platform1"; doorLandText = "color_platform1"; portalLandText = "color_platform1";
                break;
        }

        player = new Player("characters.atlas", core);
        player.createBody(world, 200, 55, 30, 30, 102, 86);
        player.setActive(false);
        player.setPause(true);
        player.setRegion("entry");
        hud = new HUD(core);

        musicButton = new ImageButton(core.bubbleSkin, "musicOption");
        musicButton.setTransform(true);
        musicButton.setPosition(-20, -140);
        musicButton.setScale(0.25f);
        musicButton.setVisible(false);
        musicButton.setChecked(core.music.musicOff);
        hud.addActor(musicButton);

        irrigator = new TreeItems("assets.atlas", "irrigatorStill", this.core, world,
                "irrigator", BodyDef.BodyType.DynamicBody);
        irrigatorDrop = new TreeItems("assets.atlas", "irrigatorStill", this.core, world,
                "irrigatorDrop", BodyDef.BodyType.KinematicBody);
        ruby = new Gems("assets.atlas", "ruby", this.core, world, 24, 27,
                "rubyH", BodyDef.BodyType.KinematicBody);
        moonstone = new Gems("assets.atlas", "moonstone", this.core, world, 44, 41,
                "moonstoneH", BodyDef.BodyType.KinematicBody);

        inputGroup.addProcessor(hud.getStage());
        inputGroup.addProcessor(player.getGesture());
        inputGroup.addProcessor(player.getInputProcessor());
        Gdx.input.setInputProcessor(inputGroup);

        eng = new Engineer(core, world);
        engIcon = new ActionButton(core, "red_small", 115, -245, 0.4f);
        engIcon.setMaxRate(0.5f);
        engIcon.setAction(core, eng, 600);
        engIcon.setTracker(core, 155, -215);
        hud.addActor(engIcon.getButton());
        hud.addActor(engIcon.getAmountLabel());

        laser = new Laser("redBeam", core, world, 10, true);
        laserIcon = new ActionButton(core, "yellow_small", 85, -280, 0.3f);
        laserIcon.setMaxRate(0.3f);
        laserIcon.setAction(core, laser, 600, 10);
        //laserIcon.setTracker(core, 109, -260);
        laserIcon.setTimerOn(false);
        hud.addActor(laserIcon.getButton());
        //hud.addActor(laserIcon.getAmountLabel());

        mineTraderConfig();
        generateWeather();
        setStageMap();
        createBosses();
        createFx();
        createBlockers();
        createStageActors();
        createUniqueLand();
        createGenerator();
        generateConditions();
        generateStartPad();
        createTree();
        createTiki();

        switch (currentLevel)
        {
            case 1:
                rubbles.addTexture("assets.atlas", "rubble1", this.core, world, 2,
                        15, 10, 41, 27, false);
                rubbles.addTexture("assets.atlas", "rubble2", this.core, world, 2,
                        15, 10, 41, 29, false);
                rubbles.enableRespawn(-100, -80, -10, 50);

                //FOR MUSHROOM BOSS
                mushroomBoss.createMushroom("1st stage/firstStage.atlas", "ground_mushroom", 2.5f,
                        this.core, world, 55, 60, 114, 135, "ground_mushroom");
                mushroomBoss.createLand("assets.atlas", "cave2", this.core, world, 101, 20,
                        132, 66, "cave2");
                mushroomBoss.setLimit(55, block.get(2).getX(), block.get(3).getX(), block.get(1).getY(), 3f);

                tikiBomb = new TikiBombManager(this.core, world, 25);
                break;
            case 3:
                rubbles.addTexture("assets.atlas", "rubble4", this.core, world, 4,
                        60, 30, 70, 48, false);
                rubbles.addTexture("assets.atlas", "rubble3", this.core, world, 3,
                        50, 21, 61, 40, false);
                rubbles.enableRespawn(-100, -80, -10, 50);
                rubbles.spawn(-90, MathUtils.random(-10, 50), 15, 0, 150, -100, 1.5f);
                rubbleSpawn = true;

                mushroomOn = true;
                mushroomBoss.createMushroom("thirdStage/thirdStage.atlas", "orangeMushroom", 2.5f,
                        this.core, world, 55, 60, 114, 135, "orangeMushroom");
                mushroomBoss.createLand("assets.atlas", "rock4", this.core, world, 101, 20,
                        132, 66, "rock4");
                mushroomBoss.spawn(MathUtils.random(-30, 80), MathUtils.random(0, 35),
                        5 * MathUtils.randomSign(), 5 * MathUtils.randomSign());
                mushroomBoss.setLimit(55, block.get(2).getX(), block.get(3).getX(), block.get(1).getY(), 3f);

                orbs = new SpiritOrbs("thirdStage/thirdStage.atlas", "rookHalo", 1.5f, 25,
                        this.core, world, 40, 40, 62, 47);
                break;
            case 4:
                //FOR CROSSERS
                for(int n = 0; n < 7; n++)
                {
                    crossers.add(new Crossers("fourthStage/fourthStage.atlas", "butterfly", 1.5f,
                            this.core, world, 20, 15, 70, 65, "butterfly" + n));
                    crossers.get(n).setOffset(10, 10);
                    crossers.get(n).setNoGravity();
                    int minSpeed = 7, maxSpeed = 30;
                    switch (n)
                    {
                        case 0:
                            crossers.get(n).spawn(-50, 52, MathUtils.random(minSpeed, maxSpeed), false);
                            break;
                        case 1:
                            crossers.get(n).spawn(125, 42, MathUtils.random(minSpeed, maxSpeed), true);
                            break;
                        case 2:
                            crossers.get(n).spawn(-50, 35, MathUtils.random(minSpeed, maxSpeed), false);
                            break;
                        case 3:
                            crossers.get(n).spawn(125, 25, MathUtils.random(minSpeed, maxSpeed), true);
                            break;
                        case 4:
                            crossers.get(n).spawn(-50, 15, MathUtils.random(minSpeed, maxSpeed), false);
                            break;
                        case 5:
                            crossers.get(n).spawn(125, 10, MathUtils.random(minSpeed, maxSpeed), true);
                            break;
                        case 6:
                            crossers.get(n).spawn(-50, 5, MathUtils.random(minSpeed, maxSpeed), false);
                            break;
                    }
                }

                //FOR MUSHROOM BOSS
                mushroomBoss.createMushroom("fourthStage/fourthStage.atlas", "purpleMushroom", 2.5f,
                        this.core, world, 55, 60, 114, 135, "purpleMushroom");
                mushroomBoss.createLand("assets.atlas", "cave2", this.core, world, 101, 20,
                        132, 66, "cave2");
                mushroomBoss.setLimit(55, block.get(2).getX(), block.get(3).getX(), block.get(1).getY(), 3f);
                mushroomBoss.spawn(MathUtils.random(-30, 80), MathUtils.random(0, 35),
                        5 * MathUtils.randomSign(), 5 * MathUtils.randomSign());
                mushroomOn = true;

                orbs = new SpiritOrbs("fourthStage/fourthStage.atlas", "spiritOrb", 2.5f, 25,
                        this.core, world, 40, 40, 40, 40);
                orbs.setSpeed(25);
                orbs.setRange(30);
                break;
        }

        fireStorm.addAnimation("assets.atlas", "meteor", 2f, this.core, world, 4,
                20, 20, 58, 100, false);
        fireStorm.setOffset(0, 25);
        fireStorm.enableRespawn(-40, 90, 100, 110);

        shield = new CreateTexture(this.core.assetmanager.getTextureFromAtlas("assets.atlas", "BubbleSimple"),
                this.core, BodyDef.BodyType.DynamicBody);
        shield.setData(0,0,  true);
        shield.setFilter(FilterID.trash_category, (short)(FilterID.enemy_category | FilterID.egg_category | FilterID.floor_category));
        shield.setUniqueID("BubbleSimple");
        shield.create(world, -700, -700, 84, 84, false);
        shield.setActive(false);
        shield.setDisplay(false);
        ObjectID.shieldID = shield.getBody().getUserData();

        if(!this.core.music.ifHorrorPlay() && !core.music.musicOff)
        {
            switch (this.currentLevel)
            {
                case 1:
                    this.core.music.playFirstStageRandom();
                    break;
                case 3:
                    this.core.music.playThirdStageRandom();
                    break;
                case 4:
                    this.core.music.playFourthStageRandom();
            }
        }

        maxDoors = EggCount.maxDoors;

        doorWarning = new Label("",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                Color.WHITE));
        doorWarning.setTouchable(Touchable.disabled);
        doorWarning.setPosition(-110, 60);
        doorWarning.setVisible(false);
        hud.addActor(doorWarning);

        giftLabel = new Label("You have unlocked\n a new item!\n Check the tree\n storage.",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                        Color.WHITE));
        giftLabel.setPosition(-85, -150);
        giftLabel.setVisible(false);
        hud.addActor(giftLabel);

        chanceGenerator();
    }

    public void setMaps(String... fileName)
    {
        for(int n = 0; n < fileName.length; n++)
            mapDisplay.add(new Image(core.assetmanager.getTexture(fileName[n])));
    }

    private void setStageMap()
    {
        switch (currentLevel)
        {
            case 1:
                mapDisplay.add(new Image(core.assetmanager.getTexture("game_background_1.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("game_background_2.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("game_background_3.2.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("game_background_4.png")));
                break;
            case 3:
                mapDisplay.add(new Image(core.assetmanager.getTexture("desert_background1.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("desert_background2.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("desert_background3.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("desert_background4.png")));
                break;
            case 4:
                mapDisplay.add(new Image(core.assetmanager.getTexture("Cartoon_Forest_BG_01.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("Cartoon_Forest_BG_02.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("Cartoon_Forest_BG_03.png")));
                mapDisplay.add(new Image(core.assetmanager.getTexture("Cartoon_Forest_BG_04.png")));
                break;
        }

        for(int n = 0; n < mapDisplay.size; n++)
        {
            mapDisplay.get(n).setPosition(-600, -200);
            mapDisplay.get(n).setVisible(false);
            background.addActor(mapDisplay.get(n));
        }

        randomMap();
    }

    private void createBosses()
    {
        apparition = new Apparition(core, world);
        int amountOfHunters = 0;
        switch (currentLevel)
        {
            case 1:
                amountOfHunters = 3;
                break;
            case 3:
                amountOfHunters = 5;
                break;
            case 4:
                 amountOfHunters = 3;
                 break;
        }
        for(int n = 0; n < amountOfHunters; n++)
            hunter.add(new SpecialEnemy(this.core, world, "hunter1", "hunter1" + n));
    }

    private void createBlockers()
    {
        //top
        block.add(new CreateTexture(this.core.assetmanager.getTextureFromAtlas("assets.atlas", "board"),
                this.core, BodyDef.BodyType.StaticBody));
        block.get(0).setData(0.9f,0,  true);
        block.get(0).setFilter(FilterID.platform_category, (short)(FilterID.player_category | FilterID.enemy_category
                | FilterID.egg_category | FilterID.firing_category));
        block.get(0).setUniqueID("block0");
        block.get(0).create(world, -570, 825, 1900, 64, false);
        block.get(0).setActive(true);
        block.get(0).setDisplay(true);

        //bottom
        block.add(new CreateTexture(this.core.assetmanager.getTextureFromAtlas("assets.atlas", "board"),
                this.core, BodyDef.BodyType.StaticBody));
        block.get(1).setData(0.9f,0,  true);
        block.get(1).setFilter(FilterID.platform_category, (short)(FilterID.player_category | FilterID.enemy_category
                | FilterID.egg_category));
        block.get(1).setUniqueID("block1");
        block.get(1).create(world, -570, -210, 1900, 64, false);
        block.get(1).setActive(true);
        block.get(1).setDisplay(true);

        //left
        block.add(new CreateTexture(this.core.assetmanager.getTextureFromAtlas("assets.atlas", "board1"),
                this.core, BodyDef.BodyType.StaticBody));
        block.get(2).setData(0.9f,0,  false);
        block.get(2).setFilter(FilterID.platform_category, (short)(FilterID.player_category | FilterID.enemy_category
                | FilterID.egg_category));
        block.get(2).setUniqueID("block2");
        block.get(2).create(world, -610, -210, 64, 1100, false);
        block.get(2).setActive(true);
        block.get(2).setDisplay(true);

        //right
        block.add(new CreateTexture(this.core.assetmanager.getTextureFromAtlas("assets.atlas", "board1"),
                this.core, BodyDef.BodyType.StaticBody));
        block.get(3).setData(0.9f,0,  false);
        block.get(3).setFilter(FilterID.platform_category, (short)(FilterID.player_category | FilterID.enemy_category
                | FilterID.egg_category));
        block.get(3).setUniqueID("block3");
        block.get(3).create(world, 1300, -210, 64, 1100, false);
        block.get(3).setActive(true);
        block.get(3).setDisplay(true);
    }

    private void createTiki()
    {
        switch (currentLevel)
        {
            case 1:
                for (int n = 0; n < 2; n++)
                {
                    tiki.add(new TikiBoss("1st stage/firstStage.atlas", "tikiWar", core, world,
                            "tiki" + n, 35, 35, 45, 72));
                    if (n == 1) {
                        tiki.get(n).setMaxRate(0.65f);
                        tiki.get(n).setMaxRateFinder(0.7f);
                    }
                }
                break;
            case 3:
                for (int n = 0; n < 3; n++)
                {
                    tiki.add(new TikiBoss("thirdStage/thirdStage.atlas", "tikiWar2", core, world,
                            "tiki" + n, 35, 35, 45, 72));
                    tiki.get(n).setHealth(3);
                    tiki.get(n).setSpeed(10, 25);
                    switch (n)
                    {
                        case 1:
                            tiki.get(n).setMaxRate(0.65f);
                            tiki.get(n).setMaxRateFinder(0.7f);
                            break;
                        case 2:
                            tiki.get(n).setMaxRate(0.7f);
                            tiki.get(n).setMaxRateFinder(0.65f);
                            break;
                        case 3:
                            tiki.get(n).setMaxRate(0.75f);
                            tiki.get(n).setMaxRateFinder(0.6f);
                            break;
                    }
                }
                break;
        }
    }

    private void createFx()
    {
        explosion = new Animator("effects.atlas", core);
        explosion.setScale(256, 256);
        explosion.addRegion("explosionH", 1.0f);

        smoke = new Animator("effects.atlas", core);
        smoke.addRegion("smoke_fade", 3f);

        hitFx = new Animator("effects.atlas", core);
        hitFx.addRegion("hit_yellow", 1.3f);
        hitFx.addRegion("hit_red", 1.3f);
        hitFx.addRegion("charge_fx", 1.3f);

        doubleQuo = new Animator("effects.atlas", core);
        doubleQuo.addRegion("double", 1.3f);
        doubleQuo.addRegion("doubleSpeed", 1.3f);

        rookHead = new Animator("characters.atlas", core);
        rookHead.addRegion("rook_head", 2.0f);
        rookHead.setScale(108, 127);
    }

    private void createStageActors()
    {
        splash = new Image(core.assetmanager.getTextureFromAtlas("misc.atlas","splash"));
        splash.setPosition(-500, -550);
        splash.setScale(splashScale);
        splash.setVisible(false);
        hud.addActor(splash);

        title.add(new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "title_slant")));
        title.get(0).setPosition(18, -225);
        title.get(0).setScale(titleScale);
        title.add(new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "title")));
        title.get(1).setPosition(88, -245);
        title.get(1).setScale(titleScale);
        for(int n = 0; n < title.size; n++)
            hud.addActor(title.get(n));
    }

    private void createUniqueLand()
    {
        door = new MagicalDoor(core, world,  "MagicalDoor1");
        portal = new Portal(core, world,  "Portal1");

        doorLand = new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", doorLandText), core,
                BodyDef.BodyType.StaticBody);
        doorLand.setData(0.9f, 0, true);
        doorLand.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category |
                FilterID.item_category | FilterID.collector_category));

        portalLand = new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", portalLandText), core,
                BodyDef.BodyType.StaticBody);
        portalLand.setData(0.9f, 0, true);
        portalLand.setUniqueID("Portalland");
        portalLand.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category |
                FilterID.boss_category | FilterID.item_category | FilterID.collector_category));

        startPad = new CreateTexture(core.assetmanager.getTextureFromAtlas("assets.atlas", startingText), core,
                BodyDef.BodyType.StaticBody);
        startPad.setData(0.9f, 0, true);
        startPad.setUniqueID("startPad");
        startPad.setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.enemy_category |
                FilterID.potion_category | FilterID.mine_catageory | FilterID.egg_category | FilterID.door_category |
                FilterID.boss_category | FilterID.item_category | FilterID.collector_category));


        switch (currentLevel)
        {
            case 1:
            case 3:
                doorLand.createOriginalCustom(world, -700, -700,100, 15, 125, 60, false);
                portalLand.createOriginalCustom(world, -700, -700,100, 15, 125, 60, false);
                startPad.createOriginalCustom(world, -300, -300,100, 15, 125, 60, false);
                break;
            case 4:
                doorLand.createOriginalCustom(world, -700, -700,100, 15, 117, 95, false);
                portalLand.createOriginalCustom(world, -700, -700,100, 15, 117, 95, false);
                startPad.createOriginalCustom(world, -300, -300,100, 15, 117, 95, false);
                break;
        }

        doorLand.setActive(false);
        portalLand.setActive(false);
        startPad.setActive(true);
    }

    private void createTree()
    {
        treeRegion.put("treeLife1", 1f);
        treeRegion.put("treeLife2", 1f);
        treeRegion.put("treeLife3", 1f);
        treeRegion.put("treeLife4", 1f);
        treeRegion.put("treeLife5", 1f);
        treeRegion.put("treeLife6", 1f);
        treeRegion.put("treeLife7", 1f);
        treeRegion.put("treeLife8", 1f);
        treeRegion.put("treeLife9", 1f);
        treeRegion.put("treeLife10", 1f);
        tree = new GrowableTree("assets.atlas", "treeLife", this.core, world);
        tree.createLand(this.core, world, "assets.atlas", "platform_yellow1",
                100, 20, 128, 64);;
        tree.spawn();
    }

    private void generateConditions()
    {
        //0 -> find door, 1 -> find hidden portal, 2 -> grow the tree
        chosenCondition = MathUtils.random(0, 1);
        conditionSet.add(new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "findDoor")));
        conditionSet.add(new Image(core.assetmanager.getTextureFromAtlas("misc.atlas", "hiddenPortal")));
        switch (chosenCondition)
        {
            case 0:
                conditionScale = 0.5f;
                conditionSet.get(0).setPosition(-100, -75);
                conditionSet.get(0).setScale(conditionScale);
                generateDoor();
                break;
            case 1:
                conditionScale = 0.6f;
                conditionSet.get(1).setPosition(-150, -75);
                conditionSet.get(1).setScale(conditionScale);
                generatePortal();
                break;
        }

        for(int n = 0; n < conditionSet.size; n++)
        {
            if(chosenCondition != n)
                conditionSet.get(n).setVisible(false);
            hud.addActor(conditionSet.get(n));
        }
    }

    private void createGenerator()
    {
        generator = new FMapGenerator(this.core, world);
        switch (currentLevel)
        {
            case 1:
                generator.generateLand(3, "assets.atlas", "platform3",
                        100, 27, 117, 55);
                generator.generateLand(3, "assets.atlas", "platform4",
                        100, 27, 117, 55);
                generator.generateEgg(5,"chicken_egg", EggPOD.CHICKEN_EGG, 31, 46);
                generator.generateEgg(3,"egg_plain", EggPOD.EGG_PLAIN, 27, 34);
                generator.generateEgg(3,"egghunt_egg1", EggPOD.EGGHUNT_EGG1, 24, 19);
                generator.generateEgg(3,"egghunt_egg2", EggPOD.EGGHUNT_EGG2, 24, 19);
                generator.generateEgg(1,"egghunt_egg3", EggPOD.EGGHUNT_EGG3, 30, 24);
                generator.generateEgg(1,"egghunt_egg4", EggPOD.EGGHUNT_EGG4, 31, 28);
                generator.generateEgg(1,"butterfly_egg", EggPOD.BUTTERFLY_EGG ,21, 27);

                generator.generateEnemies(14, "1st stage/firstStage.atlas", "batterfly", 2f,
                        35, 25, 111, 96, true, 1, BodyDef.BodyType.KinematicBody,
                        0, -1, false);
                generator.generateEnemies(4, "1st stage/firstStage.atlas", "pet_rock", 2f,
                        32, 40, 67, 89, false, 2, BodyDef.BodyType.DynamicBody,
                        0, -8, true);
                generator.setMaxEnemies(12);
                break;
            case 3:
                generator.generateLand(3, "assets.atlas", "dirt3",
                        105, 40, 108, 80);
                generator.generateLand(3, "assets.atlas", "dirt4",
                        115, 39, 128, 67);
                generator.generateLand(3, "assets.atlas", "dirt5",
                        115, 30, 122, 67);
                generator.generateEgg(5,"chicken_egg", EggPOD.CHICKEN_EGG, 31, 46);
                generator.generateEgg(3,"egg_plain", EggPOD.EGG_PLAIN, 27, 34);
                generator.generateEgg(3,"egghunt_egg1", EggPOD.EGGHUNT_EGG1, 24, 19);
                generator.generateEgg(3,"egghunt_egg2", EggPOD.EGGHUNT_EGG2, 24, 19);
                generator.generateEgg(1,"egghunt_egg3", EggPOD.EGGHUNT_EGG3, 30, 24);
                generator.generateEgg(1,"egghunt_egg4", EggPOD.EGGHUNT_EGG4, 31, 28);
                generator.generateEgg(5,"butterfly_egg", EggPOD.BUTTERFLY_EGG ,21, 27);
                generator.generateEnemies(4, "thirdStage/thirdStage.atlas", "cactus", 2f,
                        25, 40, 57, 46, false, 2, BodyDef.BodyType.DynamicBody,
                        0, 5, true);
                generator.generateEnemies(4, "thirdStage/thirdStage.atlas", "horseman", 2f,
                        35, 70, 75, 89, false, 2, BodyDef.BodyType.DynamicBody,
                        0, 8, true);
                generator.setMaxEnemies(8);
                break;
            case 4:
                generator.generateLand(3, "assets.atlas", "color_platform1",
                        100, 18, 117, 95);
                generator.generateLand(3, "assets.atlas", "color_platform2",
                        100, 18, 117, 95);
                generator.generateLand(3, "assets.atlas", "color_platform3",
                        100, 18, 117, 95);
                generator.generateEgg(10,"chicken_egg", EggPOD.CHICKEN_EGG, 31, 46);
                generator.generateEgg(10,"egg_plain", EggPOD.EGG_PLAIN, 27, 34);
                generator.generateEgg(4,"egghunt_egg1", EggPOD.EGGHUNT_EGG1, 24, 19);
                generator.generateEgg(4,"egghunt_egg2", EggPOD.EGGHUNT_EGG2, 24, 19);
                generator.generateEgg(3,"egghunt_egg3", EggPOD.EGGHUNT_EGG3, 30, 24);
                generator.generateEgg(1,"egghunt_egg4", EggPOD.EGGHUNT_EGG4, 31, 28);
                generator.generateEgg(1,"butterfly_egg", EggPOD.BUTTERFLY_EGG ,21, 27);
                generator.generateEnemies(12, "fourthStage/fourthStage.atlas", "flamingo_fly", 2f,
                        30, 15, 111, 96, true, 1, BodyDef.BodyType.KinematicBody, false);
                generator.generateEnemiesLoopBack(8, "fourthStage/fourthStage.atlas", "frog", 2f,
                        40, 60, 108, 94, true, 1, BodyDef.BodyType.KinematicBody, false);
                generator.generateEnemies(8, "fourthStage/fourthStage.atlas", "scarecrow", 2f,
                        15, 80, 89, 99, false, 2, BodyDef.BodyType.DynamicBody,
                        0, 8, true);
                break;
        }
        generator.generateMap();
    }

    private void generateWeather()
    {
        switch (currentLevel)
        {
            case 1:
                windChill = new Animator("weather.atlas", core);
                windChill.setScale(256, 60);
                windChill.addRegion("WindCurl", 1.9f);
                windX = MathUtils.random(-40, 100);
                windY = MathUtils.random(0, 50);

                windWave = new Animator("weather.atlas", core);
                windWave.setScale(256, 128);
                windWave.addRegion("WindWave", 2.5f);
                waveX = MathUtils.random(-40, 100);
                waveY = MathUtils.random(0, 50);
                break;
            case 3:
                dust = new Animator("weather.atlas", core);
                dust.setScale(512, 512);
                dust.addRegion("smokeBrown", 2f);
                break;
        }
    }

    private void mineTraderConfig()
    {
        for(int n = 0; n < 4; n++)
        {
            mineButton.add(new Button(core.bubbleSkin, "yellow"));
            mineButton.get(n).setTransform(true);
            mineButton.get(n).setScale(0.3f);
            switch (n)
            {
                case 0:
                    mineButton.get(n).setPosition(50, 25);
                    break;
                case 1:
                    mineButton.get(n).setPosition(50, 75);
                    break;
                case 2:
                    mineButton.get(n).setPosition(50, 125);
                    break;
                case 3:
                    mineButton.get(n).setPosition(50, -25);
                    break;
            }
            mineButton.get(n).setVisible(false);
            hud.addActor(mineButton.get(n));
        }

        for(int n = 0; n < 4; n++)
        {
            mineLabel.add(new Label("Buy", new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                    Color.BLACK)));
            mineLabel.get(0).setTouchable(Touchable.disabled);
            switch (n)
            {
                case 0:
                    mineLabel.get(n).setPosition(80, 35);
                    break;
                case 1:
                    mineLabel.get(n).setPosition(80, 85);
                    break;
                case 2:
                    mineLabel.get(n).setPosition(80, 135);
                    break;
                case 3:
                    mineLabel.get(n).setPosition(80, -15);
            }
            mineLabel.get(n).setVisible(false);
            mineLabel.get(n).setTouchable(Touchable.disabled);
            hud.addActor(mineLabel.get(n));
        }
        mineLabel.get(2).setText("Use");

        if(GameStat.pumpkinCat)
            mineLabel.get(0).setText("Use");
        if(GameStat.pumpkinDemon)
            mineLabel.get(1).setText("Use");

        minePrompt.add(new Label("These are my wares.",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                        Color.WHITE)));
        minePrompt.get(0).setTouchable(Touchable.disabled);
        minePrompt.get(0).setPosition(-100, 190);
        minePrompt.get(0).setVisible(false);

        minePrompt.add(new Label("Cat - 50 Eggs",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                        Color.WHITE)));
        minePrompt.get(1).setTouchable(Touchable.disabled);
        minePrompt.get(1).setPosition(-150, 35);
        minePrompt.get(1).setVisible(false);

        minePrompt.add(new Label("Demon - 25 Eggs",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                        Color.WHITE)));
        minePrompt.get(2).setTouchable(Touchable.disabled);
        minePrompt.get(2).setPosition(-150, 85);
        minePrompt.get(2).setVisible(false);

        minePrompt.add(new Label("Steel",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                        Color.WHITE)));
        minePrompt.get(3).setTouchable(Touchable.disabled);
        minePrompt.get(3).setPosition(-150, 135);
        minePrompt.get(3).setVisible(false);

        minePrompt.add(new Label("Water",
                new Label.LabelStyle((BitmapFont) core.assetmanager.getFile("Chewy-Regular.ttf"),
                        Color.WHITE)));
        minePrompt.get(4).setTouchable(Touchable.disabled);
        minePrompt.get(4).setPosition(-150, -15);
        minePrompt.get(4).setVisible(false);

        for(int n = 0; n < minePrompt.size; n++)
            hud.addActor(minePrompt.get(n));
    }

    private void generateDoor()
    {
        float doorLandX = MathUtils.random(-40, 105), doorLandY = MathUtils.random(10, 50);

        int offset = 5;
        if(currentLevel == 4)
            offset = 4;

        doorLand.setPosition(doorLandX, doorLandY);
        doorLand.setActive(true);
        door.respawn(doorLand.getX(), doorLand.getY() + offset);
    }

    private void generatePortal()
    {
        portalLand.setPosition(MathUtils.random(-40, 105), MathUtils.random(10, 55));
        portalLand.setActive(true);
    }

    private void generateStartPad()
    {
        startPad.setActive(true);
        startPad.setPosition(MathUtils.random(-30, 100), MathUtils.random(60, 65));
        player.setPosition(startPad.getX(), startPad.getY() + 5);
    }

    private void chanceGenerator()
    {
        //GENERATE FIRESTORM
        int fireStormRNG = MathUtils.random(0, 20);
        if(EggCount.doorAmount >= 30)
        {
            fireStormGen = true;
        }
        else if(((fireStormRNG >= 5 && fireStormRNG <= 10) || fireStormRNG == 13 || fireStormRNG == 17) && EggCount.doorAmount >= 5)
            fireStormGen = true;
        else if(((fireStormRNG >= 5 && fireStormRNG <= 7)) && EggCount.doorAmount <= 5)
            fireStormGen = true;
        else
            fireStormGen = false;
        if(!fireStormGen)
            fireStorm.despawn();
        else
            fireStorm.spawn(0, 0, 0, -45, 150, -50);

        apparitionSpawn = false;
        switch (currentLevel)
        {
            case 1:
                if(!rubbleSpawn && EggCount.doorAmount >= 5)
                {
                    rubbles.spawn(-90, MathUtils.random(-10, 60), 15, 0, 150, -100, 1.5f);
                    rubbleSpawn = true;
                }

                if (core.music.ifHorrorPlay())
                {
                    core.music.stopHorror();
                    core.music.playFirstCurrent();
                }
                randomChance = MathUtils.random(1, 100);

                //SPAWN TIKI BOSS FOR 1ST LEVEL
                if(!tikiOn && !tikiDefeated)
                {
                    if(randomChance >= 20 && randomChance <= 30 && EggCount.eggAmount >= 5)
                        for (int n = 0; n < tiki.size; n++)
                            tiki.get(n).spawn(MathUtils.random(-50, 100), 0);
                    tikiOn = true;
                }

                //MUSHROOM BOSS SPAWN
                if(!mushroomOn && !mushroomDefeat)
                {
                    if(EggCount.doorAmount >= 30)
                    {
                        mushroomBoss.spawn(MathUtils.random(-30, 80), MathUtils.random(0, 35),
                                5 * MathUtils.randomSign(), 5 * MathUtils.randomSign());
                        mushroomOn = true;
                    }
                    else if(EggCount.eggAmount <= 5)
                    {
                        if(randomChance == 30 || randomChance == 60)
                            mushroomBoss.spawn(MathUtils.random(-30, 80), MathUtils.random(0, 35),
                                    5 * MathUtils.randomSign(), 5 * MathUtils.randomSign());
                        mushroomOn = true;
                    }
                    else if (EggCount.doorAmount > 5 && EggCount.doorAmount <= 10)
                    {
                        if(randomChance >= 40 && randomChance <= 50)
                            mushroomBoss.spawn(MathUtils.random(-30, 80), MathUtils.random(0, 35),
                                    5 * MathUtils.randomSign(), 5 * MathUtils.randomSign());
                        mushroomOn = true;
                    }
                    else
                    {
                        if(randomChance >= 40 && randomChance <= 70)
                            mushroomBoss.spawn(MathUtils.random(-30, 80), MathUtils.random(0, 35),
                                    5 * MathUtils.randomSign(), 5 * MathUtils.randomSign());
                        mushroomOn = true;
                    }
                }

                //number roll for apparition spawn
                if ((randomChance == 3 || randomChance == 50 || randomChance == 99) && EggCount.doorAmount >= 5)
                {
                    apparitionSpawn = true;
                    core.music.stopFirstStage();
                    if(!core.music.musicOff)
                        core.music.playHorror();
                    apparition.spawn(MathUtils.random(40, 60), MathUtils.random(0, 20));
                }
                if (randomChance > 60 && randomChance < 90)
                {
                    Vector2 randomPos = generator.getRandomLandPos();
                    irrigator.spawn(randomPos.x, randomPos.y + 5);
                }
                if ((randomChance >= 5 && randomChance <= 20) || (randomChance >= 70 && randomChance <= 90))
                    hunter.get(0).spawn(MathUtils.random(-10, 40), MathUtils.random(-10, 40), 5);
                else if (randomChance >= 55 && randomChance <= 65) {
                    int spawnAmount = MathUtils.random(0, hunter.size - 1);
                    for (int n = 0; n < spawnAmount; n++)
                        hunter.get(n).spawn(MathUtils.random(-10, 40), MathUtils.random(-10, 40), 5);
                }
                break;

            case 3:
                if(core.music.ifHorrorPlay())
                {
                    core.music.stopHorror();
                    core.music.playThirdCurrent();
                }
                randomChance = MathUtils.random(1, 100);

                //number roll for apparition spawn
                if(randomChance == 3 || randomChance == 50 || randomChance == 99 || (randomChance >= 5 && randomChance <= 15))
                {
                    apparitionSpawn = true;
                    core.music.stopThirdStage();
                    if(!core.music.musicOff)
                        core.music.playHorror();
                    apparition.spawn(MathUtils.random(40, 60), MathUtils.random(0, 20));
                }
                if((randomChance > 66 && randomChance < 99))
                {
                    Vector2 randomPos = generator.getRandomLandPos();
                    irrigator.spawn(randomPos.x, randomPos.y + 5);
                }
                if((randomChance >= 5 && randomChance <= 20) || (randomChance >= 90 && randomChance <= 99))
                    hunter.get(0).spawn(MathUtils.random(-20, 90), MathUtils.random(-10, 40), 5);
                else if(randomChance >= 80 && randomChance <= 89)
                {
                    int spawnAmount = MathUtils.random(3, hunter.size - 1);
                    for(int n = 0; n < spawnAmount; n++)
                        hunter.get(n).spawn(MathUtils.random(-20, 90), MathUtils.random(-10, 40), 5);
                }

                if(randomChance >= 51 && randomChance <= 98)
                {
                    amntDesetTiki = MathUtils.random(1, tiki.size - 1);
                    for(int n = 0; n < amntDesetTiki; n++)
                        tiki.get(n).spawn(MathUtils.random(-50, 100), 0);
                }
                else
                {
                    if(amntDesetTiki > 0)
                        amntDesetTiki = 0;
                }
                break;
            case 4:
                if(core.music.ifHorrorPlay())
                {
                    core.music.stopHorror();
                    core.music.playFourthStageRandom();
                }
                randomChance = MathUtils.random(1, 100);

                //number roll for apparition spawn
                if((randomChance >= 1 && randomChance <= 9) || randomChance == 50)
                {
                    apparitionSpawn = true;
                    core.music.stopFourthStage();
                    if(!core.music.musicOff)
                        core.music.playHorror();
                    apparition.spawn(MathUtils.random(40, 60), MathUtils.random(0, 20));
                }
                if((randomChance > 51 && randomChance < 99))
                {
                    Vector2 randomPos = generator.getRandomLandPos();
                    irrigator.spawn(randomPos.x, randomPos.y + 5);
                }
                if((randomChance >= 5 && randomChance <= 20) || (randomChance >= 50 && randomChance <= 69))
                    hunter.get(0).spawn(MathUtils.random(-20, 90), MathUtils.random(-10, 40), 5);
                else if(randomChance >= 70 && randomChance <= 89)
                {
                    int spawnAmount = MathUtils.random(2, hunter.size - 1);
                    for(int n = 0; n < spawnAmount; n++)
                        hunter.get(n).spawn(MathUtils.random(-20, 90), MathUtils.random(-10, 40), 5);
                }
                break;
        }
    }

    private void levelSetter()
    {
        if(EggCount.doorAmount > 15 && EggCount.doorAmount <= 30)
            hud.setStartMinute(40);
        else if(EggCount.doorAmount > 30)
            hud.setStartMinute(30);

        switch (currentLevel)
        {
            case 1:
                if(EggCount.doorAmount >= 5)
                        generator.setBatSpeed(12);
                        generator.setMaxEnemies(15);
                if(EggCount.doorAmount >= 30)
                        generator.setBatSpeed(15);
                        generator.setMaxEnemies(18);
                        generator.setMinimums(1, 0, 8);
                break;
            case 3:
                if(EggCount.doorAmount >= 15)
                    orbs.setSpeed(20);
                if(EggCount.doorAmount >= 40)
                    orbs.setSpeed(25);
                break;
            case 4:
                if(EggCount.doorAmount >= 25)
                {
                    generator.setBatSpeed(13);
                    generator.setMaxEnemies(15);
                    generator.setMinimums(1, 0, 10);
                }

                if(EggCount.doorAmount >= 50)
                {
                    generator.setBatSpeed(15);
                    generator.setMaxEnemies(18);
                    generator.setMinimums(1, 0, 13);
                    orbs.setSpeed(35);
                }
                break;
        }
    }

    private void randomMap()
    {
        mapDisplay.get(indexMap).setVisible(false);
        indexMap = mapRandomizer.nextInt(mapDisplay.size);
        mapDisplay.get(indexMap).setVisible(true);
    }


    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        world.setContactListener(EventManager.getContactListener());
        world.setContactFilter(EventManager.getContactFilter());
        hud.stopTimer();
        player.setLoop(false);

        if(core.music.ifGummaPlay())
            core.music.stopGumma();

        Inventory.water = false;
        Inventory.release = false;

        //FOR MUSIC OPTION
        musicButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                core.music.musicOff = !core.music.musicOff;
                musicButton.setChecked(core.music.musicOff);

                if(core.music.musicOff)
                {
                    switch (currentLevel)
                    {
                        case 1:
                            core.music.pauseFirstCurrent();
                            break;
                        case 3:
                            core.music.pauseThirdCurrent();
                            break;
                        case 4:
                            core.music.pauseFourthCurrent();
                            break;
                    }
                }
                else
                {
                    switch (currentLevel)
                    {
                        case 1:
                            core.music.playFirstCurrent();
                            break;
                        case 3:
                            core.music.playThirdCurrent();
                            break;
                        case 4:
                            core.music.playFourthCurrent();
                            break;
                    }
                }
            }
        });


        hud.getMenuBtn().addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                menuSplash = true;
            }
        });

        mineButton.get(0).addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                if(GameStat.pumpkinCat)
                {
                    if (eng.getState() != 2)
                    {
                        eng.setState(2);
                        engIcon.setEggSubtractor(4);
                        engIcon.resetIndex();
                    }
                }
                else if(EggCount.eggAmount >= 50)
                {
                    EggCount.eggAmount -= 50;
                    GameStat.pumpkinCat = true;
                    mineLabel.get(0).setText("Use");
                }
            }
        });

        mineButton.get(1).addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                if(GameStat.pumpkinDemon)
                {
                    if (eng.getState() != 1)
                    {
                        eng.setState(1);
                        engIcon.setEggSubtractor(3);
                        engIcon.resetIndex();
                    }
                }
                else if(EggCount.eggAmount >= 25)
                {
                    EggCount.eggAmount -= 25;
                    GameStat.pumpkinDemon = true;
                    mineLabel.get(1).setText("Use");
                }
            }
        });

        mineButton.get(2).addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                if (eng.getState() != 0)
                {
                    eng.setState(0);
                    engIcon.setEggSubtractor(2);
                    engIcon.resetIndex();
                }
            }
        });

        mineButton.get(3).addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(!core.music.musicOff)
                    core.sounds.buttonClicked.play();
                if(!Inventory.water && EggCount.eggAmount >= 50)
                {
                    if(!core.music.musicOff)
                    {
                        core.sounds.pickBucket.stop();
                        core.sounds.pickBucket.play();
                    }
                    Inventory.water = true;
                    EggCount.addEgg(-50);
                    hud.setIrrigatorDisplay(true);
                }

            }
        });


        core.batch.enableBlending();
    }


    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        Gdx.gl.glClearColor(240 / 255f, 240 / 255f, 240 / 255f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        delta = Math.min(delta, 1/30f);
        world.step(Gdx.graphics.getDeltaTime(), 27, 24);      //allows box2D objects to move

        if(interScreen)
        {
            stopMusic();
            core.setScreen(new UnloadScreen(core, false, currentLevel, true));
            dispose();
        }
        else if(nextScreen)
        {
            stopMusic();
            if(!core.music.musicOff)
                core.music.playGumma();
            core.setScreen(new UnloadScreen(core, false, currentLevel, false));
            dispose();
        }
        else if(toMenu)
        {
            stopMusic();
            core.setScreen(new UnloadScreen(core, true, currentLevel, false));
            dispose();
        }
        else
        {
            //************************ POP UP IN-GAME MENU CONFIG BLOCK ***********************
            if (hud.isMenuOn())
            {
                delta = 0;
                if(engIcon.getButton().isTouchable())
                    engIcon.getButton().setTouchable(Touchable.disabled);
                if(laserIcon.getButton().isTouchable())
                    laserIcon.getButton().setTouchable(Touchable.disabled);
                if(!musicButton.isVisible())
                    musicButton.setVisible(true);

                if(menuSplash)
                {
                    if(!splash.isVisible())
                        splash.setVisible(true);
                    if (splashScale <= 2.0f)
                        splashScale += 0.03f;
                    else
                    {
                        menuSplash = false;
                        toMenu = true;
                    }
                    splash.setScale(splashScale);
                }
            }
            else
            {
                if(musicButton.isVisible())
                    musicButton.setVisible(false);

                //action button only works when the menu is not up
                if(!engIcon.getButton().isTouchable())
                    engIcon.getButton().setTouchable(Touchable.enabled);

                engIcon.setDirection(player.getDirection());
                engIcon.update(delta, player.getPos());

                if (engIcon.getClickVal())
                    engIcon.resetClickVal();

                //for lasers
                if(!laserIcon.getButton().isTouchable())
                    laserIcon.getButton().setTouchable(Touchable.enabled);

                laserIcon.setDirection(player.getDirection());
                laserIcon.update(delta, player.getPos());

                if (laserIcon.getClickVal())
                    laserIcon.resetClickVal();
            }
            //************************ POP UP IN-GAME MENU CONFIG BLOCK ***********************
            if(!apparitionSpawn && !core.music.musicOff)
            {
                switch (this.currentLevel) {
                    case 1:
                        if (core.music.getCurrentIndex() < 0)
                            core.music.playFirstStageRandom();
                        else if (!core.music.isFirstCurrentPlaying())
                            core.music.playFirstStageRandom();
                        break;
                    case 3:
                        if (core.music.getCurrentIndex() < 0)
                            core.music.playThirdStageRandom();
                        else if (!core.music.isThirdCurrentPlaying())
                            core.music.playFirstStageRandom();
                        break;
                    case 4:
                        if (core.music.getCurrentIndex() < 0)
                            core.music.playFourthStageRandom();
                        else if (!core.music.isFourthCurrentPlaying())
                            core.music.playFirstStageRandom();
                        break;
                }
            }

            objectiveConditions();
            splashScreenConfig();

            if(player.isAnimFinished() && start)    //if start entry animation is finished
            {                                       //only called once
                start = false;
                player.setActive(true);
                player.setPause(false);
                player.setLoop(true);
                player.setRegion("fly");
            }

            //debugMatrix = core.batch.getProjectionMatrix().cpy().scale(Scaler.PIXELS_TO_METERS, Scaler.PIXELS_TO_METERS, 0);

            //***************************** LOSING CONDITION BLOCK **********************************
            if((tree.isGameLost() && !lose) || (chosenCondition == 1 && EggCount.eggAmount <= 1
                    && hud.ifOutofTime() && !lose))
            {
                lostAnimation();
            }
            //***************************** LOSING CONDITION BLOCK **********************************
            screenChangeConfig();

            if(hitFx.checkRegion("charge_fx") && hitFx.getWidth() != 256 && hitFx.getHeight() != 256)
            {
                hitFx.setScale(156, 156);
            }
            else if(!hitFx.checkRegion("charge_fx") && hitFx.getWidth() != 64 && hitFx.getHeight() != 64)
                hitFx.setScale(128, 128);

            //playerRotorConfig();
            specialTimer();
            if(updater)
            {
                doorEvent();
                portalEvent();
                if(!lose)
                    camMovement();
                eng.update();
                laser.update();
                worldCollisions();
                hunterUpdate(delta);
                gemHunterDropUpdate();
                apparitionUpdate(delta);
                portalUpdate();
                treeUpdate();
                butterflyEggUpdate();
                shieldConfig();
            }
            else
            {
                if (player.isAnimFinished())
                {
                    lose = true;
                }
            }
            core.batch.setProjectionMatrix(viewport.getCamera().combined);
            viewport.getCamera().update();

            despawnBosses();
            if (generator.getEnemyHit() && !doorCollide)
            {
                if(player.isAnimFinished())
                {
                    player.setLoop(true);
                    player.setActive(true);
                    player.setPause(false);
                    player.setRegion("fly");
                    player.resetAnimation();
                    generator.resetEnemyHit(); //enemyHit = false;
                }
            }

            background.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            background.draw();

            //**************************** BEGIN ********************************************
            //**************************** RENDERING BLOCK **********************************
            core.batch.begin();

            for (int n = 0; n < block.size; n++)
                block.get(n).display();

            if(startPad.isActive())
                startPad.display();

            switch (chosenCondition)
            {
                case 0:
                    doorLand.display();
                    door.display(delta);
                    break;
                case 1:
                    portal.display(delta);
                    portalLand.display(delta);
                    break;
            }

            tree.display(core.batch, delta);

            generator.displayScreen(delta, player, hitFx, doorCollide, portalCollide);
            irrigator.display(delta);
            irrigatorDrop.display(delta);
            for(int n = 0; n < hunter.size; n++)
                hunter.get(n).display(delta);

            switch (currentLevel)
            {
                case 1:     //1st level boss
                    if(tikiOn)
                    {
                        for (int n = 0; n < tiki.size; n++)
                        {
                            if (tiki.get(n).update(delta, player.getX(), player.getY()))
                            {
                                addEgg(35);
                                tikiOn = false;
                                tikiDefeated = true;
                            }
                            if (tiki.get(n).inContact(player.getBody().getUserData()))
                                playerExplode();

                            tiki.get(n).render(delta);
                        }
                    }
                    break;
                case 3:
                    //desert tiki
                    if(amntDesetTiki >= 1 )
                    {
                        for (int n = 0; n < amntDesetTiki; n++)
                        {
                            if(tiki.get(n).update(delta, player.getX(), player.getY()))
                                addEgg(5);
                            if (tiki.get(n).inContact(player.getBody().getUserData()))
                            {
                                playerExplode();
                            }
                            tiki.get(n).render(delta);
                        }
                    }
                    break;
            }

            mushroomBossConfig(delta);

            player.setExit(hud.getExit());
            player.display(delta);
            if(shieldOn)
                shield.display(delta);
            if(doubleEggsOn || hyperSpeed)
                doubleQuo.render(core.batch, player.getX(), player.getY() + 3, delta);
            if(rubbleSpawn)
                rubbles.display(delta, doorCollide, portalCollide, toPlay);
            fireStorm.display(delta, doorCollide, portalCollide, toPlay);
            if(currentLevel == 4)
            {
                for (int n = 0; n < crossers.size; n++)
                {

                    if(crossers.get(n).update(delta, portalCollide, doorCollide, player.getBody().getUserData()))
                        playerExplode();
                    crossers.get(n).display(delta);
                }
            }

            laser.render(core, delta);
            eng.render(core, delta);
            animatedConditions(delta);

            if(ruby.isActive())
                ruby.display(delta);
            if(moonstone.isActive())
                moonstone.display(delta );

            apparition.display(delta);
            apparitionExplodeFx(delta);
            if(ifSmoke)
                smokeOn(delta);
            weatherUpdate(delta);

            core.batch.end();
            //**************************** RENDERING BLOCK **********************************
            //**************************** END **********************************************
            //System.out.println(player.getX());

            if(giftLabel.isVisible())
            {
                giftCounter += delta;
                if(giftCounter >= 4)
                {
                    giftCounter = 0;
                    giftLabel.setVisible(false);
                }
            }

            hud.render(delta, lose);

            if(startTransition || menuSplash || toPlay)
            {
                stageTransition.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                stageTransition.draw();
            }

            //debugRenderer.render(world, debugMatrix);
        }
    }

    private void mushroomBossConfig(float delta)
    {
        if(mushroomOn)
        {
            switch (mushroomBoss.update(delta, player.getBody().getUserData())) {
                case 0:
                    break;
                case 1:
                    //spawn mushroom mobs
                    switch (currentLevel)
                    {
                        case 1:
                            tikiBomb.spawn(mushroomBoss.getX(), mushroomBoss.getY(), amountOrbs);
                            amountOrbs++;
                            if (amountOrbs >= tikiBomb.getSize())
                                amountOrbs = 0;
                            break;
                        case 3:
                        case 4:
                            orbs.spawn(mushroomBoss.getX(), mushroomBoss.getY(), amountOrbs);
                            amountOrbs++;
                            if (amountOrbs >= orbs.getSize())
                                amountOrbs = 0;
                            break;
                    }
                    break;
                case 2:
                    mushroomOn = false;
                    mushroomBoss.setSpeedZero();
                    playerExplode();
                    break;
                case 3:
                    mushroomOn = false;
                    mushroomDefeat = true;
                    EggCount.addEgg(50);
                    //despawn mushroom mobs
                    switch (currentLevel)
                    {
                        case 1:
                            tikiBomb.despawnAll();
                        case 3:
                        case 4:
                            orbs.despawnAll();
                            break;
                    }
                    break;
            }

            if (currentLevel == 3 || currentLevel == 4)
                orbs.display(delta, player.getX(), player.getY(), doorCollide, portalCollide);

            if(currentLevel == 3 && turnCrow)
            {
                if(player.getBody().getLinearVelocity().x != 0 && player.getBody().getLinearVelocity().y != 0)
                    player.getBody().setLinearVelocity(0, 0);
                crowCounter += delta;
                if(crowCounter >= 1.5f)
                {
                    crowCounter = 0;
                    turnCrow = false;
                    player.setDisplay(true);
                    player.setPause(false);
                    player.getBody().setGravityScale(1f);
                }
                else
                    rookHead.render(core.batch, player.getX(), player.getY(), delta);
            }

            if(currentLevel == 1)
            {
                switch (tikiBomb.display(core.batch, delta, player.getX(), player.getY(), doorCollide, portalCollide))
                {
                    case 1:
                        if(!core.music.musicOff)
                        {
                            core.sounds.explode.stop();
                            core.sounds.explode.play(0.6f);
                        }
                        player.hit(-30, 0);
                        subEggDeadly(MathUtils.random(1, 4));
                        break;
                    case 2:
                        if(!core.music.musicOff)
                        {
                            core.sounds.explode.stop();
                            core.sounds.explode.play(0.6f);
                        }
                        player.hit(30, 0);
                        subEggDeadly(MathUtils.random(1, 4));
                        break;
                }
            }
        }

        mushroomBoss.display(delta);
    }

    private void stopMusic()
    {
        if(core.music.ifHorrorPlay())
            core.music.stopHorror();
        switch (currentLevel)
        {
            case 1:
                core.music.stopFirstStage();
                break;
            case 3:
                core.music.stopThirdStage();
                break;
            case 4:
                core.music.stopFourthStage();
                break;
        }
    }

    private void weatherUpdate(float delta)
    {
        switch (currentLevel)
        {
            case 1:
                if (windChill.ifFinished()) {
                    windX = MathUtils.random(-40, 100);
                    windY = MathUtils.random(0, 50);
                    windChill.resetTime();
                } else
                    windChill.render(core.batch, windX, windY, delta);

                if (windWave.ifFinished()) {
                    waveX = MathUtils.random(-40, 100);
                    waveY = MathUtils.random(0, 50);
                    windWave.resetTime();
                } else
                    windWave.render(core.batch, waveX, waveY, delta);
                break;
            case 3:
                dust.render(core.batch, xStorm, yStorm, delta);
                if(delta != 0)
                    xStorm += 0.5f;
                if(xStorm > 250)
                {
                    xStorm = -150;
                    yStorm = MathUtils.random(-20, 60);
                }
                break;
        }
    }

    private void addEgg(int amount)
    {
        if(EggCount.eggAmount + amount >= 999)
            EggCount.eggAmount = 999;
        else
            EggCount.addEgg(amount);
    }

    private void subEgg(int amount)
    {
        if(EggCount.eggAmount - amount <= 0)
            EggCount.eggAmount = 0;
        else
            EggCount.addEgg(-amount);
    }

    private void subEggDeadly(int amount)
    {
        if(EggCount.eggAmount - amount < 0)
        {
            EggCount.eggAmount = 0;
            if(!explode && !lose)
                playerExplode();
        }
        else
            EggCount.addEgg(-amount);
    }

    /**Sets the splash to cover the entire display screen. After the display screen
     * has been covered, the conditions of either to the nextScreen(goes to addScreen.java)
     * or toIntermission(goes to Intermission.java or intermission screen to the next stage)*/
    private void screenChangeConfig()
    {
        if(lose)
        {
            if(!splash.isVisible())
                splash.setVisible(true);
            if (splashScale <= 2.0f)
                splashScale += 0.03f;
            else
            {
                nextScreen = true;
            }
            splash.setScale(splashScale);
        }
        if(toIntermission)
        {
            if(!splash.isVisible())
                splash.setVisible(true);
            if (splashScale <= 2.0f)
                splashScale += 0.03f;
            else
            {
                interScreen = true;
            }
            splash.setScale(splashScale);
        }
    }

    /**Shows a text label after spawning the world which tells whether the
     * player is looking for a door or hidden portal to get to the next map*/
    private void objectiveConditions()
    {
        if(conditionScale > 0)          //reduce the text until invisible from
        {                               //the display screen
            if(conditionScale > 0.47)
                conditionScale -= 0.001f;
            else
                conditionScale -= 0.01f;
            conditionSet.get(chosenCondition).setScale(conditionScale);
        }
        else
        {
            if(conditionSet.get(chosenCondition).isVisible())
                conditionSet.get(chosenCondition).setVisible(false);
        }
    }

    /**The display screen starts with the game's title on the bottom with
     * a black splash screen behind. Both the title text and black splash
     * screen is reduced to show to invisibility to show the generated
     * world.*/
    private void splashScreenConfig()
    {
        if(toPlay)
        {
            if (titleScale >= 0.01f)        //reduce title text
            {
                titleScale -= 0.05f;
                title.get(0).setScale(titleScale);
                title.get(1).setScale(titleScale);
            }
            else
            {
                title.get(0).setVisible(false);
                title.get(1).setVisible(false);
            }

            if (!splash.isVisible())
                splash.setVisible(true);
            if (splashScale >= 0)               //reduce black splash screen
                splashScale -= 0.03f;
            else
            {
                splash.setVisible(false);
                start = true;
                toPlay = false;
                hud.startTimer();
            }
            splash.setScale(splashScale);
        }

    }

    /**Despawns the hunter and apparition if present when the player enters
     * either a door or portal to the next map or stage.*/
    private void despawnBosses()
    {
        if(!stopBosses)
        {
            if ((portalCollide && !mapTransition) || (doorCollide && !mapTransition))
            {
                //if (!apparition.isSpeedZero())
                    apparition.setSpeed(0, 0);
                for (int n = 0; n < hunter.size; n++)
                {
                    //if(!hunter.get(n).isSpeedZero())
                        hunter.get(n).setSpeed(0, 0);
                }

                stopBosses = true;
            }
        }
    }

    /**Handles the butterfly egg or buffing egg conditions with a numeric roll.
     * Numeric Roll Conditions
     * 0 -> 5:   Bubble shield that protects the player from harm excluding from
     *           bosses. Enemies that collide with bubble generate 1 egg.
     * 6 -> 14:  Increases the player speed.
     * 15 -> 20: Doubles the egg values received by the player.*/
    private void butterflyEggUpdate()
    {
        if(EggPOD.butterflySpawn)
        {
            EggPOD.butterflySpawn = false;
            butterflyChance = MathUtils.random(0, 20);
            if(butterflyChance >= 0 && butterflyChance <= 5)
            {
                shieldOn = true;
                if(hyperSpeed)
                    hyperSpeed = false;
                if(doubleEggsOn)
                {
                    doubleEggsOn = false;
                    EggPOD.doubleOn = false;
                }
            }
            else if(butterflyChance >= 6 && butterflyChance <= 14)
            {
                doubleQuo.findRegion("doubleSpeed");
                player.setSpeed(30);
                hyperSpeed = true;
                if(doubleEggsOn)
                {
                    doubleEggsOn = false;
                    EggPOD.doubleOn = false;
                }
                if(shieldOn)
                {
                    shieldOn = false;
                    despawnShield();
                }
            }
            else if(butterflyChance >= 15 && butterflyChance <= 20)
            {
                doubleQuo.findRegion("double");
                doubleEggsOn = true;
                EggPOD.doubleOn = true;
                if(hyperSpeed)
                    hyperSpeed = false;
                if(shieldOn)
                {
                    shieldOn = false;
                    despawnShield();
                }
            }
        }
    }

    /**Handles tree mechanics such as incrementing and decrementing the state.*/
    private void treeUpdate()
    {
        //Increments the tree's state after pouring water
        if(tree.update(player.getBody().getUserData(), maxState) == 1 && !tree.isFullGrown())
        {
            if(!core.music.musicOff)
                core.sounds.pourWater.play();
            hud.setIrrigatorDisplay(false);
            hud.resetTimer();

            for(int n = 0; n < mineLabel.size; n++)
                mineLabel.get(n).setVisible(true);
            for(int n = 0; n < minePrompt.size; n++)
                minePrompt.get(n).setVisible(true);
            for(int n = 0; n < mineButton.size; n++)
                mineButton.get(n).setVisible(true);
        }
        else if(tree.inArea(player.getX(), player.getY()))
        {
            for(int n = 0; n < mineLabel.size; n++)
                mineLabel.get(n).setVisible(true);
            for(int n = 0; n < minePrompt.size; n++)
                minePrompt.get(n).setVisible(true);
            for(int n = 0; n < mineButton.size; n++)
                mineButton.get(n).setVisible(true);
        }
        else
        {
            for(int n = 0; n < mineLabel.size; n++)
                mineLabel.get(n).setVisible(false);
            for(int n = 0; n < minePrompt.size; n++)
                minePrompt.get(n).setVisible(false);
            for(int n = 0; n < mineButton.size; n++)
                mineButton.get(n).setVisible(false);
        }

        if(tree.isFullGrown() && giftTree && (!GameStat.pumpkinDemon || !GameStat.pumpkinCat))
        {
            int rng = MathUtils.random(1, 20);
            if(rng == 2 || rng == 20 || rng == 10 || rng == 15)
            {
                giftTree = false;
                GameStat.pumpkinDemon = true;
                giftLabel.setVisible(true);
                mineLabel.get(1).setText("Use");

            }
            else if(rng == 8 || rng == 12)
            {
                giftTree = false;
                GameStat.pumpkinCat = true;
                giftLabel.setVisible(true);
                mineLabel.get(0).setText("Use");
            }
        }

        //picks up a randomly generated bucket
        if(irrigator.update(player.getBody().getUserData(), player.getX(), player.getY(), player.getDirection()) == 1)
        {
            if(!core.music.musicOff)
                core.sounds.pickBucket.play();
            hud.setIrrigatorDisplay(true);
        }
        //picks up a bucket dropped from a boss
        if(irrigatorDrop.update(player.getBody().getUserData(), player.getX(), player.getY(), player.getDirection()) == 1)
        {
            if(!core.music.musicOff)
                core.sounds.pickBucket.play();
            irrigatorDrop.stopRotation();
            hud.setIrrigatorDisplay(true);
        }
        //tree decrements state after timer goes to 0
        if(hud.ifOutofTime() && !lose)
        {
            tree.revertState();
            hud.resetTimer();
        }
    }

    /**Spawns and renders the portal on the screen after destroying
     * the land hiding portal with a mine.*/
    private void portalUpdate()
    {
        if(portalOn)
        {
            if(!portal.getRegion().equals("portal_in"))
            {
                portal.respawn(portalLand.getX(), portalLand.getY());
                portal.setOpen();
                //portal.resetAnimation();
                portalOn = false;
                portalLand.setPosition(-700, -700);
                portalLand.setActive(false);
            }
        }
        if(portal.ifAnimFinished() && !portalCollide)
            if(!portal.getRegion().equals("portal"))
                portal.setFlash();
    }

    /**Lets the apparition track and follow the player's location.
     * This also handles apparition collision with the surrounding
     * world.*/
    private void apparitionUpdate(float delta)
    {
        if(!stopBosses)
        {
            apparition.update(player.getX(), player.getY(), delta);
            if (apparition.ifCollison() && !explode && !nonGeneratedCol)
            {
                explode = true;
                explosion.resetTime();
                if(!core.music.musicOff)
                {
                    core.sounds.explode.stop();
                    core.sounds.explode.play();
                }
            }
        }
    }

    /**Handles picking up and adding the eggs receiver from the
     * egg. Sound effects included here.*/
    private void gemHunterDropUpdate()
    {
        if(ruby.update(player.getBody().getUserData()) == 1)
        {
            if(!core.music.musicOff)
                core.sounds.gemPicked.play();
            if(EggPOD.doubleOn)
                EggCount.addEgg(20);
            else
                EggCount.addEgg(10);
        }
        if(moonstone.update(player.getBody().getUserData()) == 1)
        {
            if(!core.music.musicOff)
                core.sounds.gemPicked.play();
            if(EggPOD.doubleOn)
                EggCount.addEgg(100);
            else
                EggCount.addEgg(50);
        }
    }

    /**Updates the hunter if the player is the vicinity of it's line vision
     * so it will then follow and try to get to the player.
     * This also handles collision with the player, death for no HP, and
     * dropping gems and water bucket.*/
    private void hunterUpdate(float delta)
    {
        if(!stopBosses)
        {
            for (int n = 0; n < hunter.size; n++)
            {
                if (hunter.get(n).getDisplayOrActive())
                {
                    hunter.get(n).update(player.getX(), player.getY(), delta);

                    if (hunter.get(n).inCollisonWith(player.getBody().getUserData()) && (!portalCollide || !doorCollide))
                    {
                        if(!core.music.musicOff)
                        {
                            core.sounds.explode.stop();
                            core.sounds.explode.play();
                        }
                        explode = true;
                        nonGeneratedCol = true;
                        explodeX = player.getX();
                        explodeY = player.getY();

                        if (player.getBody().isActive())
                        {
                            if (!player.isPaused())
                                player.setPause(true);
                            player.setPosition(-700, -700);
                            player.getBody().setLinearVelocity(0, 0);
                            player.setActive(false);
                        }

                        updater = false;
                        generator.resetEnemyHit();
                        enemyHit = false;
                        hud.stopTimer();
                        lose = true;
                    }

                    if (hunter.get(n).isHPZero())
                    {
                        addEgg(15);
                        specItemRoll = MathUtils.random(0, 20);
                        ifSmoke = true;
                        smokeX = hunter.get(n).getX();
                        smokeY = hunter.get(n).getY();
                        if (specItemRoll >= 8 && specItemRoll <= 10)
                        {
                            if (!irrigatorDrop.isActive())
                            {
                                if(!core.music.musicOff)
                                    core.sounds.gemFound.play(1);
                                irrigatorDrop.spawn(hunter.get(n).getX(), hunter.get(n).getY());
                                irrigatorDrop.setRotation(0.5f);
                            }
                        }
                        if ((specItemRoll >= 0 && specItemRoll <= 7) || (specItemRoll >= 11 && specItemRoll <= 19))
                        {
                            if(!core.music.musicOff)
                                core.sounds.gemFound.play(1);
                            ruby.spawn(hunter.get(n).getX(), hunter.get(n).getY(), 0.5f);
                        }
                        if (specItemRoll == 20)
                        {
                            if(!core.music.musicOff)
                                core.sounds.gemFound.play(1);
                            moonstone.spawn(hunter.get(n).getX(), hunter.get(n).getY(), 0.5f);
                        }
                        hunter.get(n).despawn();
                    }
                }
            }
        }
    }

    /**Each buff from a butterfly egg is 8 in-game minutes long(like 10 seconds IRT)*/
    private void specialTimer()
    {
        if(shieldOn || doubleEggsOn || hyperSpeed)
        {
            if(outTimer >= 8)
            {
                if(shieldOn)
                    despawnShield();
                if(doubleEggsOn)
                    despawnDouble();
                if(hyperSpeed)
                    despawnHyper();
                outTimer = 0;
                second = 0;
            }
            else
            {
                second++;
                if(second >= 60)
                {
                    second = 0;
                    outTimer++;
                }
            }
        }
    }

    /**Sets a losing condition to the addscreen after the player disappearing
     * animation finishes.*/
    private void lostAnimation()
    {
        updater = false;
        if (!player.getRegion().equals("disapear"))
        {
            enemyHit = false;
            if (!player.isPaused())
                player.setPause(true);
            player.getBody().setLinearVelocity(0, 0);
            player.setDisapear();
        }
        if (player.isAnimFinished())
            lose = true;
    }

    private void animatedConditions(float delta)
    {
        if (generator.ifEggHit() || spiritHit)
        {
            if (!hitFx.ifFinished())
            {
                hitFx.render(core.batch, player.getX(), player.getY(), delta);
            } else
            {
                if(spiritHit)
                    spiritHit = false;
                if(generator.ifEggHit())
                    generator.setEggHitToFalse();
                hitFx.resetTime();
            }
        }
    }

    private void shieldConfig()
    {
        if(shieldOn)
        {
            if(!shield.isActive() || !shield.ifDisplayed())
            {
                shield.setActive(true);
                shield.setDisplay(true);
            }

            shield.setPosition(player.getX(), player.getY());
        }
    }

    private void smokeOn(float delta)
    {
        if(!smoke.ifFinished())
        {
            smoke.render(core.batch, smokeX, smokeY, delta);
        }
        else {
            ifSmoke = false;
            smoke.resetTime();
        }
    }

    private void apparitionExplodeFx(float delta)
    {
        if(nonGeneratedCol)
        {
            if(explode)
            {
                if (!explosion.ifFinished())
                    explosion.render(core.batch, explodeX, explodeY, delta);
                else
                {
                    explosion.resetTime();
                    nonGeneratedCol = false;
                    explode = false;
                }
            }
        }
        else
        {
            if(explode)
            {
                if (!explosion.ifFinished())
                    explosion.render(core.batch, generator.getColX(), generator.getColY(), delta);
                else
                {
                    explosion.resetTime();
                    explode = false;
                }
            }
        }
    }

    /**Runs right after player collides with a portal. The world is then
     * despawned and if not going to the next stage, another new world
     * is then spawned.*/
    private void portalEvent()
    {
        if (portal.ifCollide(player.getBody().getUserData()) && !enemyHit)
        {
            if(EggCount.doorAmount >= maxDoors && EggCount.eggAmount < maxEggs)
            {
                if(!doorWarning.isVisible())
                {
                    doorWarning.setText("You need " + maxEggs + " eggs to pass!");
                    doorWarning.setVisible(true);
                }
                return;
            }

            player.setGravityScale(0);
            player.setActive(false);
            hud.stopTimer();
            if (!player.isPaused())
                player.setPause(true);
            player.getBody().setLinearVelocity(0, 0);
            portalCollide = true;
        }

        if (portalCollide)
        {
            if (!reset)
                portalExit();
            else
            {
                //if(tree.isFullGrown())
                if(EggCount.doorAmount >= maxDoors)
                    toIntermission = true;
                else
                    mapTransition = true;

                if(mapTransition)
                {
                    if (landGenerate)
                        mapRegeneration();
                    else
                    {
                        playerEntry();
                        portal.despawn();
                    }
                }
            }
        }
    }

    /**Runs right after player collides with a door. The world is then
     * despawned and if not going to the next stage, another new world
     * is then spawned.*/
    private void doorEvent()
    {
        if (door.ifCollide(player.getBody().getUserData()) && !enemyHit)
        {
            if(EggCount.doorAmount >= maxDoors && EggCount.eggAmount < maxEggs)
            {
                if(!doorWarning.isVisible())
                {
                    doorWarning.setText("You need " + maxEggs + " eggs to pass!");
                    doorWarning.setVisible(true);
                }
                return;
            }

            player.setGravityScale(0);
            player.setActive(false);
            hud.stopTimer();
            if (!player.isPaused())
                player.setPause(true);
            player.getBody().setLinearVelocity(0, 0);
            doorCollide = true;
        }
        else
        {
            if(doorWarning.isVisible())
                doorWarning.setVisible(false);
        }

        if (doorCollide)
        {
            if (!reset)
                playerExit();
            else
            {
                //if(tree.isFullGrown())
                if(EggCount.doorAmount >= maxDoors)
                    toIntermission = true;
                else
                    mapTransition = true;

                if(mapTransition)
                {
                    if (landGenerate)
                        mapRegeneration();
                    else
                    {
                        playerEntry();
                    }
                }
            }
        }
    }

    private void despawnShield()
    {
        shieldOn = false;
        shield.setPosition(-700, -700);
        shield.setActive(false);
        shield.setDisplay(false);
    }

    private void despawnDouble()
    {
        doubleEggsOn = false;
        EggPOD.doubleOn = false;
    }

    private void despawnHyper()
    {
        hyperSpeed = false;
        player.setSpeed(15);
    }

    private void mapRegeneration()
    {
        if(EggCount.doorAmount > 21 && maxEggs != 100)
            maxEggs = 100;

        //despawn all present desert tikis
        if(amntDesetTiki >= 1)
        {
            for (int n = 0; n < amntDesetTiki; n++)
                tiki.get(n).despawn();
        }

        rubbles.respawn();

        chosenCondition = MathUtils.random(0, 1);
        stopBosses = false;
        if(apparition.getDisplayOrActive())
            apparition.despawn();

        //despawn current world
        hud.resetTimer();
        for(int n = 0; n < hunter.size; n++)
            hunter.get(n).despawn();
        generator.despawn();
        irrigator.despawn();
        Inventory.water = false;
        hud.setIrrigatorDisplay(false);

        EggPOD.butterflySpawn = false;
        despawnShield();
        despawnDouble();
        despawnHyper();

        //respawn world
        randomMap();
        generator.generateMap();
        switch (chosenCondition)
        {
            case 0:
                conditionSet.get(0).setVisible(true);
                conditionScale = 0.5f;
                conditionSet.get(0).setPosition(-100, -75);
                conditionSet.get(0).setScale(conditionScale);
                generateDoor();
                portal.despawn();
                portalLand.setPosition(-700, -700);
                portalLand.setActive(false);
                break;
            case 1:
                conditionSet.get(1).setVisible(true);
                conditionScale = 0.5f;
                conditionSet.get(1).setPosition(-150, -75);
                conditionSet.get(1).setScale(conditionScale);
                generatePortal();
                door.despawn();
                doorLand.setPosition(-700, -700);
                doorLand.setActive(false);
                break;
        }
        generateStartPad();
        chanceGenerator();

        landGenerate = false;
        door.resetAnimation();
        door.setFlash();
    }

    /**Resets the world after the player enters the screen after
     * a world spawn.*/
    private void playerEntry()
    {
        if (!player.getRegion().equals("entry"))
        {
            if(!core.music.musicOff)
                core.sounds.disapear.play(0.5f);
            player.setActive(false);
            player.setAppear();
        }
        if (player.isAnimFinished())
        {
            player.setActive(true);
            player.setPause(false);
            player.setGravityScale(1);
            player.resetAnimation();
            player.setRegion("fly");
            player.setGravity();
            reset = false;
            if(doorCollide)
                doorCollide = false;
            if(portalCollide)
                portalCollide = false;
            levelSetter();
            hud.startTimer();
            mapTransition = false;
            EggCount.doorAmount++;
            /**
            switch (currentLevel)
            {
                case 1:
                    if(tree.getState() == 3)
                        bossCondition = true;
                    break;
                case 3:
                    break;
            }*/
        }
    }

    /**Animation when the player enters a door*/
    private void playerExit()
    {
        if (!door.getRegion().equals("door_open"))
        {
            if (!player.getRegion().equals("disapear"))
            {
                player.setDisapear();
                if(!core.music.musicOff)
                    core.sounds.disapear.play(0.5f);
            }
            door.setOpen();
        }
        if (door.ifAnimFinished())
        {
            if (player.isAnimFinished())
            {
                reset = true;
                landGenerate = true;
                startTransition = true;
            }
        }
    }

    private void portalExit()
    {
        if (!portal.getRegion().equals("portal_out"))
        {
            if (!player.getRegion().equals("disapear"))
            {
                player.setDisapear();
                if(!core.music.musicOff)
                    core.sounds.disapear.play(0.5f);
            }
            portal.setClose();
        }
        if (portal.ifAnimFinished())
        {
            if (player.isAnimFinished())
            {
                reset = true;
                landGenerate = true;
                startTransition = true;
            }
        }
    }

    private void worldCollisions()
    {
        if (EventManager.conditions(block.get(2).getBody().getUserData(), player.getBody().getUserData()))
            player.setPosition(block.get(3).getX() - 15, player.getY());
        else if (EventManager.conditions(block.get(3).getBody().getUserData(), player.getBody().getUserData()))
            player.setPosition(block.get(2).getX() + 15, player.getY());
        else if (EventManager.conditions(block.get(1).getBody().getUserData(), player.getBody().getUserData()))
            player.setPosition(player.getX(), block.get(0).getY() - 10);

        if ((ObjectID.mineList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == portalLand.getBody().getUserData())
                || (ObjectID.mineList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == portalLand.getBody().getUserData()))
        {
            if(!core.music.musicOff)
                core.sounds.portalRift.play();
            portalOn = true;
        }

        if((ObjectID.enemyList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == portalLand.getBody().getUserData())
                || (ObjectID.enemyList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == portalLand.getBody().getUserData()))
        {
            if(!core.music.musicOff)
            {
                core.sounds.explode.stop();
                core.sounds.explode.play();
            }
            explode = true;
            nonGeneratedCol = true;
            explodeX = portalLand.getX();
            explodeY = portalLand.getY();
            portalOn = true;
        }

        if((ObjectID.enemyList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == startPad.getBody().getUserData())
                || (ObjectID.enemyList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == startPad.getBody().getUserData()))
        {
            if(!core.music.musicOff)
            {
                core.sounds.explode.stop();
                core.sounds.explode.play();
            }
            explode = true;
            nonGeneratedCol = true;
            explodeX = startPad.getX();
            explodeY = startPad.getY();
            startPad.setPosition(-700, -700);
            startPad.setActive(false);
        }

        if((ObjectID.mineList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == startPad.getBody().getUserData())
                || (ObjectID.mineList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == startPad.getBody().getUserData()))
        {
            startPad.setPosition(-700, -700);
            startPad.setActive(false);
        }

        if ((ObjectID.enemyProjectile.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == player.getBody().getUserData())
                || (ObjectID.enemyProjectile.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == player.getBody().getUserData()))
        {
            if(EggCount.eggAmount <= 0)
            {
                playerExplode();
            }
            else
            {
                if(EggCount.eggAmount - 2 > 0)
                    EggCount.addEgg(-2);
                else
                    EggCount.eggAmount = 0;
            }
        }

        if ((ObjectID.spiritOrbs.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == player.getBody().getUserData())
                || (ObjectID.spiritOrbs.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == player.getBody().getUserData()))
        {
            spiritHit = true;
            if(!hitFx.getRegion().equals("charge_fx"))
                hitFx.findRegion("charge_fx");
            if(!core.music.musicOff)
                core.sounds.entry.stop();
            if(!core.music.musicOff)
                core.sounds.entry.play(0.5f);
            if(EggCount.eggAmount <= 0)
            {
                playerExplode();
            }
            else
            {
                if(EggCount.eggAmount - 2 > 0)
                    EggCount.addEgg(-2);
                else
                    EggCount.eggAmount = 0;
            }

            if(currentLevel == 3)
            {
                if(!turnCrow)
                {
                    turnCrow = true;
                    player.setPause(true);
                    player.setDisplay(false);
                    player.getBody().setLinearVelocity(0, 0);
                    player.getBody().setGravityScale(0);
                }
                else
                    crowCounter = 0;
            }
        }

        //apparition vs player
        if((ObjectID.enemyList.containsKey(EventManager.getUserA()) &&
                EventManager.getUserB() == player.getBody().getUserData())
                || (ObjectID.enemyList.containsKey(EventManager.getUserB()) &&
                EventManager.getUserA() == player.getBody().getUserData()))
        {
            playerExplode();
        }

        if(player.hitRubble())
        {
            playerExplode();
        }
    }

    private void playerExplode()
    {
        if(!lose)
        {
            lose = true;
            if (player.getBody().isActive()) {
                explodeX = player.getX();
                explodeY = player.getY();
                player.setPosition(-700, -700);
                player.getBody().setLinearVelocity(0, 0);
                if (!player.isPaused())
                    player.setPause(true);
                player.setActive(false);
            }

            if(!core.music.musicOff)
            {
                core.sounds.explode.stop();
                core.sounds.explode.play();
            }
            explode = true;
            nonGeneratedCol = true;

            updater = false;
            generator.resetEnemyHit();
            enemyHit = false;
            hud.stopTimer();
        }
    }

    /** @see ApplicationListener#resize(int, int) */
    public void resize (int width, int height)
    {
        viewport.update(width, height, false);
        hud.update(width, height);
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
        hud.dispose();
        stageTransition.dispose();
        world.dispose();
        background.dispose();

        Gdx.app.log("MapStage.java:", "Fully Disposed");
    }

    /**Lets the camera follow the player position.
     * offset coordinates are used to adjust if the camera is too high, too low, etc.
     * camPosition is a referece to the viewport camera*/
    private void camMovement()
    {
        camPosition = viewport.getCamera().position;
        camPosition.x += (player.getX() * Scaler.PIXELS_TO_METERS - camPosition.x) * lerp;
        camPosition.y += (player.getY() * Scaler.PIXELS_TO_METERS - camPosition.y + camOffY) * lerp;
    }
}
