package org.battelle.katana.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.battelle.katana.mechanics.Cut;
import org.battelle.katana.mechanics.Cutter;
import org.battelle.katana.mechanics.Fader;
import org.battelle.katana.mechanics.ImageCursor;
import org.battelle.katana.mechanics.Launcher;
import org.battelle.katana.mechanics.Physics;
import org.battelle.katana.mechanics.Timer;
import org.battelle.katana.model.GameModel;
import org.battelle.katana.model.Mode;
import org.battelle.katana.object.IKatanaObject;
import org.battelle.katana.object.KatanaObject;
import org.battelle.katana.object.KatanaObjectFactory;
import org.battelle.katana.object.ObjectType;
import org.battelle.katana.view.FruitCutInRowObserver;
import org.battelle.katana.view.HeartObserver;
import org.battelle.katana.view.LevelObserver;
import org.battelle.katana.view.ScoreObserver;
import org.battelle.katana.view.TotalFruitObserver;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.GeometryInfo;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PGraphics;
import processing.core.PImage;

public class SaladSamuraiScene extends GameScene
{

	private MTApplication mtApp;
	private MTCanvas gameCanvas;
	private String imagesPath = "resources" + MTApplication.separator
			+ "images" + MTApplication.separator;
	private Cutter cutter;
	private HeartObserver heartObserver;
	private TotalFruitObserver totalFruitObserver;
	private FruitCutInRowObserver fruitCutInRowObserver;
	private ScoreObserver scoreObserver;
	private LevelObserver levelObserver;
	private GameModel model;
	private Launcher launcher;
	private Physics physicsEngine;
	private Timer timer;
	private Timer launchTimer;
	private KatanaObjectFactory katanaFac;
	private boolean frenzyModeForHearts = false;

	private int launchCycle = 1;

	private int frenzyLaunchCycle = 0;
	private Random random;
	private ArrayList<IKatanaObject> launchedItems;
	private Mode gameMode;
	private boolean isVisible;
	private MTColor textColor;
	private MTColor trailColor;
	private float currentTime;
	private float previousTime;
	private final String backgroundPath = "resources" + MTApplication.separator
			+ "images" + MTApplication.separator + "background"
			+ MTApplication.separator;
	private boolean starting;

	public SaladSamuraiScene(MTApplication mtApplication, String name,
			Mode gameMode, KatanaObjectFactory kFac, MTColor textColor,
			MTColor trailColor)
	{
		super(mtApplication, name);

		this.mtApp = mtApplication;
		this.trailColor = trailColor;

		this.gameMode = gameMode;

		this.textColor = textColor;

		this.gameCanvas = getCanvas();

		this.model = new GameModel(gameMode);

		this.launcher = new Launcher(this.mtApp);

		setClearColor(new MTColor(0.0F, 0.0F, 0.0F));

		this.cutter = new Cutter(this.mtApp, getCanvas(), this.textColor,
				this.trailColor);
		getCanvas().addChild(this.cutter);

		registerGlobalInputProcessor(new ImageCursor(mtApplication, this,
				this.mtApp.loadImage(this.imagesPath + "/misc/sword.png")));

		this.physicsEngine = new Physics(this.mtApp);

		this.launchedItems = new ArrayList();

		this.timer = new Timer();

		this.launchTimer = new Timer();

		this.katanaFac = kFac;

		this.random = new Random();

		this.starting = true;

		this.katanaFac.init(this.launcher, this.model, this.timer);
	}

	public void onEnter()
	{
		super.init();
		this.isVisible = true;
	}

	public void onLeave()
	{
		super.shutDown();
		this.isVisible = false;
	}

