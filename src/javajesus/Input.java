package javajesus;

import org.lwjgl.glfw.*;
import org.lwjgl.glfw.GLFWKeyCallback;

/*
 * Handles game input from keyboard and mouse
 */
public class Input extends GLFWKeyCallback {
	
	// create an array of keys C style
	public static boolean[] keys = new boolean[1024];

	/**
	 * invoke()
	 * called whenever input changes
	 * 
	 * @param window - the address of the window where input was initiated
	 * @param key - the ID of the key pressed
	 * @param action - the action performed
	 */
	public void invoke(long window, int key, int scancode, int action, int mods) {
		
		// Not GLFW_PRESS or GLFW_REPEATED
		keys[key] = action != GLFW.GLFW_RELEASE;
		
	}

}
