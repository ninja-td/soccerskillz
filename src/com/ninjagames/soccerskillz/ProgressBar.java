package com.ninjagames.soccerskillz;

import org.andengine.entity.sprite.Sprite;

public class ProgressBar extends ClipEntity {
	private ResourceManager mResourceManager;
	private Sprite mBar;
	public ProgressBar(final float pX, final float pY, final float pWidth, final float pHeight, ResourceManager pResourceManager) {
		super(pX, pY, pWidth, pHeight);
		mResourceManager = pResourceManager;
		mBar = new Sprite(0, 0, mResourceManager.mBallRegion, mResourceManager.getVBOM());
		this.attachChild(mBar);
	}
	
}
