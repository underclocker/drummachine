package com.pwnscone.drummachine.ui;

import com.pwnscone.drummachine.actors.Actor;

public class InputManager {

	public enum EditMode {
		NONE, ROTATE, TRANSLATE
	}

	public static EditMode EDIT = EditMode.NONE;

	private static Actor mSelectedActor;

	public static void setSelectedActor(Actor actor) {
		mSelectedActor = actor;
	}

	public static Actor getSelectedActor() {
		return mSelectedActor;
	}
}
