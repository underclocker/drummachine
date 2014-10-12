package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.actors.Actor;

public class ActorInputProcessor implements InputProcessor {
	private OrthographicCamera mCamera;
	private boolean mDown;
	private Vector3 mPosition;
	private Vector3 mDownPosition;
	private Vector3 mDownScreenPosition;
	private Vector3 mScreenPosition;
	private HitTest mHitTest;
	private boolean mPress;

	public void create() {
		mCamera = Game.get().getView().getCamera();
		mDown = false;
		mPosition = new Vector3();
		mDownPosition = new Vector3();
		mDownScreenPosition = new Vector3();
		mScreenPosition = new Vector3();
		mHitTest = new HitTest();
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
		if (pointer == 0) {
			mPress = true;
			mDownScreenPosition.set(screenX, screenY, 0.0f);
			mDownPosition.set(mDownScreenPosition);
			mCamera.unproject(mDownPosition);
			mPosition.set(mDownPosition);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			if (mPress) {
				mPosition.set(screenX, screenY, 0.0f);
				mCamera.unproject(mPosition);
				World world = Game.get().getLevel().getWorld();
				mHitTest.reset();
				world.QueryAABB(mHitTest, mPosition.x, mPosition.y, mPosition.x, mPosition.y);
				Actor actor = mHitTest.getActor();
				InputManager.setSelectedActor(actor);
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer == 0) {
			mScreenPosition.set(screenX, screenY, 0.0f);
			mPosition.set(mScreenPosition);
			mCamera.unproject(mPosition);
			if (mPress && mScreenPosition.dst(mDownScreenPosition) > 10f * View.DENSITY) {
				mPress = false;
			}
		}
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
}
