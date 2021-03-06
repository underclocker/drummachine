package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.actors.Actor;
import com.pwnscone.drummachine.ui.InputManager.EditMode;
import com.pwnscone.drummachine.util.Misc;

public class ActorInputProcessor implements InputProcessor {
	private static float mBoundSpeed = .125f;
	private static float mActorBoundSpeed = .0625f;
	private static float mBoundBuffer = 0.25f;

	private OrthographicCamera mCamera;
	private boolean mDown;
	private Vector3 mPosition;
	private Vector3 mDownPosition;
	private Vector3 mDownScreenPosition;
	private Vector3 mScreenPosition;
	private Vector3 mDownOffset;
	private HitTest mHitTest;
	private boolean mPress;
	private float mRotation;
	private float mSnapCooldown;

	public void create() {
		mCamera = Game.get().getView().getCamera();
		mDown = false;
		mPosition = new Vector3();
		mDownPosition = new Vector3();
		mDownScreenPosition = new Vector3();
		mScreenPosition = new Vector3();
		mDownOffset = new Vector3();
		mHitTest = new HitTest();
		mSnapCooldown = 0;
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
			Actor selectedActor = InputManager.getSelectedActor();
			if (selectedActor != null) {
				Vector2 pos = Misc.v2r0;
				pos.set(mPosition.x, mPosition.y);
				Vector2 actorPos = selectedActor.getPosition();
				mDownOffset.set(actorPos.x, actorPos.y, 0.0f);
				mDownOffset.sub(mPosition);
				float dist = mDownOffset.len() / mCamera.zoom;
				if (dist < 1.75f * View.UI_SCALE && !selectedActor.isLocked()
						&& !selectedActor.isTranslationLocked()) {
					InputManager.EDIT = EditMode.TRANSLATE;
				} else if (dist < 3.5f * View.UI_SCALE && !selectedActor.isLocked()
						&& !selectedActor.isRotationLocked()) {
					InputManager.EDIT = EditMode.ROTATE;
					mRotation = selectedActor.getBody().getAngle()
							- (float) Math.atan2(mDownOffset.y, mDownOffset.x);
					if (mRotation > Math.PI) {
						mRotation -= 2 * Math.PI;
					} else if (mRotation < -Math.PI) {
						mRotation += 2 * Math.PI;
					}
				} else {
					InputManager.EDIT = EditMode.NONE;
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			Actor selectedActor = InputManager.getSelectedActor();
			if (selectedActor != null) {
				selectedActor.setLinearVelocity(Vector2.Zero);
				selectedActor.setAngularVelocity(0.0f);
			}
			if (mPress && InputManager.EDIT == EditMode.NONE) {
				mPosition.set(screenX, screenY, 0.0f);
				mCamera.unproject(mPosition);
				World world = Game.get().getLevel().getWorld();
				mHitTest.set(mPosition.x, mPosition.y);
				world.QueryAABB(mHitTest, mPosition.x, mPosition.y, mPosition.x, mPosition.y);
				Actor actor = mHitTest.getActor();
				InputManager.setSelectedActor(actor);
				if (actor != null) {
					actor.resetGhostCycle();
				}
			}
			InputManager.EDIT = EditMode.NONE;
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

	public void update() {
		if (InputManager.EDIT == InputManager.EditMode.TRANSLATE) {
			Actor selectedActor = InputManager.getSelectedActor();
			if (selectedActor.isLocked() || selectedActor.isTranslationLocked()) {
				return;
			}
			Body body = selectedActor.getBody();
			Vector3 position = Misc.v3r1;
			position.set(body.getPosition().x, body.getPosition().y, 0.0f);

			Vector3 offset = Misc.v3r0;
			offset.set(mPosition).add(mDownOffset).sub(position).scl(10f);
			mSnapCooldown--;
			if (selectedActor.isShowingGhost()) {
				float dist = body.getPosition().dst2(selectedActor.getGhostPos());
				if (dist < 0.125f && mSnapCooldown <= 0) {
					if (offset.len2() > 30.0f) {
						mSnapCooldown = 30;
					}
					offset.set(selectedActor.getGhostPos().x, selectedActor.getGhostPos().y, 0.0f);
					offset.sub(position).scl(15);

				}
			}
			Vector2 bounds = Game.get().getLevel().getBounds();
			if (Math.abs(position.x + offset.x * mBoundSpeed) > bounds.x - mBoundBuffer) {
				offset.x = (bounds.x - mBoundBuffer - Math.abs(position.x)) / mBoundSpeed
						* (offset.x > 0 ? 1.0f : -1.0f);
			}
			if (Math.abs(position.y + offset.y * mBoundSpeed) > bounds.y - mBoundBuffer) {
				offset.y = (bounds.y - mBoundBuffer - Math.abs(position.y)) / mBoundSpeed
						* (offset.y > 0 ? 1.0f : -1.0f);
			}

			selectedActor.setLinearVelocity(offset.x, offset.y);
		} else if (InputManager.EDIT == InputManager.EditMode.ROTATE) {
			Actor selectedActor = InputManager.getSelectedActor();
			if (selectedActor.isLocked() || selectedActor.isRotationLocked()) {
				return;
			}
			Body body = selectedActor.getBody();
			Vector2 offset = Misc.v2r0;
			offset.set(mPosition.x, mPosition.y).sub(body.getPosition()).scl(-1.0f);
			float angle = body.getAngle() - (float) Math.atan2(offset.y, offset.x);
			angle = mRotation - angle;
			if (angle > Math.PI) {
				int laps = (int) (angle / Misc.TAU - .5f);
				angle -= Misc.TAU * (1 + laps);
			} else if (angle < -Math.PI) {
				int laps = (int) (-angle / Misc.TAU - .5f);
				angle += Misc.TAU * (1 + laps);
			}
			selectedActor.setAngularVelocity(angle * 10.0f);
		}
	}
}
