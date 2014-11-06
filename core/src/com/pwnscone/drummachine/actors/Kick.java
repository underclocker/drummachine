package com.pwnscone.drummachine.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pwnscone.drummachine.Game;
import com.pwnscone.drummachine.Level;

public class Kick extends Actor {

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
			fixtureDef.restitution = 0.75f;

			PolygonShape boxShape = new PolygonShape();
			boxShape.setAsBox(1.0f, 0.75f);
			fixtureDef.shape = boxShape;
			mainBody.createFixture(fixtureDef);

			fixtureDef.isSensor = true;
			mainBody.createFixture(fixtureDef);

			boxShape.dispose();

			// Graphics

			mTexture = Game.get().getAssetManager().get("kick.png", Texture.class);
			mOffset = new Vector2(-1.0f, -0.75f);
			mScale = .25f;

			mGlowOnHit = true;
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
		mOnTime = Game.get().getSynth().kick(this) ? 1.0f : 0.0f;
	}
}
