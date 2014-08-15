package com.ninjagames.soccerskillz;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

public class InstructionsScene extends Scene {
	
	private ResourceManager mResourceManager;
	private SceneManager mSceneManager;
	private Rectangle mBackRect;
	private Text mStep1Text;
	private Text mStep2Text;
	private Text mStep3Text;
	private Text mStep4Text;
	private Text mContinueText;
	
	public InstructionsScene(ResourceManager pResourceManager, SceneManager pSceneManager) {
		this.mResourceManager = pResourceManager;
		this.mSceneManager = pSceneManager;
		setBackground(new Background(Color.WHITE));
		loadEntities();
		initializeEntites();
	}
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		mSceneManager.setScene(SceneManager.SceneType.SCENE_GAME);
		((GameScene) mSceneManager.getScene(
				SceneManager.SceneType.SCENE_GAME)).restartGame();
		return false;
	}

	public void initializeEntites() {
		mResourceManager.getCamera().setHUD(null);
		// Scene opens
		mBackRect.setAlpha(1);
		mBackRect.registerEntityModifier(new SequenceEntityModifier(
				new AlphaModifier(1, 1, 0)));
		
	}
	
	private void loadEntities() {
		mStep1Text = new Text(20, 50, mResourceManager.mFont, "Swipe to dribble",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mStep1Text.setColor(Color.BLACK);
		this.attachChild(mStep1Text);
		mStep2Text = new Text(100, 220, mResourceManager.mFont, "Give direction and power",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mStep2Text.setColor(Color.BLACK);
		this.attachChild(mStep2Text);
		mStep3Text = new Text(20, 430, mResourceManager.mFont, "Dont let the ball fall.",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mStep3Text.setColor(Color.BLACK);
		this.attachChild(mStep3Text);
		mStep4Text = new Text(60, 620, mResourceManager.mFont, "And ya.. Keep an eye on power.",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mStep4Text.setColor(Color.BLACK);
		this.attachChild(mStep4Text);
		mContinueText = new Text(250, 740, mResourceManager.mFont, "Get Started",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mContinueText.setColor(Color.BLACK);
		this.registerTouchArea(mContinueText);
		this.attachChild(mContinueText);
		mBackRect = new Rectangle(0,  0, GameScene.SCREEN_WIDTH, GameScene.SCREEN_HEIGHT,
				mResourceManager.getVBOM());
		mBackRect.setColor(Color.BLACK);
		this.attachChild(mBackRect);
	}
}
