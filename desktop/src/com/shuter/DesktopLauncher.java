package com.shuter;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		int width = (int) Core.VIRTUAL_WIDTH;
		int height = (int) Core.VIRTUAL_HEIGHT;
		config.setWindowedMode(width, height);
		new Lwjgl3Application(new Core(), config);
	}
}
