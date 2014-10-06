package com.pwnscone.drummachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SceneInputProcessor implements InputProcessor {
	private float mScrollSpeed = 0.5f;
	private float mDecaySpeed = 0.8f;
	private OrthographicCamera mCamera;
	private boolean mZeroDown;
	private boolean mOneDown;
	private Vector3 mZeroDownPosition;
	private Vector3 mOneDownPosition;
	private Vector3 mZeroPosition;
	private Vector3 mOnePosition;
	private Vector3 mZeroScreenPosition;
	private Vector3 mOneScreenPosition;
	private Vector2 mVelocity;

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
		mVelocity = new Vector2();
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
		if (pointer == 0) {
			touchCoord = mZeroPosition;
			touchDownPos = mZeroDownPosition;
			touchScreenPos = mZeroScreenPosition;
			mZeroDown = true;
		} else if (pointer == 1) {
			touchCoord = mOnePosition;
			touchDownPos = mOneDownPosition;
			touchScreenPos = mOneScreenPosition;
			mOneDown = true;
		} else {
			return false;
		}
		touchScreenPos.set(screenX, screenY, 0.0f);
		touchCoord.set(touchScreenPos);
		mCamera.unproject(touchCoord);
		touchDownPos.set(touchCoord);
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
		return false;
	}

	public void update() {
		if (mZeroDown ^ mOneDown) {
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
			mCamera.update();
		} else if (mZeroDown && mOneDown) {
			// Zoom
		} else {
			mVelocity.scl(mDecaySpeed);
			mCamera.translate(mVelocity);
			mCamera.update();
		}
	}
}
