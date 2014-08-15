package com.ninjagames.soccerskillz;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.google.example.games.basegameutils.GameHelper;

import android.graphics.Color;

public class ResourceManager {
	
	// Game Textures.
	public ITextureRegion mBallRegion;
	public ITextureRegion mGroundRegion;
	public ITextureRegion mShadowRegion;
	public ITextureRegion mClipboardRegion;
	public ITextureRegion mPlayButtonRegion;
	public ITextureRegion mLegRegion;
	public ITextureRegion mLegMaskRegion;
    public IFont mFont;
    
    // Splash Screen Textures.
    public ITiledTextureRegion mNinjaSpriteRegion;
    public ITextureRegion mNinjaGamesLogoRegion;
    public ITextureRegion mNinjaStarRegion;
    
    // Game Sounds.
    public Sound mBallHitSound;
    public Sound mGameOverSound;
    
    public GameActivity mGameActivity;
    
	private VertexBufferObjectManager mVBOM;
	private Camera mCamera;
	private GameHelper mGameHelper;
	
	private TextureManager mTextureManager;
	
	private BitmapTextureAtlas mFontTextureAtlas;
	private BuildableBitmapTextureAtlas mGameTextureAtlas;
	private BuildableBitmapTextureAtlas mGameTextureAtlasWithAlpha;
	
	private BuildableBitmapTextureAtlas mSplashTextureAtlas;
	
	
	public ResourceManager(GameActivity pGameActivity, Camera pCamera, GameHelper pGameHelper) {
		mGameActivity = pGameActivity;
		mTextureManager = mGameActivity.getTextureManager();
		mVBOM = mGameActivity.getVertexBufferObjectManager();
		mCamera = pCamera;
		this.mGameHelper = pGameHelper;
	}
	
	public Camera getCamera() {
		return mCamera;
	}
	
	public GameHelper getGoogleGameHelper() {
		return mGameHelper;
	}
	
	public VertexBufferObjectManager getVBOM() {
		return mVBOM;
	}
	
	public void loadGameResoures() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mGameTextureAtlas = new BuildableBitmapTextureAtlas(
				mTextureManager, 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBallRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameTextureAtlas, mGameActivity, "ball.png");
		
		mGroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameTextureAtlas, mGameActivity, "stadium2.png");
		
		mShadowRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameTextureAtlas, mGameActivity, "shadow.png");
		
		mClipboardRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameTextureAtlas, mGameActivity, "clipboard_only.png");
		
		mPlayButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameTextureAtlas, mGameActivity, "retry_button.png");
		
		mLegRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameTextureAtlas, mGameActivity, "foot2_mask.png");
		
		try {
			mGameTextureAtlas.build(
					new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
					BitmapTextureAtlas>(0, 1, 0));
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mGameTextureAtlas.load();
		
		///////////////////////
		// TEXTURES WITH ALPHA.
		///////////////////////
		mGameTextureAtlasWithAlpha = new BuildableBitmapTextureAtlas(
				mTextureManager, 1024, 1024, TextureOptions.DEFAULT);
		mLegMaskRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameTextureAtlasWithAlpha, mGameActivity, "foot2_bar.png");
		try {
			mGameTextureAtlasWithAlpha.build(
					new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
					BitmapTextureAtlas>(0, 1, 0));
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mGameTextureAtlasWithAlpha.load();
		loadGameFonts();
		loadAudio();
		
	}
	
	public void loadSplashResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mSplashTextureAtlas = new BuildableBitmapTextureAtlas(
				mTextureManager, 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mNinjaSpriteRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				mSplashTextureAtlas, mGameActivity, "ninja.png", 7, 1);
		mNinjaStarRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mSplashTextureAtlas, mGameActivity, "star.png");
		mNinjaGamesLogoRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mSplashTextureAtlas, mGameActivity, "ninjagames.png");
		try {
			mSplashTextureAtlas.build(
					new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
					BitmapTextureAtlas>(0, 1, 0));
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mSplashTextureAtlas.load();
	}
	
    private void loadGameFonts()
    {
    	FontFactory.setAssetBasePath("gfx/");
    	mFontTextureAtlas = new BitmapTextureAtlas(mTextureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	mFont = FontFactory.createFromAsset(mGameActivity.getFontManager(), mFontTextureAtlas,
    			mGameActivity.getAssets(), "ChalkDust.TTF", 26, true, Color.WHITE);
    	mFontTextureAtlas.load();
		mFont.load();
    }
    
    private void loadAudio() {
        SoundFactory.setAssetBasePath("mfx/");
        try {
        	mBallHitSound = SoundFactory.createSoundFromAsset(
        			mGameActivity.getEngine().getSoundManager(),
        			mGameActivity, "ball_hit.wav");
        	mBallHitSound.setVolume(0.5f);
        	mGameOverSound = SoundFactory.createSoundFromAsset(
        			mGameActivity.getEngine().getSoundManager(),
        			mGameActivity, "game_over.wav");
        	mGameOverSound.setVolume(0.7f);
        } catch (final IOException e) {
                Debug.e("Error", e);
        }
    }
}
