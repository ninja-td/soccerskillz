package com.ninjagames.soccerskillz;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameScene extends Scene implements IOnSceneTouchListener {
	
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 800;
	public static final int GROUND_HEIGHT = 780;
	public static final int MENU_X = 150;
	public static final int MENU_Y = 100;


	
	private ResourceManager mResourceManager;
	private Sprite mBall;
	private Sprite mShadow;
	private Body mBallBody;
	private PhysicsWorld mPhysicsWorld;
	private Text mScoreText;
	private Text mHighScoreText;
	private int mScore = 0;
	private GameState mGameState = GameState.STARTING;
	private IngameMenu mIngameMenu;
	private SpikeTouchHandler mSpikeTouchHandler;
	private Energy mRightLegEnergy;
	private Energy mLeftLegEnergy;
	private HUD mHud;
	private GameData mGameData;
	
	public enum PhysicsBodyType {
		BALL,
		FLOOR
	}
	
	public enum GameState {
		STARTING,
		RUNNING,
		PAUSED,
		FINISHED
	}
	
	// Constructor.
	public GameScene(ResourceManager pResourceManager) {
		mResourceManager = pResourceManager;
		Debug.d("testing");
		//mBall = new Sprite(0, 0, mResourceManager.mBallRegion, mResourceManager.getVBOM());
		//setBackground(new Background(Color.BLACK));
		setBackground(new SpriteBackground(new Sprite(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT,
				mResourceManager.mGroundRegion, mResourceManager.getVBOM())));
		mSpikeTouchHandler = new SpikeTouchHandler(this.mResourceManager);
		mGameData = new GameData(this.mResourceManager, mResourceManager.getGoogleGameHelper());
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		populateScene();
	}
	
	public void populateScene() {
		Debug.d("Game Scene Created.");
		mShadow = new Sprite(100, 0, mResourceManager.mShadowRegion, mResourceManager.getVBOM());
		attachChild(mShadow);
		mBall = new Sprite(100, 10, mResourceManager.mBallRegion, mResourceManager.getVBOM()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				if (mGameState == GameState.RUNNING || mGameState == GameState.STARTING) {
					
					//mSpikeTouchHandler.kick(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), mBall, mBallBody);
					if (mGameState == GameState.STARTING) {
						mGameState = GameState.RUNNING;
					}
					/*if (pSceneTouchEvent.isActionDown()) {
						mLastTouchDown = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					}
					if (pSceneTouchEvent.isActionUp()) {
						
						Vector2 newPos = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						spikeBall(newPos.sub(mLastTouchDown));
					}*/
				}
				return true;
			}
		};
		mBall.setScale(.6f);
		this.setOnSceneTouchListener(this);
		createPhysics();
		attachChild(mBall);
		
		mRightLegEnergy = new Energy(mResourceManager);
		mRightLegEnergy.setPosition(SCREEN_WIDTH - 60, 20);
		mRightLegEnergy.setScale(0.6f);
		mRightLegEnergy.setAlpha(0.5f);

		mLeftLegEnergy = new Energy(mResourceManager, true);
		mLeftLegEnergy.setPosition(15, 20);
		mLeftLegEnergy.setScale(0.6f);
		mLeftLegEnergy.setAlpha(0.5f);
		
		mScoreText = new Text(SCREEN_WIDTH/2, 20, mResourceManager.mFont, "0123456789",
				new TextOptions(HorizontalAlign.CENTER), mResourceManager.getVBOM());
		mScoreText.setText("0");
		attachChild(mScoreText);
		
		mHighScoreText = new Text(150, 20, mResourceManager.mFont, "0123456789",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mHighScoreText.setText(String.valueOf(mGameData.getHighScore()));
		//attachChild(mHighScoreText);
		
		// Attach the in-game menu entity last so that it stays on top.
		mIngameMenu = new IngameMenu(mResourceManager, this, mGameData);
		mIngameMenu.setPosition(SCREEN_WIDTH, MENU_Y);		
		
		//attachChild(mRightLegEnergy);
		mHud = new HUD();
		mHud.attachChild(mRightLegEnergy);
		mHud.attachChild(mLeftLegEnergy);
		
		mHud.attachChild(mIngameMenu);
				
		mResourceManager.getCamera().setHUD(mHud);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mGameState == GameState.RUNNING || mGameState == GameState.STARTING) {

			if (pSceneTouchEvent.isActionDown()) {
				mSpikeTouchHandler.startKick(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			}
			if (pSceneTouchEvent.isActionUp()) {
				if (mGameState == GameState.STARTING) {
					mGameState = GameState.RUNNING;
				}
				if (mBall.getX() <= (SCREEN_WIDTH/2 - mBall.getWidth()/2)) {
					mSpikeTouchHandler.kick(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(),
							mBallBody, mLeftLegEnergy);
				} else {
					mSpikeTouchHandler.kick(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(),
							mBallBody, mRightLegEnergy);
				}
				increaseScore();
				//mSpikeTouchHandler.endKick();
			}
		}
		return true;
	}
	
	@Override
	public void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		updateBallSHadow();
	}
	
	public void restartGame() {
		mResourceManager.getCamera().setHUD(mHud);
		mGameState = GameState.STARTING;
		mBall.setPosition(100, 100);
		updateScore(0);
		updateHighScore();
		mRightLegEnergy.setEnergyPercentage(100);
		mLeftLegEnergy.setEnergyPercentage(100);
		removeInGameMenu();
	}
	
	public void gameOver() {
		mGameState = GameState.FINISHED;
		mSpikeTouchHandler.endKick();
		mGameData.setScore(mScore);
		mRightLegEnergy.setEnergyPercentage(100);
		mLeftLegEnergy.setEnergyPercentage(100);
		// Play sounds.
		mResourceManager.mGameOverSound.play();
		showInGameMenu();
	}

	// PRIVATE FUNCTIONS.
	private void updateBallSHadow() {
		mShadow.setX(mBall.getX());
		mShadow.setY(GROUND_HEIGHT - (GROUND_HEIGHT - mBall.getY() - mBall.getHeight()) *
				.1f - mShadow.getHeight()/2);
		
	}	
	
	private void removeInGameMenu() {
		mIngameMenu.registerEntityModifier(new MoveXModifier(0.3f, MENU_X, SCREEN_WIDTH));
	}
	
	private void showInGameMenu() {
		mIngameMenu.updateData();
		mIngameMenu.registerEntityModifier(new MoveXModifier(0.3f, SCREEN_WIDTH, MENU_X));
	}
