package com.pwnscone.drummachine.ui;

import com.pwnscone.drummachine.actors.Actor;

public class InputManager {
	public enum ControlStates {
		NONE, PAN, ZOOM, SELECT
	}

	public static ControlStates STATE = ControlStates.NONE;

	private static Actor mSelectedActor;

	public static void setSelectedActor(Actor actor) {
		mSelectedActor = actor;
	};

	public static Actor getSelectedActor() {
		return mSelectedActor;
	}
}
