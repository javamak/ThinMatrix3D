package enginetester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.MasterRenderer;
import renderengine.OBJLoader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		RawModel model = OBJLoader.loadObjModel("stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		texture.setShineDamper(10);
		texture.setReflectivity(5);
		TexturedModel texturedModel = new TexturedModel(model, texture);

		Light light = new Light(new Vector3f(0, 0, 20), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		
		List<Entity> allCubes = new ArrayList<>();
		Random random = new Random();
		
		for(int i=0; i<200; i++) {
			float x= random.nextFloat() * 100 - 50;
			float y= random.nextFloat() * 100 - 50;
			float z= random.nextFloat() * -300;
			allCubes.add(new Entity(texturedModel, new Vector3f(x, y, z), random.nextFloat() * 180f,
					random.nextFloat() * 180f, 0f, 1f));
		}
		
		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			camera.move();
			
			for(Entity cube : allCubes) {
				renderer.processEntity(cube);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
