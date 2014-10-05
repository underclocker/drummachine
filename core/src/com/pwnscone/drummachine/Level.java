package com.pwnscone.drummachine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Level {
	private World mWorld;

	public Level() {
		mWorld = new World(new Vector2(0.0f, -4.9f), true);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		Body body = mWorld.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(.25f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.9f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
	}

	public void update() {
		mWorld.step(1.0f / 60.0f, 8, 3);
	}

	public World getWorld() {
		return mWorld;
	}
}
