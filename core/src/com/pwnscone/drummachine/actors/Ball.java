package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;

public class Ball extends Actor {

	@Override
	public void create() {
		super.create();
		if (mMainBody == null) {
			World world = Game.get().getLevel().getWorld();
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.bullet = true;
			Body body = world.createBody(bodyDef);
			mMainBody = body;
			mMainBody.setUserData(this);

			CircleShape circle = new CircleShape();
			circle.setRadius(.2f);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circle;
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.85f;

			body.createFixture(fixtureDef);
			circle.dispose();
		} else {
			mMainBody.setActive(true);
		}
		mMainBody.setLinearVelocity(Vector2.Zero);
		mMainBody.setAngularVelocity(0.0f);
	}

	@Override
	public void update() {
		super.update();
		if (mMainBody.isActive()) {
			if (mMainBody.getPosition().len2() > 100000.0f) {
				Game.get().getLevel().destroyActor(this);
			}
		}
	}

	@Override
	public void collide(Fixture otherFixture) {
		super.collide(otherFixture);
		if (mCollided || otherFixture.isSensor()) {
			return;
		}
		mCollided = true;
		// Game.get().getSynth().hiHatClosed();
	}
}
