package com.bigbass1997.sphereram.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Cylinder extends Object {
	
	public int divisions = 0;
	
	public Cylinder(Vector3 pos, Vector3 size, int divisions, int color){
		this(pos.x, pos.y, pos.z, size.x, size.y, size.z, divisions, color);
	}
	
	public Cylinder(float x, float y, float z, float sx, float sy, float sz, int divisions, int color){
		this.pos = new Vector3(x, y, z);
		this.size = new Vector3(sx, sy, sz);
		this.divisions = divisions;
		this.color = color;
		
		model = createModel(divisions);
		modelInstance = new ModelInstance(model);
		modelInstance.transform.rotate(Vector3.Z, 90f);
		
		this.setPos(pos);
	}

	@Override
	public void update(float delta){
		Input input = Gdx.input;
		
		//Moves the sphere in a random direction from its previous position
		/*Random rand = new Random();
		
		float dif = 3f;
		float dx = (rand.nextFloat() * dif) - (dif / 2);
		float dy = (rand.nextFloat() * dif) - (dif / 2);
		float dz = (rand.nextFloat() * dif) - (dif / 2);
		
		this.addPos(dx, dy, dz);*/

		if(input.isKeyPressed(Keys.UP)){
			divisions += 1;
			if(divisions > 180) divisions = 180;
			setDivisions(divisions);
		}else if(input.isKeyPressed(Keys.DOWN)){
			divisions -= 1;
			if(divisions < 2) divisions = 2;
			setDivisions(divisions);
		}
		
		float speed = 50f * delta;
		if(input.isKeyPressed(Keys.R)){
			this.addPos(-speed, 0, 0);
		}else if(input.isKeyPressed(Keys.F)){
			this.addPos(speed, 0, 0);
		}
	}
	
	public void setDivisions(int newDivisions){
		model = createModel(newDivisions);
		modelInstance = new ModelInstance(model);
		modelInstance.transform.rotate(Vector3.Z, 90f);
		this.setPos(pos);
	}
	
	public void setRadius(float radius){
		size.set(radius, size.y, radius);
		modelInstance = new ModelInstance(model);
		modelInstance.transform.rotate(Vector3.Z, 90f);
		this.setPos(pos);
	}
	
	private Model createModel(int divisions){
		ModelBuilder modelBuilder = new ModelBuilder();
		return modelBuilder.createCylinder(size.x, size.y, size.z,
				divisions, GL20.GL_LINES,
				new Material(ColorAttribute.createDiffuse(new Color(color))),
				Usage.Position | Usage.Normal);
	}
}
