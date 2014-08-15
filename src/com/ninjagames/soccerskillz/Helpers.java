package com.ninjagames.soccerskillz;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.ninjagames.soccerskillz.GameScene.PhysicsBodyType;

public class Helpers {
	
	public static Vector2 PhysicsMove(float x, float y) {
		return new Vector2(x/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				y/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	}
	
	public static PhysicsBodyType getPhysicsBodyTypeA(Contact pContact) {
		return (PhysicsBodyType) pContact.getFixtureA().getUserData();
	}
	
	public static PhysicsBodyType getPhysicsBodyTypeB(Contact pContact) {
		return (PhysicsBodyType) pContact.getFixtureB().getUserData();
	}
	
	public static boolean physicsCollides(
			Contact pContact, PhysicsBodyType pTypeA, PhysicsBodyType pTypeB) {
		Object userDataA = pContact.getFixtureA().getBody().getUserData();
		Object userDataB = pContact.getFixtureB().getBody().getUserData();
		/*if (userDataB != null) {
			Debug.d(userDataB.toString());
		}
		if (userDataA != null) {
			Debug.d(userDataA.toString());
		}*/
		//	Debug.d(userDataB.toString());
		//}
		return (userDataA != null && userDataB != null &&
				(((PhysicsBodyType) userDataA == pTypeA && (PhysicsBodyType) userDataB == pTypeB) ||
				((PhysicsBodyType) userDataA == pTypeB && (PhysicsBodyType) userDataB == pTypeA)));
	}
}
