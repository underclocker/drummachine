package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.pwnscone.drummachine.actors.Actor;

public class HitTest implements QueryCallback {
	private Fixture mFixture;

	@Override
	public boolean reportFixture(Fixture fixture) {
		if (fixture.isSensor()) {
			mFixture = fixture;
			return false;
		}
		mFixture = null;
		return true;
	}

	public void reset() {
		mFixture = null;
	}

	public Fixture getFixture() {
		return mFixture;
	}

	public Body getBody() {
		return mFixture != null ? mFixture.getBody() : null;
	}

	public Actor getActor() {
		Body body = getBody();
		if (body != null) {
			Object userData = body.getUserData();
			if (userData != null && userData instanceof Actor) {
				return (Actor) userData;
			}
		}
		return null;
	}
}
