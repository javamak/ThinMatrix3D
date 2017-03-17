package enginetester;

import org.lwjgl.opengl.Display;

import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.RawModel;
import renderengine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		//OpenGL expects vertices to be defined counter clockwise by default
		
		float[] vertices = {
				//Left bottom triangle
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				//Right top triangle
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};
		RawModel model = loader.loadToVAO(vertices);
		while (!Display.isCloseRequested()) {
			renderer.prepare();
			//Game logic
			renderer.render(model);
			DisplayManager.updateDisplay();
		}
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
