package enginetester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		// ********TERRAIN TEXTURE STUFF*********//
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *****************************************

		ModelData data = OBJFileLoader.loadOBJ("tree");
		RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());
		data = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());
		data = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());

		TexturedModel staticModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grassTModel = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
		grassTModel.getTexture().setHasTransparency(true);
		grassTModel.getTexture().setUseFakeLighting(true);
		ModelTexture fernMT = new ModelTexture(loader.loadTexture("fern"));
		fernMT.setHasTransparency(true);
		fernMT.setUseFakeLighting(true);
		fernMT.setNumberOfRows(2);
		TexturedModel fernTModel = new TexturedModel(fernModel, fernMT);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			float x = random.nextFloat() * 800 - 400;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);

			entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, 3));

			float x1 = random.nextFloat() * 800 - 400;
			float z1 = random.nextFloat() * -600;
			float y1 = terrain.getHeightOfTerrain(x1, z1);

			entities.add(new Entity(grassTModel, new Vector3f(x1, y1, z1), 0, 0, 0, 1));

			float x2 = random.nextFloat() * 800 - 400;
			float z2 = random.nextFloat() * -600;
			float y2 = terrain.getHeightOfTerrain(x2, z2);
			entities.add(new Entity(fernTModel, random.nextInt(4), new Vector3f(x2, y2, z2), 0, 0, 0, 1));
		}

		List<Light> lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0, 10000, -7000), new Vector3f(1, 1, 1)));
		lights.add(new Light(new Vector3f(-200, 10, -200), new Vector3f(10, 0, 0)));
		lights.add(new Light(new Vector3f(200, 10, 200), new Vector3f(0, 0, 10)));

		MasterRenderer renderer = new MasterRenderer();

		data = OBJFileLoader.loadOBJ("bunny");
		RawModel bunny = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());
		TexturedModel bunnyModel = new TexturedModel(bunny, new ModelTexture(loader.loadTexture("white")));
		Player player = new Player(bunnyModel, new Vector3f(100, 0, -50), 0, 0, 0, .6f);
		Camera camera = new Camera(player);

		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, .5f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		guis.add(new GuiTexture(loader.loadTexture("health"), new Vector2f(0.3f, .7f), new Vector2f(0.25f, 0.25f)));
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);
			renderer.processEntity(player);

			renderer.processTerrain(terrain);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
