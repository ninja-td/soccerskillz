package com.ninjagames.soccerskillz;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

public class SpriteProgressBar extends Entity {
	private Sprite mOverlay;
	private ClipEntity mBar;
	private Sprite mBarSprite;
	private final boolean mIsVertival;
	private ResourceManager mResourceManager;
	private float mValuePercentage;
	
	public SpriteProgressBar(final float pX, final float pY,
			ResourceManager pResourceManager,
			ITextureRegion pOverlayTextureRegion, ITextureRegion pBarTextureRegion,
			boolean pIsVertical) {
		super(pX, pY);
		mIsVertival = pIsVertical;
		
		this.mResourceManager = pResourceManager;
		this.mBarSprite = new Sprite(0, 0, pBarTextureRegion, mResourceManager.getVBOM());
		this.mBar = new ClipEntity(0, 0, this.mBarSprite.getWidth(), this.mBarSprite.getHeight());
		this.mBar.attachChild(mBarSprite);
		this.mOverlay = new Sprite(0, 0, pOverlayTextureRegion, mResourceManager.getVBOM());
		this.attachChild(mBar);
		this.attachChild(mOverlay);
		this.setRotationCenter(this.mOverlay.getWidth()/2, this.mOverlay.getHeight()/2);


		this.setValuePercentage(100);
	}
	
	public void setValuePercentage(float pValuePercentage) {
		if (pValuePercentage < 0 || pValuePercentage > 100) {
			return;
		}
		this.mValuePercentage = pValuePercentage;
		if (this.mIsVertival) {
			this.mBar.setHeight(this.mValuePercentage * this.mOverlay.getHeight() / 100);
			//this.mBar.setY(this.mOverlay.getHeight() - this.mBar.getHeight());
		} else {
			this.mBar.setWidth(this.mValuePercentage * this.mOverlay.getWidth() / 100);
		}
	}

	public float getValuePercentage() {
		return this.mValuePercentage;
	}
	
	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		this.mOverlay.setFlippedHorizontal(pFlippedHorizontal);
		this.mBarSprite.setFlippedHorizontal(pFlippedHorizontal);
	}
	
	public void setFlippedVertical(final boolean pFlippedVertical) {
		this.mOverlay.setFlippedVertical(pFlippedVertical);
		this.mBarSprite.setFlippedVertical(pFlippedVertical);
	}
	
	@Override
	public void setAlpha(final float pAlpha) {
		super.setAlpha(pAlpha);
		this.mOverlay.setAlpha(pAlpha);
		this.mBarSprite.setAlpha(pAlpha);
	}
}
