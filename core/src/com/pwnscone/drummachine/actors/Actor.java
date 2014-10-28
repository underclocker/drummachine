package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.pwnscone.drummachine.util.Misc;
import com.pwnscone.drummachine.util.Poolable;

public class Actor extends Poolable {
	protected Body mMainBody;
	protected boolean mCollided = false;

	public void create() {

	}

	public void update() {
		mCollided = false;
	}

	public void destroy() {
		if (mMainBody != null) {
			mMainBody.setActive(false);
		}
	}

	public Body getBody() {
		return mMainBody;
	}

	public Vector2 getPosition() {
		if (mMainBody != null) {
			return mMainBody.getPosition();
		}
		return null;
	}

	public void setTransformation(float x, float y, float angle) {
		Vector2 position = Misc.v2r0;
		position.set(x, y);
		if (mMainBody != null) {
			mMainBody.setTransform(position, angle);
		}
	}

	public void collide(Fixture otherFixture) {

	}
}
