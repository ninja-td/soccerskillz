package com.ninjagames.soccerskillz;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import android.content.Context;
import android.content.SharedPreferences;

public class GameData {
	private int mHighScore = 0;
	private int mLastScore = 0;
	private GameHelper mGameHelper;
	private ResourceManager mResourceManager;
	
	public GameData(ResourceManager pResourceManager, GameHelper pGameHelper) {
		this.mResourceManager = pResourceManager;
		this.mGameHelper = pGameHelper;
		loadData();
	}
	
	public int getHighScore() {
		return mHighScore;
	}
	
	public int getLastScore() {
		return mLastScore;
	}
	
	public void setScore(int pNewScore) {
		mLastScore = pNewScore;
		if(pNewScore > mHighScore) {
			mHighScore = pNewScore;
			
		}
		saveHighScore();
	}
	
	public void saveHighScore() {
		Context context = mResourceManager.mGameActivity;
		SharedPreferences sharedPref = context.getSharedPreferences(
		        "com.ninjagames.soccerskillz", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("highscore", this.mHighScore);
		editor.commit();
		if (mGameHelper.isSignedIn()) {
			Games.Leaderboards.submitScore(
					mGameHelper.getApiClient(),
					"CgkIm4Oq_cMREAIQAA",
					this.mHighScore);
		}
		
	}
	
	private void loadData() {
		Context context = mResourceManager.mGameActivity;
		SharedPreferences sharedPref = context.getSharedPreferences(
		        "com.ninjagames.soccerskillz", Context.MODE_PRIVATE);
		this.mHighScore = sharedPref.getInt("highscore", 0);
	}
}
