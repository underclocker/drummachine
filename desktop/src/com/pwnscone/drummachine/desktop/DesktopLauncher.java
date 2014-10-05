package com.pwnscone.drummachine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pwnscone.drummachine.Game;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 486;
		config.height = 864;
		new LwjglApplication(new Game(), config);
	}
}
