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
import com.pwnscone.drummachine.util.Misc;

public class Ball extends Actor {
	private static int POS_HIST_SIZE = 30;
	private static float EPSILON = 0.001f;

	private float[] xPos;
	private float[] yPos;
	private int histIndex;

	@Override
	public void create() {
		super.create();
		xPos = new float[POS_HIST_SIZE];
		yPos = new float[POS_HIST_SIZE];
		histIndex = 0;
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
			Vector2 pos = mMainBody.getPosition();
			if (pos.len2() > 100000.0f) {
				Game.get().getLevel().destroyActor(this);
				return;
			}
			xPos[histIndex] = pos.x;
			yPos[histIndex] = pos.y;
			histIndex++;
			if (histIndex == POS_HIST_SIZE) {
				histIndex = 0;
				Vector2 posCache = Misc.v2r0;
				for (int i = 0; i < POS_HIST_SIZE; i++) {
					if (pos.dst2(xPos[histIndex], yPos[histIndex]) > EPSILON) {
						return;
					}
				}
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
