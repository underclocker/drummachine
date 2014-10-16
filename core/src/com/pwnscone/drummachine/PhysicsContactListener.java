package com.pwnscone.drummachine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pwnscone.drummachine.actors.Actor;

public class PhysicsContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Actor actorA = (Actor) contact.getFixtureA().getBody().getUserData();
		Actor actorB = (Actor) contact.getFixtureB().getBody().getUserData();
		actorA.collide();
		actorB.collide();
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
