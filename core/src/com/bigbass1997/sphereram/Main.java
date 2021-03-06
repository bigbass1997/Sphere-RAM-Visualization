package com.bigbass1997.sphereram;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bigbass1997.sphereram.ScreenshotFactory;
import com.bigbass1997.sphereram.fonts.FontManager;
import com.bigbass1997.sphereram.world.RAMSystem.Method;
import com.bigbass1997.sphereram.world.World;
import com.bigbass1997.sphereram.world.RAMSystem;

public class Main extends ApplicationAdapter {
	
	public Stage stage;
	public Label debugLabel;
	private ImmediateModeRenderer20 render;
	private ShapeRenderer sr;
	public static Camera cam;
	public World world;
	public RAMSystem system;
	
	public boolean isScreenshotReady = true;
	
	public final int divisions = 20;
	
	@Override
	public void create () {
		FontManager.addFont("fonts/computer.ttf"); //Added font to be used with Debug Text
		
		cam = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(50f, 50f, 50f);
		cam.lookAt(0f, 0f, 0f);
		cam.near = 1f;
		cam.far = 5000f;
		cam.update();
		
        world = new World(cam);
        
        //Creates new stage and sets mouse detection for it.
		stage = new Stage();
        
        system = new RAMSystem("TEST", world, 4f, 10, Method.LEFT, 0xFF00FFFF, 0x00FFFFFF);
		
		debugLabel = new Label("", new Label.LabelStyle(FontManager.getFont("fonts/computer.ttf", 20).font, Color.WHITE));
		debugLabel.setPosition(10, Gdx.graphics.getHeight() - debugLabel.getHeight());
		
		//Adds the debug label to the stage so that it can be rendered/updated
		stage.addActor(debugLabel);
		
		render = new ImmediateModeRenderer20(50000, false, true, 0);
		sr = new ShapeRenderer();
	}

	@Override
	public void render () {
		draw();
		update();
	}
	
	private void draw(){
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		world.render();
		
		render.begin(cam.combined, ShapeType.Filled.getGlType());
		sr.begin(ShapeType.Filled);
		
		//render here
		
		sr.end();
		render.end();
		
		stage.draw();
		system.render();
	}
	
	private void update(){
		Input input = Gdx.input;
		float delta = Gdx.graphics.getDeltaTime();
		
		stage.act(delta);
		
		world.update(delta);
		
		//Debug Label Text Update
		String debugLabelText =
				"FPS: " + Gdx.graphics.getFramesPerSecond() + "\n" +
				"delta: " + delta + "\n" + 
				"realCamSpeed: " + (world.camSpeed * delta);
		
		debugLabel.setText(debugLabelText);
		debugLabel.setPosition(10, (debugLabel.getTop() - debugLabel.getY()) + 25);
		
		if(input.isKeyPressed(Keys.Z) && isScreenshotReady){
			ScreenshotFactory.saveScreen();
			isScreenshotReady = false;
		} else if(!input.isKeyPressed(Keys.Z) && !isScreenshotReady){
			isScreenshotReady = true;
		}
		
		system.update(delta);
	}
	
	@Override
	public void dispose(){
		render.dispose();
		stage.dispose();
		system.dispose();
	}
}
