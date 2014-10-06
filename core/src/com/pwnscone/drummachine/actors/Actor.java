package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.physics.box2d.Body;
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
}
