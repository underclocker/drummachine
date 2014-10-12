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
		if (mMainBody == null) {
			World world = Game.get().getLevel().getWorld();
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.bullet = true;
			Body body = world.createBody(bodyDef);
			mMainBody = body;

			CircleShape circle = new CircleShape();
			circle.setRadius(.25f);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circle;
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.95f;

			Fixture fixture = body.createFixture(fixtureDef);
			fixture = body.createFixture(fixtureDef);
			circle.dispose();
		} else {
			mMainBody.setActive(true);
		}
		mMainBody.setLinearVelocity(Vector2.Zero);
		mMainBody.setAngularVelocity(0.0f);
	}

	@Override
	public void update() {
		if (mMainBody.isActive()) {
			if (mMainBody.getPosition().len2() > 1000) {
				Game.get().getLevel().destroyActor(this);
			}
		}
	}
}
