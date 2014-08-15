package com.ninjagames.soccerskillz;

import org.andengine.entity.scene.Scene;
import org.andengine.util.debug.Debug;

public class SceneManager {
	
	public enum SceneType {
		SCENE_GAME,
		SCENE_SPLASH,
		SCENE_INSTRUCTIONS
	}
	private GameScene mGameScene;
	private SplashScene mSplashScene;
	private InstructionsScene mInstructionsScene;
	private ResourceManager mResourceManager;
	private SceneType currentScene;
	
	public SceneManager(ResourceManager pResourceManager) {
		mResourceManager = pResourceManager;
	}
	
	public Scene getScene(SceneType pSceneType) {
		switch (pSceneType) {
			case SCENE_GAME:
				if (mGameScene == null) {
					mGameScene = new GameScene(mResourceManager);
				}
				Debug.d("Setting GameScene ");
				return mGameScene;
			case SCENE_SPLASH:
				if (mSplashScene == null) {
					mSplashScene = new SplashScene(mResourceManager, this);
				}
				return mSplashScene;
			case SCENE_INSTRUCTIONS:
				if (mInstructionsScene == null) {
					mInstructionsScene = new InstructionsScene(mResourceManager, this);
				}
				return mInstructionsScene;
			default:
				return null;
		}
	}
	
	public void setScene(SceneType pSceneType) {
		currentScene = pSceneType;
		mResourceManager.mGameActivity.getEngine().setScene(getScene(pSceneType));
	}
	
	public SceneType getSceneType() {
		return currentScene;
	}
}
