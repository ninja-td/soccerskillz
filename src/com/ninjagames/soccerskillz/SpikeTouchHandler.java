package com.ninjagames.soccerskillz;

import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SpikeTouchHandler {
	private boolean mIsKicking = false;
	private Vector2 mKickStartPos;
	private Vector2 mEndPos;
	private ResourceManager mResourceManager;
	
	public SpikeTouchHandler(ResourceManager pResourceManager) {
		mKickStartPos = new Vector2();
		mEndPos = new Vector2();
		mResourceManager = pResourceManager;
	}
	
	public void startKick(float pX, float pY) {
		mKickStartPos.set(pX, pY);
		mIsKicking = true;
		Debug.d("kick Starting");
	}
	
	public void endKick() {
		mIsKicking = false;
		Debug.d("kick ending");
	}
	
	public void kick(float pX, float pY, Body pBody, Energy pEnergy) {
		Debug.d("kicking");
		if (!mIsKicking) {
			return;
		}
		mIsKicking = false;
		mEndPos.set(pX, pY);
		mResourceManager.mBallHitSound.play();
		Vector2 force = mEndPos.sub(mKickStartPos);
		//Debug.d("Ball speed" + String.valueOf(pBody.getLinearVelocity().len()));
		float multiplier = force.len();
		if (multiplier < 1) {
			return;
		}
		//Debug.d("kick power" + Float.toString(multiplier));
		multiplier = pEnergy.getEnergy(multiplier)/multiplier;
		//Debug.d("kick multiplier" + Float.toString(multiplier));
		force.mul(GameParams.KICK_MULTIPLIER *
				(1 + GameParams.REBOUND_MULTIPLIER * pBody.getLinearVelocity().len()) *
				multiplier);
		//force.mul(1 + force.dot(pBody.getLinearVelocity().mul(-.001f)));
		//Vector2 forcePoint = new Vector2(mEndPos).sub(pEntity.getX(), pEntity.getY());
		//Vector2 forcePoint = new Vector2(pEntity.
		
		pBody.applyLinearImpulse(force, pBody.getWorldCenter());
		//pBody.applyForce(force, forcePoint);
	}
}
