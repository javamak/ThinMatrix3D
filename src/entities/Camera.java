package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 2, 0);
	private float pitch, yaw, roll;


	public Camera() {
	}
	
	public void move() {


		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= 0.10f;
		} 
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x += 0.10f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 0.10f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			position.z += 0.10f;
		} 
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
