package com.ninjagames.soccerskillz;

import org.andengine.entity.Entity;

/**
 * This will take care of the energy required to kick stuff.
 * We should be able to kick stuff only if we have energy left.
 * Energy will regenerate over time.
 * @author Tanmay
 *
 */
public class Energy extends Entity{
	private final float MAX_ENERGY = GameParams.MAX_ENERGY;
	private float mEnergy = GameParams.MAX_ENERGY;
	private final float mRegenerationTime = GameParams.ENERGY_REGENERATION_TIME;
	private float mRegenerationFactor = MAX_ENERGY / mRegenerationTime;
	private ResourceManager mResourceManager;
	private SpriteProgressBar mEnergyBar;
	
	public Energy(ResourceManager pResourceManager) {
		this(pResourceManager, false);
	}
	
	public Energy(ResourceManager pResourceManager, boolean pIsFlipped) {
		mResourceManager = pResourceManager;
		mEnergyBar = new SpriteProgressBar(0, 0,
				mResourceManager, mResourceManager.mLegRegion,
				mResourceManager.mLegMaskRegion, true);
		this.mEnergyBar.setFlippedVertical(true);
		this.mEnergyBar.setRotation(180);
		this.mEnergyBar.setAlpha(0.7f);
		if (!pIsFlipped) {
			this.mEnergyBar.setFlippedHorizontal(true);
		}
		attachChild(this.mEnergyBar);
		/*
		mEnergyLevel = new Text(0, 0, mResourceManager.mFont, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.",
				new TextOptions(HorizontalAlign.LEFT), mResourceManager.getVBOM());
		mOuterRectangle = new Rectangle(0, 0, WIDTH + 4, HEIGHT + 4,
				mResourceManager.getVBOM(), DrawType.STATIC);
		mOuterRectangle.setColor(Color.BLACK);
		mOuterRectangle.setAlpha(.2f);
		attachChild(mOuterRectangle);
		mEnergyRectangle = new Rectangle(2, 2, WIDTH, HEIGHT,
				mResourceManager.getVBOM());
		mEnergyRectangle.setColor(Color.BLUE);
		attachChild(mEnergyRectangle);*/
		updateUI();
		//mEnergyLevel.setText("test");
		//attachChild(mEnergyLevel);
	}
	
	/**
	 * This will return the requested amount of energy. If there is not enough energy
	 * then whatever remains will be returned.
	 * @return
	 */
	public float getEnergy(float pRequestedEnergy) {
		if (pRequestedEnergy >= mEnergy) {
			pRequestedEnergy = mEnergy;
		}
		mEnergy = mEnergy - pRequestedEnergy;
		return pRequestedEnergy;
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		regenerateEnergy(pSecondsElapsed);
	}
	
	private void regenerateEnergy(final float pSecondsElapsed) {
		mEnergy = mEnergy + mRegenerationFactor * pSecondsElapsed;
		if (mEnergy > MAX_ENERGY) {
			mEnergy = MAX_ENERGY;
		}
		updateUI();
	}
	
	public void setEnergyPercentage(float pPercentage) {
		if (pPercentage > 100 || pPercentage < 0) {
			return;
		}
		mEnergy = MAX_ENERGY * pPercentage / 100;
		updateUI();
	}
	
	private void updateUI() {
		this.mEnergyBar.setValuePercentage(this.mEnergy * 100/ this.MAX_ENERGY);
	}
}