/*
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionUp()) {
			//mBallBody.setTransform(Helpers.PhysicsMove(100, 10), 0);
			//mBallBody.applyForceToCenter(new Vector2(0f, -6000f));
		}
		//mBallBody.setTransform(10,10, 0);
		//Debug.d("touched");
		return true;
	}
	*/
	private void createPhysics() {
		mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 40f), false);
		registerUpdateHandler(mPhysicsWorld);
		final FixtureDef ballFixtureDef = PhysicsFactory.createFixtureDef(1, .7f, .01f);
		mBallBody = PhysicsFactory.createCircleBody(
				mPhysicsWorld, mBall, BodyType.DynamicBody, ballFixtureDef);
		mBallBody.setUserData(PhysicsBodyType.BALL);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mBall, mBallBody, true, true));
		createWalls();
		createPhysicsContactListeners();
		//mBall.registerUpdateHandler(mPhysicsWorld);

	}
	
	private void createPhysicsContactListeners() {
		mPhysicsWorld.setContactListener(new ContactListener() {
			@Override
			public void beginContact(final Contact pContact) {
				//Debug.d("pContact");

				if (Helpers.physicsCollides(pContact, PhysicsBodyType.BALL, PhysicsBodyType.FLOOR)) {
					if (mGameState == GameState.RUNNING) {
						gameOver();
					}
					Debug.d("ball drop" + "in gamestate " + mGameState);
				}
			}
			@Override
			public void endContact(final Contact pContact) {
			}
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void createWalls() {
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        Body floor = PhysicsFactory.createBoxBody(
        		mPhysicsWorld, 0, GROUND_HEIGHT, SCREEN_WIDTH*2, 2, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, 0, 0, SCREEN_WIDTH*2, 2, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, 0, 0, 0, SCREEN_HEIGHT*2, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, SCREEN_WIDTH, 0, 2, SCREEN_HEIGHT*2, BodyType.StaticBody, wallFixtureDef);
        floor.setUserData(PhysicsBodyType.FLOOR);
	}
	
	private void increaseScore() {
		updateScore(mScore + 1);
	}

	private void updateScore(int newScore) {
		mScore = newScore;
		mScoreText.setText(Integer.toString(mScore));
	}
	
	private void updateHighScore() {
		mHighScoreText.setText(String.valueOf(mGameData.getHighScore()));
	}
}
