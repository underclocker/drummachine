package com.pwnscone.drummachine.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.pwnscone.drummachine.actors.Actor;

public class HitTest implements QueryCallback {
	private Fixture mFixture;
	private Vector2 mPosition;

	public HitTest() {
		mPosition = new Vector2();
	}

	@Override
	public boolean reportFixture(Fixture fixture) {
		if (fixture.isSensor() && fixture.testPoint(mPosition)) {
			mFixture = fixture;
			return false;
		}
		mFixture = null;
		return true;
	}

	public void set(float x, float y) {
		mFixture = null;
		mPosition.set(x, y);
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
