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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bigbass1997.sphereram.ScreenshotFactory;
import com.bigbass1997.sphereram.fonts.FontManager;
import com.bigbass1997.sphereram.world.Cylinder;
import com.bigbass1997.sphereram.world.Sphere;
import com.bigbass1997.sphereram.world.World;

public class Main extends ApplicationAdapter {
	
	public Stage stage;
	public Label debugLabel;
	private ImmediateModeRenderer20 render;
	private ShapeRenderer sr;
	public static Camera cam;
	public World world;
	
	public boolean isScreenshotReady = true;
	
	public final int divisions = 10;
	
	@Override
	public void create () {
		FontManager.addFont("fonts/computer.ttf"); //Added font to be used with Debug Text
		
		cam = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(500f, 500f, 500f);
		cam.lookAt(Vector3.Zero);
		cam.near = 1f;
		cam.far = 5000f;
		cam.update();
        
        world = new World(cam);
        world.addObject("TESTSPHERE", new Sphere(0, 0, 0, 500f, divisions, 0xFF00FFFF));
        world.addObject("TESTCYLINDER", new Cylinder(0, 0, 0, 500f, 20f, 500f, divisions, 0x00FFFFFF));
		
		//Creates new stage for use with the debug text label
		stage = new Stage();
		
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
	}
	
	private void update(){
		Input input = Gdx.input;
		float delta = Gdx.graphics.getDeltaTime();
		
		world.update(delta);
		
		//Debug Label Text Update
		String debugLabelText =
				"FPS: " + Gdx.graphics.getFramesPerSecond() + "\n" +
				"delta: " + delta + "\n";
		
		debugLabel.setText(debugLabelText);
		debugLabel.setPosition(10, (debugLabel.getTop() - debugLabel.getY()) + 15);
		
		if(input.isKeyPressed(Keys.Z) && isScreenshotReady){
			ScreenshotFactory.saveScreen();
			isScreenshotReady = false;
		} else if(!input.isKeyPressed(Keys.Z) && !isScreenshotReady){
			isScreenshotReady = true;
		}
	}
	
	@Override
	public void dispose(){
		render.dispose();
	}
}
