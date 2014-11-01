package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;

public class Snare extends Actor {

	@Override
	public void create() {
		super.create();
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
			fixtureDef.friction = 0.5f;
			fixtureDef.restitution = 0.9f;

			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(.75f);
			fixtureDef.shape = circleShape;
			mainBody.createFixture(fixtureDef);

			fixtureDef.isSensor = true;
			mainBody.createFixture(fixtureDef);

			circleShape.dispose();
		} else {
			mMainBody.setActive(true);
		}
	}

	@Override
	public void collide(Fixture otherFixture) {
		super.collide(otherFixture);
		if (mCollided || otherFixture.isSensor() || isInCache(otherFixture)) {
			return;
		}
		mCollided = true;
		Game.get().getSynth().snare(this);
	}
}