	private void launchSomeFruits()
	{
		int i = 0;

		if (this.model.getFrenzyMode())
		{
			i = 10 + this.model.getLevel();
			this.model.setFrenzyMode(false);
			this.frenzyModeForHearts = true;
		}
		else
		{
			i = this.random.nextInt(this.model.getLevel()) + 3;
		}

		while (i > 0)
		{
			KatanaObject toLaunch = this.katanaFac
					.create(ObjectType.RANDOMFRUIT);
			toLaunch.getComponent().removeAllGestureEventListeners();
			this.launchedItems.add(toLaunch.launch());
			this.gameCanvas.addChild(((IKatanaObject) this.launchedItems
					.get(this.launchedItems.size() - 1)).getComponent());
			i--;
		}

		int randomExtra = this.random.nextInt(14);

		int j = this.random.nextInt(this.model.getLevel()) + 1;
		while (j > 0)
		{
			KatanaObject randomLaunch;
			switch (randomExtra)
			{
				case 0:
					randomLaunch = this.katanaFac.create(ObjectType.TIMEWARP);
					break;
				case 1:
					randomLaunch = this.katanaFac.create(ObjectType.FRENZY);
					break;
				case 2:
					randomLaunch = this.katanaFac.create(ObjectType.TIMEWARP);
					break;
				case 3:
				case 4:
					randomLaunch = this.katanaFac
							.create(ObjectType.RANDOMFRUIT);
				default:
					if ((this.gameMode == Mode.ARCADE)
							&& (this.gameMode != Mode.PRACTICE))
					{
						randomLaunch = this.katanaFac
								.create(ObjectType.ARCADEBOMB);
					}
					else
					{
						if (this.gameMode != Mode.PRACTICE)
							randomLaunch = this.katanaFac
									.create(ObjectType.CLASSICBOMB);
						else
							randomLaunch = this.katanaFac
									.create(ObjectType.RANDOMFRUIT);
					}
			}
			randomLaunch.getComponent().removeAllGestureEventListeners();
			this.launchedItems.add(randomLaunch.launch());
			this.gameCanvas.addChild(((IKatanaObject) this.launchedItems
					.get(this.launchedItems.size() - 1)).getComponent());
			randomExtra += 3;
			j--;
		}
	}

	private void createFruitCounter()
	{
		this.totalFruitObserver = new TotalFruitObserver(this.mtApp,
				this.gameCanvas, this.textColor);

		this.model.addTotalFruitObserver(this.totalFruitObserver);

		KatanaObject launchedFruit = this.katanaFac
				.create(ObjectType.RANDOMFRUIT);
		PImage dropTexture = launchedFruit.getTexture();

		AbstractShape fruitComponent = (AbstractShape) launchedFruit
				.getComponent();

		GeometryInfo gi = new GeometryInfo(this.mtApp, fruitComponent
				.getGeometryInfo().getVertices(), fruitComponent
				.getGeometryInfo().getNormals(), fruitComponent
				.getGeometryInfo().getIndices());

		if ((fruitComponent instanceof MTTriangleMesh))
		{
			MTTriangleMesh droppedComponentOne = new MTTriangleMesh(this.mtApp,	gi);

			droppedComponentOne.scale(0.25F, 0.25F, 0.25F,
					fruitComponent.getCenterPointLocal());
			KatanaObject firstObject = new KatanaObject(droppedComponentOne,
					this.launcher, this.model, ObjectType.RANDOMFRUIT,
					this.timer);

			firstObject.setPosition(new Vector3D(400.0F, 400.0F, 20.0F));
			firstObject.setCut();
			droppedComponentOne.setTexture(dropTexture);
			droppedComponentOne.setPositionGlobal(new Vector3D(
					2 * this.mtApp.width / 13, this.mtApp.height / 25, 0.0F));
			droppedComponentOne.removeAllGestureEventListeners();
			this.gameCanvas.addChild(droppedComponentOne);
		}
		else
		{
			MTPolygon stationaryImage = new MTPolygon(this.mtApp,
					gi.getVertices());

			stationaryImage.scale(0.25F, 0.25F, 0.25F,
					fruitComponent.getCenterPointLocal());
			KatanaObject newObject = new KatanaObject(stationaryImage,
					this.launcher, this.model, ObjectType.RANDOMFRUIT,
					this.timer);

			newObject.setPosition(new Vector3D(400.0F, 400.0F, 20.0F));
			newObject.setCut();
			stationaryImage.setTexture(dropTexture);
			stationaryImage.setPositionGlobal(new Vector3D(
					2 * this.mtApp.width / 13, this.mtApp.height / 25, 0.0F));
			stationaryImage.removeAllGestureEventListeners();
			this.gameCanvas.addChild(stationaryImage);
		}
	}

