package com.ninjagames.soccerskillz;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

public class IngameMenu extends Entity{
	
	private ResourceManager mResourceManager;
	private Text mGameOverText;
	private Text mScoreText;
	private Text mHighScoreText;
	private Sprite mPlayButton;
	private Sprite mBackGround;
	private GameScene mGameScene;
	private GameData mGameData;
	
	public IngameMenu(ResourceManager pResourceManager, GameScene pGameScene, GameData pGameData) {
		mResourceManager = pResourceManager;
		mGameScene = pGameScene;
		mGameData = pGameData;
		mBackGround = new Sprite(0,0, mResourceManager.mClipboardRegion, mResourceManager.getVBOM());
		mBackGround.setScaleCenter(0, 0);
		mBackGround.setScale(1.5f);
		attachChild(mBackGround);
		
		mGameOverText = new Text(50, 80, mResourceManager.mFont, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mGameOverText.setText("GAME OVER!!");
		mGameOverText.setColor(Color.BLACK);
		attachChild(mGameOverText);
		
		mScoreText = new Text(50, 150, mResourceManager.mFont, "YourScore: 0123456789",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mScoreText.setColor(0.5f, 0.5f, 0.5f);
		this.attachChild(mScoreText);
		mHighScoreText = new Text(50, 230, mResourceManager.mFont, "HighScore: 0123456789",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mHighScoreText.setColor(0.5f, 0.5f, 0.5f);
		this.attachChild(mHighScoreText);
		
		// The play button.
		mPlayButton = new Sprite(75, 300, mResourceManager.mPlayButtonRegion, mResourceManager.getVBOM()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				setScale(1);
				if (pSceneTouchEvent.isActionDown()) {
					setScale(1.1f);
				}
				if (pSceneTouchEvent.isActionUp()) {
					mGameScene.restartGame();
				}
				Debug.d("button touched");
				return true;
			}
		};
		attachChild(mPlayButton);
		mGameScene.registerTouchArea(mPlayButton);
		
	}
	
	public void updateData() {
		mScoreText.setText("Your Score: " + String.valueOf(mGameData.getLastScore()));
		mHighScoreText.setText("HighScore: " + String.valueOf(mGameData.getHighScore()));
	}
}
