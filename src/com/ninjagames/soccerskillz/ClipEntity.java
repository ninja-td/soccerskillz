package com.ninjagames.soccerskillz;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import android.opengl.GLES20;

public class ClipEntity extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	protected float mHeight;
	protected float mWidth;
	
	private boolean mClippingEnabled = true;
	private TouchEvent mDummyTouchEvent = new TouchEvent();
	static final float[] VERTICES_TMP = new float[2];

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClipEntity() {
		super();
	}

	public ClipEntity(final float pX, final float pY) {
		super(pX, pY);
	}

	public ClipEntity(final float pX, final float pY, final float pWidth, final float pHeight) {
		super(pX, pY);
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean getClippingEnabled() {
		return this.mClippingEnabled;
	}

	public void setClippingEnabled(final boolean pClippingEnabled) {
		this.mClippingEnabled = pClippingEnabled;
	}
	
	public void setWidth(final float pWidth) {
		this.mWidth = pWidth;
	}
	
	public float getWidth() {
		return this.mWidth;
	}
	
	public void setHeight(final float pHeight) {
		this.mHeight = pHeight;
	}
	
	public float getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onManagedDraw(GLState pGLState, Camera pCamera) {
		if (this.mClippingEnabled) {
			/* Enable scissor test, while remembering previous state. */
			final boolean wasScissorTestEnabled = pGLState.enableScissorTest();

			final int surfaceHeight = pCamera.getSurfaceHeight();

			/* In order to apply clipping, we need to determine the the axis aligned bounds in OpenGL coordinates. */

			/* Determine clipping coordinates of each corner in surface coordinates. */
			final float[] lowerLeftSurfaceCoordinates = this.getSurfaceCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(0, 0), pCamera);
			final int lowerLeftX = Math.round(lowerLeftSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
			final int lowerLeftY = surfaceHeight - Math.round(lowerLeftSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);

			final float[] upperLeftSurfaceCoordinates = this.getSurfaceCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(0, this.mHeight), pCamera);
			final int upperLeftX = Math.round(upperLeftSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
			final int upperLeftY = surfaceHeight - Math.round(upperLeftSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);

			final float[] upperRightSurfaceCoordinates = this.getSurfaceCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(this.mWidth, this.mHeight), pCamera);
			final int upperRightX = Math.round(upperRightSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
			final int upperRightY = surfaceHeight - Math.round(upperRightSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);

			final float[] lowerRightSurfaceCoordinates = this.getSurfaceCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(this.mWidth, 0), pCamera);
			final int lowerRightX = Math.round(lowerRightSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
			final int lowerRightY = surfaceHeight - Math.round(lowerRightSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);
			/* Determine minimum and maximum x clipping coordinates. */
			final int minClippingX = min(lowerLeftX, upperLeftX, upperRightX, lowerRightX);
			final int maxClippingX = max(lowerLeftX, upperLeftX, upperRightX, lowerRightX);

			/* Determine minimum and maximum y clipping coordinates. */
			final int minClippingY = min(lowerLeftY, upperLeftY, upperRightY, lowerRightY);
			final int maxClippingY = max(lowerLeftY, upperLeftY, upperRightY, lowerRightY);

			/* Determine clipping width and height. */
			final int clippingWidth = maxClippingX - minClippingX;
			final int clippingHeight = maxClippingY - minClippingY;
			/* Finally apply the clipping. */
			GLES20.glScissor(minClippingX, minClippingY, clippingWidth, clippingHeight);

			/* Draw children, etc... */
			super.onManagedDraw(pGLState, pCamera);

			/* Revert scissor test to previous state. */
			pGLState.setScissorTestEnabled(wasScissorTestEnabled);
		} else {
			super.onManagedDraw(pGLState, pCamera);
		}
	}
	
	private int min(int a, int b, int c, int d) {
		return (Math.min(a, Math.min(b, Math.min(c, d))));
	}
	
	private int max(int a, int b, int c, int d) {
		return (Math.max(a, Math.max(b, Math.max(c, d))));
	}
	
	private float[] getSurfaceCoordinatesFromSceneCoordinates(final float[] pSceneCoordinates, Camera pCamera) {
		mDummyTouchEvent.set(pSceneCoordinates[0], pSceneCoordinates[1]);
		pCamera.convertSceneToSurfaceTouchEvent(mDummyTouchEvent,
				pCamera.getSurfaceWidth(), pCamera.getSurfaceHeight());
		VERTICES_TMP[0] = mDummyTouchEvent.getX();
		VERTICES_TMP[1] = mDummyTouchEvent.getY();
		return VERTICES_TMP;
	}
}