	private void createQuitButton()
	{
		MTImageButton quit = new MTImageButton(
				this.mtApp.loadImage(this.imagesPath + "misc/closebutton.png"),
				this.mtApp);
		quit.setSizeXYGlobal(this.mtApp.width / 16, this.mtApp.width / 16);
		quit.setNoStroke(true);
		if (MT4jSettings.getInstance().isOpenGlMode())
		{
			quit.setUseDirectGL(true);
		}
		quit.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{

			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent) ge;
				if (te.isTapped())
				{
					SaladSamuraiScene.this.mtApp
							.invokeLater(new SceneChanger(
									SaladSamuraiScene.this.mtApp,
									SaladSamuraiScene.this.mtApp
											.getScene("Menu Scene")));
				}
				return true;
			}
		});
		quit.setPositionGlobal(new Vector3D(this.mtApp.width
				- quit.getWidthXY(TransformSpace.GLOBAL) / 2.0F,
				this.mtApp.height - quit.getHeightXY(TransformSpace.GLOBAL)
						/ 2.0F));
		this.gameCanvas.addChild(quit);
	}

	public void drawAndUpdate(PGraphics graphics, long timeDelta)
	{
		this.currentTime = getElapsedTime();
		if (this.isVisible)
		{
			if (this.starting)
			{
				loadBG();
				loadObservers();
				createQuitButton();

				// FIXME convoluted. decompiler messed it up but it looks like
				// jeff was using an external table for switching, which i dont
				// have.
				
				// switch
				// ($SWITCH_TABLE$org$battelle$katana$model$Mode()[this.gameMode.ordinal()])
				// {
				// case 1:
				// break;
				// case 2:
				// this.model.removeHeart();
				// this.model.removeHeart();
				// break;
				// case 3:
				// break;
				// }

				this.timer.start();
				this.launchTimer.start();
				launchSomeFruits();
				this.starting = false;
			}
			if (this.launchedItems.size() > 0)
			{
				for (int i = 0; i < this.launchedItems.size(); i++)
				{
					KatanaObject checkingLauncher = (KatanaObject) this.launchedItems
							.get(i);

					if ((checkingLauncher.getComponent() instanceof MTTriangleMesh))
					{
						checkingLauncher.getComponent().rotateX(
								checkingLauncher.getCenterPointLocal(), 2.0F,
								TransformSpace.LOCAL);
						checkingLauncher.getComponent().rotateY(
								checkingLauncher.getCenterPointLocal(), 1.0F,
								TransformSpace.LOCAL);
					}
					else
					{
						checkingLauncher
								.getComponent()
								.rotateZ(
										((IKatanaObject) this.launchedItems
												.get(i)).getCenterPointLocal(),
										2.0F,
										TransformSpace.LOCAL);
					}

					this.physicsEngine.doPhysicsStep(checkingLauncher,
							this.currentTime - this.previousTime);

//					switch ($SWITCH_TABLE$org$battelle$katana$object$ObjectType()[checkingLauncher
//							.getType().ordinal()])
//					{
//						case 6:
//						case 7:
//						case 8:
//						case 9:
//							if ((this.cutter.getActiveCuts() > 0)
//									&& (!checkingLauncher.hasBeenCut())
//									&& (checkingLauncher
//											.checkValidCut(this.cutter) != null))
//							{
//								if ((checkingLauncher.getComponent() instanceof MTTriangleMesh))
//									dropSpheres(checkingLauncher,
//											checkingLauncher
//													.checkValidCut(this.cutter));
//								else
//								{
//									eraseShape(checkingLauncher);
//								}
//
//								checkingLauncher.performOnCut();
//								if (this.timer.getComboTimer() > 0L)
//								{
//									if (System.currentTimeMillis()
//											- this.timer.getComboTimer() < 250L)
//									{
//										this.model.updateComboCount();
//										break label432;
//									}
//								}
//								this.model.resetComboCount();
//								this.timer.comboTimerStart();
//
//								this.launchedItems.remove(i);
//								i--;
//							}
//							else
//							{
//								if (checkingLauncher.getCenterPointGlobal().y
//										- checkingLauncher.getRadius() <= this.mtApp.height)
//									break;
//								if ((!this.frenzyModeForHearts)
//										&& (!checkingLauncher.hasBeenCut())
//										&& (this.gameMode != Mode.PRACTICE))
//								{
//									this.model.removeHeart();
//									this.model.resetNumberOfFruitsCutInRow();
//								}
//								else if (this.frenzyModeForHearts)
//								{
//									this.frenzyLaunchCycle = this.launchCycle;
//								}
//								this.launchedItems.remove(i);
//								checkingLauncher.destroy();
//								i--;
//							}
//							break;
//						case 1:
//						case 2:
//						case 3:
//						case 4:
//						case 5:
//							label432: if ((this.cutter.getActiveCuts() > 0)
//									&& (checkingLauncher
//											.checkValidCut(this.cutter) != null))
//							{
//								if ((checkingLauncher.getType() == ObjectType.ARCADEBOMB)
//										|| (checkingLauncher.getType() == ObjectType.CLASSICBOMB))
//								{
//									animateExplosion();
//								}
//
//								if ((checkingLauncher.getComponent() instanceof MTPolygon))
//								{
//									eraseShape(checkingLauncher);
//								}
//								((IKatanaObject) this.launchedItems.remove(i))
//										.performOnCut();
//								i--;
//							}
//							else
//							{
//								if (checkingLauncher.getCenterPointGlobal().y
//										- checkingLauncher.getRadius() <= this.mtApp.height)
//									break;
//								if (this.frenzyModeForHearts)
//								{
//									this.frenzyLaunchCycle = this.launchCycle;
//								}
//								this.launchedItems.remove(i);
//								checkingLauncher.destroy();
//								i--;
//							}
//
//					}

					if ((i < 0) || (i > this.launchedItems.size()))
						break;
				}
			}
			else
			{
				if (this.launchCycle == this.frenzyLaunchCycle)
					this.frenzyModeForHearts = true;
				else
				{
					this.frenzyModeForHearts = false;
				}

				this.launchCycle += 1;

				if ((this.launchTimer.getElapsedTimeSecs() > 5L)
						&& (this.model.getHearts() > 0))
				{
					launchSomeFruits();
					this.launchTimer.stop();
					this.launchTimer.start();
				}
				else if (this.model.getHearts() <= 0)
				{
					ArrayList values = new ArrayList();
					values.add(Integer.valueOf(this.model.getScore()));
					values.add(Integer.valueOf(this.model
							.getNumberOfFruitsCutInRow()));
					values.add(Integer.valueOf(this.model
							.getNumberOfFruitsCut()));

					this.mtApp.invokeLater(new SceneChanger(this.mtApp,
							new HighScoresKeyboardScene(this.mtApp,
									"High Scores Entry", values, this.gameMode,
									this.textColor)));
				}

			}

			if ((this.timer.getPause() > 0L)
					&& (System.currentTimeMillis() - this.timer.getPause() >= 1500L))
			{
				this.timer.resume();
			}
			if ((this.timer.getSubTimerStart() > 0L)
					&& (System.currentTimeMillis()
							- this.timer.getSubTimerStart() >= 3000L))
			{
				this.model.setDoubleScore(false);
				this.timer.subTimerEnd();
			}

			super.drawAndUpdate(graphics, timeDelta);
		}
		this.previousTime = this.currentTime;
	}

	private void loadBG()
	{
		File backgroundFolder = new File(this.backgroundPath);

		MTBackgroundImage bkgImg = new MTBackgroundImage(
				this.mtApp,
				this.mtApp.loadImage(this.imagesPath + "background"
						+ MTApplication.separator + backgroundFolder.list()[0]),
				false);
		bkgImg.setPositionGlobal(new Vector3D(this.mtApp.width / 2,
				this.mtApp.height / 2, -this.mtApp.height / 5));
		bkgImg.scale(1.25F, 1.25F, 1.0F, bkgImg.getCenterPointGlobal());
		getCanvas().addChild(bkgImg);
	}

	private void loadObservers()
	{
		this.heartObserver = new HeartObserver(this.mtApp, this.gameCanvas);

		this.model.addHeartObserver(this.heartObserver);

		this.scoreObserver = new ScoreObserver(this.mtApp, this.gameCanvas,
				this.textColor);

		this.model.addScoreObserver(this.scoreObserver);

		this.fruitCutInRowObserver = new FruitCutInRowObserver(this.mtApp,
				this.gameCanvas, this.textColor);

		this.model.addFruitCutInRowObserver(this.fruitCutInRowObserver);

		this.levelObserver = new LevelObserver(this.mtApp, this.gameCanvas,
				this.textColor);

		this.model.addLevelObserver(this.levelObserver);

		createFruitCounter();
	}

	private void animateExplosion()
	{
		MTColor white = new MTColor(255.0F, 255.0F, 255.0F, 255.0F);

		MTRectangle boom = new MTRectangle(this.mtApp.height + 20, -10.0F, -10.0F, 0.0F,
				this.mtApp.width + 20, this.mtApp);
		boom.setFillColor(white);
		boom.removeAllGestureEventListeners();
		this.gameCanvas.addChild(boom);

		Fader bearClawFader = new Fader(boom, boom.getFillColor());
		this.mtApp.invokeLater(bearClawFader);
	}

	private void eraseShape(KatanaObject launchedFruit)
	{
		MTPolygon shape = (MTPolygon) launchedFruit.getComponent();

		Fader fader = new Fader(shape, shape.getFillColor());
		this.mtApp.invokeLater(fader);
	}

	private void dropSpheres(KatanaObject launchedFruit, Cut cut)
	{
		PImage dropTexture = launchedFruit.getTexture();

		AbstractShape fruitComponent = (AbstractShape) launchedFruit
				.getComponent();

		fruitComponent.scale(0.5F, 0.5F, 0.5F,
				fruitComponent.getCenterPointLocal());

		GeometryInfo gi = new GeometryInfo(this.mtApp, fruitComponent
				.getGeometryInfo().getVertices(), fruitComponent
				.getGeometryInfo().getNormals(), fruitComponent
				.getGeometryInfo().getIndices());

		MTTriangleMesh droppedComponentTwo = new MTTriangleMesh(this.mtApp, gi);
		MTTriangleMesh droppedComponentOne = new MTTriangleMesh(this.mtApp, gi);
		KatanaObject secondObject = new KatanaObject(droppedComponentTwo,
				this.launcher, this.model, ObjectType.RANDOMFRUIT,
				this.launchTimer);

		droppedComponentTwo.scale(0.5F, 0.5F, 0.5F,
				fruitComponent.getCenterPointLocal());
		droppedComponentOne.scale(0.5F, 0.5F, 0.5F,
				fruitComponent.getCenterPointLocal());
		KatanaObject firstObject = new KatanaObject(droppedComponentOne,
				this.launcher, this.model, ObjectType.RANDOMFRUIT,
				this.launchTimer);

		if (cut.getDeltaX() > cut.getDeltaY())
		{
			firstObject.setPosition(new Vector3D(launchedFruit.getPosition().x,
					launchedFruit.getPosition().y,
					launchedFruit.getPosition().z));
			secondObject.setPosition(new Vector3D(
					launchedFruit.getPosition().x,
					launchedFruit.getPosition().y,
					launchedFruit.getPosition().z));

			firstObject.setVelocity(new Vector3D(cut.getDeltaX() / 2.0F
					+ launchedFruit.getVelocity().x, -0.5F
					* launchedFruit.getVelocity().y, 0.0F));
			secondObject.setVelocity(new Vector3D(-1.0F * cut.getDeltaX()
					/ 2.0F + launchedFruit.getVelocity().x, launchedFruit
					.getVelocity().y, 0.0F));
		}
		else
		{
			firstObject.setPosition(new Vector3D(launchedFruit.getPosition().x,
					launchedFruit.getPosition().y,
					launchedFruit.getPosition().z));
			secondObject.setPosition(new Vector3D(
					launchedFruit.getPosition().x,
					launchedFruit.getPosition().y,
					launchedFruit.getPosition().z));

			firstObject.setVelocity(new Vector3D(launchedFruit.getVelocity().x,
					-1.0F * cut.getDeltaY() / 2.0F
							+ launchedFruit.getVelocity().y, 0.0F));
			secondObject.setVelocity(new Vector3D(-1.0F
					* launchedFruit.getVelocity().x, cut.getDeltaY() / 2.0F
					+ launchedFruit.getVelocity().y, 0.0F));
		}

		firstObject.setCut();
		droppedComponentOne.setTexture(dropTexture);
		secondObject.setCut();
		droppedComponentTwo.setTexture(dropTexture);

		droppedComponentOne.removeAllGestureEventListeners();
		droppedComponentTwo.removeAllGestureEventListeners();

		this.gameCanvas.addChild(droppedComponentTwo);
		this.launchedItems.add(secondObject);
		this.gameCanvas.addChild(droppedComponentOne);
		this.launchedItems.add(firstObject);
	}

	public float getElapsedTime()
	{
		return (float) this.timer.getElapsedTimeSecs()
				+ (float) this.timer.getElapsedTime() / 1000.0F;
	}
}