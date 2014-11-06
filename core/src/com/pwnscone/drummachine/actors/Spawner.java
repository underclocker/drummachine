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
import com.pwnscone.drummachine.util.Misc;

public class Spawner extends Actor {
	private Level mLevel;
	private float mSpeed = 32.0f;
	private int mSpawnTimer = 0;
	private int mSpawnRate = 64;

	@Override
	public void create() {
		super.create();
		if (mMainBody == null) {
			mLevel = Game.get().getLevel();
			World world = mLevel.getWorld();
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			Body mainBody = world.createBody(bodyDef);
			mMainBody = mainBody;
			mainBody.setUserData(this);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 0.0f;
			fixtureDef.restitution = 0.95f;

			float scale = .25f;

			float[] verts = { 1, -3, 2, -2, 2, 1, 1, 2 };
			Misc.scaleArray(verts, scale);
			PolygonShape poly = new PolygonShape();
			poly.set(verts);
			fixtureDef.shape = poly;
			mainBody.createFixture(fixtureDef);
			poly.dispose();

			float[] verts2 = { 1, 2, -1, 2, 1, 0 };
			Misc.scaleArray(verts2, scale);
			poly = new PolygonShape();
			poly.set(verts2);
			fixtureDef.shape = poly;
			mainBody.createFixture(fixtureDef);
			poly.dispose();

			float[] verts3 = { -1, 2, -2, 1, -1, 0, 0, 1 };
			Misc.scaleArray(verts3, scale);
			poly = new PolygonShape();
			poly.set(verts3);
			fixtureDef.shape = poly;
			mainBody.createFixture(fixtureDef);
			poly.dispose();

			float[] verts4 = { -2, 1, -2, -2, -1, -3, -1, 0 };
			Misc.scaleArray(verts4, scale);
			poly = new PolygonShape();
			poly.set(verts4);
			fixtureDef.shape = poly;
			mainBody.createFixture(fixtureDef);
			poly.dispose();

			// Hit Sensor
			fixtureDef.isSensor = true;

			float[] verts5 = { 1, -3, 2, -2, 2, 1, 1, 2, -1, 2, -2, 1, -2, -2, -1, -3 };
			Misc.scaleArray(verts5, scale);
			poly = new PolygonShape();
			poly.set(verts5);
			fixtureDef.shape = poly;
			mMainBody.createFixture(fixtureDef);
			poly.dispose();

			// Graphics

			mTexture = Game.get().getAssetManager().get("spawner.png", Texture.class);
			mOffset = new Vector2(-0.5f, -0.75f);
			mScale = .25f;

		} else {
			mMainBody.setActive(true);
		}
		mSpawnTimer = Game.get().getLevel().getFrame() % mSpawnRate;
	}

	@Override
	public void update() {
		super.update();
		if (mSpawnTimer == 0) {
			mSpawnTimer = mSpawnRate;
			Ball ball = (Ball) mLevel.createActor(Ball.class);
			Vector2 offset = Misc.v2r0;
			offset.set(0.0f, -.25f);
			offset.rotate(mMainBody.getAngle() * Misc.RAD_TO_DEG);
			Vector2 position = Misc.v2r1;
			position.set(mMainBody.getPosition());
			position.add(offset);
			Body ballBody = ball.mMainBody;
			ballBody.setTransform(position, 0.0f);
			offset.scl(mSpeed);
			ball.mMainBody.setLinearVelocity(offset);
		}
		mSpawnTimer--;
	}

	public void setSpawnRate(int spawnrate) {
		mSpawnRate = spawnrate;
	}

	@Override
	public void collide(Fixture otherFixture) {
		super.collide(otherFixture);
		if (mCollided || otherFixture.isSensor() || isInCache(otherFixture)) {
			return;
		}
		mCollided = true;
	}
}
