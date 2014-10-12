package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.pwnscone.drummachine.util.Misc;
import com.pwnscone.drummachine.util.Poolable;

public class Actor extends Poolable {
	protected Body mMainBody;

	public void create() {

	}

	public void update() {

	}

	public void destroy() {
		if (mMainBody != null) {
			mMainBody.setActive(false);
		}
	}

	public Body getBody() {
		return mMainBody;
	}

	public void setTransformation(float x, float y, float angle) {
		Vector2 position = Misc.v2r0;
		position.set(x, y);
		if (mMainBody != null) {
			mMainBody.setTransform(position, angle);
		}
	}
}
