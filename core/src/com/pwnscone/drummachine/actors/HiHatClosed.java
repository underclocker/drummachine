package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;

public class HiHatClosed extends Actor {

	@Override
	public void create() {
		if (mMainBody == null) {
			Level level = Game.get().getLevel();
			World world = level.getWorld();
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			Body mainBody = world.createBody(bodyDef);
			mMainBody = mainBody;
			mainBody.setUserData(this);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 0.0f;
			fixtureDef.restitution = 0.95f;

			PolygonShape triangleShape = new PolygonShape();
			triangleShape.set(new float[] { 0.0f, 0.5f, -1.5f, -0.5f, 1.5f, -0.5f });
			fixtureDef.shape = triangleShape;
			mainBody.createFixture(fixtureDef);

			fixtureDef.isSensor = true;
			mainBody.createFixture(fixtureDef);

			triangleShape.dispose();
		} else {
			mMainBody.setActive(true);
		}
	}

	@Override
	public void collide(Fixture otherFixture) {
		if (mCollided || otherFixture.isSensor()) {
			return;
		}
		mCollided = true;
		Game.get().getSynth().hiHatClosed();
	}
}
