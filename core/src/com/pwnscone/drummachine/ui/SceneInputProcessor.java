package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pwnscone.drummachine.Game;

public class SceneInputProcessor implements InputProcessor {
	private float mScrollSpeed = 0.5f;
	private float mDecaySpeed = 0.75f;
	private float mMaxZoom = 8.0f;
	private float mMinZoom = 0.2f;
	private OrthographicCamera mCamera;
	private boolean mZeroDown;
	private boolean mOneDown;
	private Vector3 mZeroDownPosition;
	private Vector3 mOneDownPosition;
	private Vector3 mZeroPosition;
	private Vector3 mOnePosition;
	private Vector3 mZeroDownScreenPosition;
	private Vector3 mOneDownScreenPosition;
	private Vector3 mZeroScreenPosition;
	private Vector3 mOneScreenPosition;
	private Vector3 mPinchPosition;
	private Vector3 mPinchDownPosition;
	private Vector2 mVelocity;
	private float mDownZoom;
	private float mZoomVelocity;

	public void create() {
		mCamera = Game.get().getView().getCamera();
		mZeroDown = false;
		mOneDown = false;
		mZeroPosition = new Vector3();
		mOnePosition = new Vector3();
		mZeroDownPosition = new Vector3();
		mOneDownPosition = new Vector3();
		mZeroScreenPosition = new Vector3();
		mOneScreenPosition = new Vector3();
		mZeroDownScreenPosition = new Vector3();
		mOneDownScreenPosition = new Vector3();
		mPinchPosition = new Vector3();
		mPinchDownPosition = new Vector3();
		mVelocity = new Vector2();
		mDownZoom = 1.0f;
		mZoomVelocity = 0.0f;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 touchCoord;
		Vector3 touchDownPos;
		Vector3 touchScreenPos;
		Vector3 touchDownScreenPos;
		Vector3 altTouchPos = null;
		if (pointer == 0) {
			touchCoord = mZeroPosition;
			touchDownPos = mZeroDownPosition;
			touchScreenPos = mZeroScreenPosition;
			touchDownScreenPos = mZeroDownScreenPosition;
			mZeroDown = true;
			if (mOneDown) {
				mOneDownScreenPosition.set(Gdx.input.getX(1), Gdx.input.getY(1), 0.0f);
				mOnePosition.set(mOneDownScreenPosition);
				mCamera.unproject(mOnePosition);
				altTouchPos = mOnePosition;
			}
		} else if (pointer == 1) {
			touchCoord = mOnePosition;
			touchDownPos = mOneDownPosition;
			touchScreenPos = mOneScreenPosition;
			touchDownScreenPos = mOneDownScreenPosition;
			mOneDown = true;
			if (mZeroDown) {
				mZeroDownScreenPosition.set(Gdx.input.getX(0), Gdx.input.getY(0), 0.0f);
				mZeroPosition.set(mZeroDownScreenPosition);
				mCamera.unproject(mZeroPosition);
				altTouchPos = mZeroPosition;
			}
		} else {
			return false;
		}
		touchScreenPos.set(screenX, screenY, 0.0f);
		touchDownScreenPos.set(touchScreenPos);
		touchCoord.set(touchScreenPos);
		mCamera.unproject(touchCoord);
		touchDownPos.set(touchCoord);
		if (mZeroDown && mOneDown) {
			mDownZoom = mCamera.zoom;
			mPinchDownPosition.set(altTouchPos).add(touchDownPos).scl(0.5f);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			mZeroDown = false;
			mOneDownPosition.set(Gdx.input.getX(1), Gdx.input.getY(1), 0.0f);
			mCamera.unproject(mOneDownPosition);
		} else {
			mOneDown = false;
			mZeroDownPosition.set(Gdx.input.getX(0), Gdx.input.getY(0), 0.0f);
			mCamera.unproject(mZeroDownPosition);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 touchCoord;
		Vector3 touchScreenPos;
		if (pointer == 0) {
			touchCoord = mZeroPosition;
			touchScreenPos = mZeroScreenPosition;
			mZeroDown = true;
		} else if (pointer == 1) {
			touchCoord = mOnePosition;
			touchScreenPos = mOneScreenPosition;
			mOneDown = true;
		} else {
			return false;
		}
		touchScreenPos.set(screenX, screenY, 0.0f);
		touchCoord.set(touchScreenPos);
		mCamera.unproject(touchCoord);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		mZoomVelocity += amount * Math.sqrt(mCamera.zoom) / (20.0f);
		return false;
	}

	public void update() {
		if (InputManager.EDIT != InputManager.EditMode.NONE) {
			mZoomVelocity = 0.0f;
			mVelocity.set(Vector2.Zero);
		} else if (mZeroDown ^ mOneDown) {
			Vector3 position;
			Vector3 downPosition;
			int index;
			if (mZeroDown) {
				position = mZeroPosition;
				downPosition = mZeroDownPosition;
				index = 0;
			} else {
				position = mOnePosition;
				downPosition = mOneDownPosition;
				index = 1;
			}
			position.set(Gdx.input.getX(index), Gdx.input.getY(index), 0.0f);
			mCamera.unproject(position);

			mVelocity.set((downPosition.x - position.x) * mScrollSpeed,
					(downPosition.y - position.y) * mScrollSpeed);
			mCamera.translate(mVelocity);
		} else if (mZeroDown && mOneDown) {
			mZeroScreenPosition.set(Gdx.input.getX(0), Gdx.input.getY(0), 0.0f);
			mOneScreenPosition.set(Gdx.input.getX(1), Gdx.input.getY(1), 0.0f);
			// Down delta
			float ddx = (mZeroDownScreenPosition.x - mOneDownScreenPosition.x);
			float ddy = (mZeroDownScreenPosition.y - mOneDownScreenPosition.y);
			// Down dist
			float ddist = (float) Math.sqrt(ddx * ddx + ddy * ddy);
			// Current delta
			float cdx = (mZeroScreenPosition.x - mOneScreenPosition.x);
			float cdy = (mZeroScreenPosition.y - mOneScreenPosition.y);
			// Current dist
			float cdist = (float) Math.sqrt(cdx * cdx + cdy * cdy);

			mZoomVelocity = (ddist / cdist * mDownZoom - mCamera.zoom);

			mPinchPosition.set(mZeroScreenPosition).add(mOneScreenPosition).scl(0.5f);
			mCamera.unproject(mPinchPosition);

			mVelocity.set((mPinchDownPosition.x - mPinchPosition.x) * mScrollSpeed,
					(mPinchDownPosition.y - mPinchPosition.y) * mScrollSpeed);
		} else {
			mVelocity.scl(mDecaySpeed);
			mCamera.translate(mVelocity);
		}
		mZoomVelocity *= mDecaySpeed;
		mCamera.zoom += mZoomVelocity;
		if (mCamera.zoom > mMaxZoom) {
			mCamera.zoom = mMaxZoom;
		} else if (mCamera.zoom < mMinZoom) {
			mCamera.zoom = mMinZoom;
		}
		mCamera.update();
		if (mZeroDown && mOneDown && InputManager.EDIT == InputManager.EditMode.NONE) {
			float cachedPinchX = mPinchPosition.x;
			float cachedPinchY = mPinchPosition.y;
			mPinchPosition.set(mZeroScreenPosition).add(mOneScreenPosition).scl(0.5f);
			mCamera.unproject(mPinchPosition);
			cachedPinchX -= mPinchPosition.x;
			cachedPinchY -= mPinchPosition.y;
			mCamera.translate(cachedPinchX + mVelocity.x, cachedPinchY + mVelocity.y);
			mCamera.update();
		}
	}
}
