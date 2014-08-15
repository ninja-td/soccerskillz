package com.ninjagames.soccerskillz;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.content.Intent;
import android.os.Bundle;

import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class GameActivity extends BaseGameActivity{
	
	private Camera mCamera;
	private ResourceManager mResourceManager;
	private SceneManager mSceneManager;
	private GameHelper mGameHelper;
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, GameScene.SCREEN_WIDTH, GameScene.SCREEN_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(
				true,
				ScreenOrientation.PORTRAIT_FIXED,
				new FillResolutionPolicy(),
				this.mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		Debug.setDebugLevel(Debug.DebugLevel.NONE);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		mResourceManager = new ResourceManager(this, mCamera, mGameHelper);
		mResourceManager.loadSplashResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mSceneManager = new SceneManager(mResourceManager);
		pOnCreateSceneCallback.onCreateSceneFinished(
				mSceneManager.getScene(SceneManager.SceneType.SCENE_SPLASH));
		//mSceneManager.PopulateGameScene();

	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		// TODO Auto-generated method stub
		// mSceneManager.PopulateGameScene();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public void onBackPressed() {
		if (mSceneManager.getSceneType() == SceneManager.SceneType.SCENE_GAME) {
			mSceneManager.setScene(SceneManager.SceneType.SCENE_INSTRUCTIONS);
			((InstructionsScene)mSceneManager.getScene(
					SceneManager.SceneType.SCENE_INSTRUCTIONS)).initializeEntites();
		} else {
			System.exit(0);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		// create game helper with all APIs (Games, Plus, AppState):
	    mGameHelper = new GameHelper(this, GameHelper.CLIENT_PLUS | GameHelper.CLIENT_GAMES);
	    mGameHelper.enableDebugLog(true);


	    GameHelperListener listener = new GameHelper.GameHelperListener() {
	        @Override
	        public void onSignInSucceeded() {
	            // handle sign-in succeess
	        	Debug.d("logged in");
	        }
	        @Override
	        public void onSignInFailed() {
	            // handle sign-in failure (e.g. show Sign In button)
	        	Debug.e("Not logged in");
	        }

	    };
	    mGameHelper.setup(listener);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();

	    mGameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
	    super.onStop();
	    mGameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
	    super.onActivityResult(request, response, data);
	    mGameHelper.onActivityResult(request, response, data);
	}


}
