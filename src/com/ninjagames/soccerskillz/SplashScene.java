package com.ninjagames.soccerskillz;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

public class SplashScene extends Scene {
	
	private ResourceManager mResourceManager;
	private SceneManager mSceneManager;
	private AnimatedSprite mNinja;
	private Rectangle mBackRect;
	private Sprite mStar;
	private Sprite mNinjaGamesLogo;
	
	public SplashScene(ResourceManager pResourceManager, SceneManager pSceneManager) {
		this.mResourceManager = pResourceManager;
		this.mSceneManager = pSceneManager;
		setBackground(new Background(Color.WHITE));
		loadEntities();
		initializeEntites();
	}

	public void initializeEntites() {
		// Scene opens
		mBackRect.setAlpha(1);
		mBackRect.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(1f),
				new AlphaModifier(2, 1, 0)));
		
		mNinja.setPosition(-100, 600);
		mNinja.setScale(3);
		mNinja.animate(new long[] {400, 100});
		mNinja.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(3) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						pItem.setPosition(100, 600);
						
					}
				},
				new DelayModifier(2) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						mNinja.animate(new long[] {0, 0, 100, 100, 100, 100, 100});
						mResourceManager.loadGameResoures();
					}
				},
				new MoveModifier(4, 100, 600, 600, 600),
				new DelayModifier(2) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						Debug.d("Splash Ends");
						mSceneManager.setScene(SceneManager.SceneType.SCENE_INSTRUCTIONS);
					}
				}));
		
		mStar.setPosition(-100, 610);
		mStar.setScale(2);
		mStar.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(5),
				new MoveModifier(4.2f, 0, 600, 600, 600)));
		
		mNinjaGamesLogo.setPosition(-300, 600);
		mNinjaGamesLogo.setScale(3.5f);
		mNinjaGamesLogo.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(5.3f),
				new MoveModifier(3.5f, -100, 200, 600, 600)));
		// Scene Closes.
		mBackRect.setAlpha(0);
		mBackRect.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(9f),
				new AlphaModifier(1, 0, 1)));
	}
	
	private void loadEntities() {
		mNinja = new AnimatedSprite(0,0, mResourceManager.mNinjaSpriteRegion, mResourceManager.getVBOM());
		this.attachChild(mNinja);
		mStar = new Sprite(0, 0, mResourceManager.mNinjaStarRegion,
				mResourceManager.getVBOM());
		this.attachChild(mStar);
		mNinjaGamesLogo = new Sprite(0, 0, mResourceManager.mNinjaGamesLogoRegion,
				mResourceManager.getVBOM());
		this.attachChild(mNinjaGamesLogo);
		mBackRect = new Rectangle(0,  0, GameScene.SCREEN_WIDTH, GameScene.SCREEN_HEIGHT,
				mResourceManager.getVBOM());
		mBackRect.setColor(0,0,0);
		this.attachChild(mBackRect);
	}
}
