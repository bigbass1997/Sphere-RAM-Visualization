package com.bigbass1997.sphereram.world;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.bigbass1997.sphereram.ram.RAMUtil;

public class RAMSystem {
	
	public static enum Method {
		LEFT, MIDDLE, RIGHT
	}
	
	private final String id, idPrefix;
	
	private World world;
	public float radius;
	public int n, sphereColor, cylColor;
	
	public Method method;
	
	public RAMSystem(String id, World world, float radius, int n, Method method, int sphereColor, int cylColor){
		this.id = id;
		idPrefix = this.id + "_SYSTEM_";
		
		this.world = world;
		this.radius = radius;
		this.n = n;
		this.method = method;
		this.sphereColor = sphereColor;
		this.cylColor = cylColor;
		
		recreate();
	}
	
	public void recreate(){
		//Find all existing objects of this system
		ArrayList<String> removeList = new ArrayList<String>();
		for(String id : world.objects.keySet()){
			if(id.contains(idPrefix)) removeList.add(id);
		}
		
		//Remove all found objects for this system
		for(String id : removeList){
			world.removeObject(id);
		}
		
		//Re/create the sphere and all needed cylinders for this system
		createSphere();
		createCylinders(this.method);
	}
	
	private void createSphere(){
		world.addObject(idPrefix + "SPHERE", new Sphere(0, 0, 0, radius, 50, sphereColor));
	}
	
	private void createCylinders(Method method){
		//world.addObject(idPrefix + "CYLINDER_" + i, new Cylinder(x, 0, 0, y, width, y, 50, cylColor));
		
		double min = -radius;
		double max = radius;
		double width = (max - min) / n;
		
		LinkedList<Double> yList;
		
		double x = min;
		switch(method){
		case LEFT:
			yList = RAMUtil.getList("sqrt(r^2 - x^2)", -radius, radius, radius, width);
			
			for(int i = 0; i < yList.size(); i++){
				world.addObject(idPrefix + "CYLINDER_" + i, new Cylinder(((float) x), 0, 0, yList.get(i).floatValue(), (float) width, yList.get(i).floatValue(), 50, cylColor));
				
				x += width;
			}
			
			break;
		case MIDDLE:
			yList = RAMUtil.getList("sqrt(r^2 - x^2)", (-radius) + (width/2), (radius) + (width/2), radius, width);
			
			for(int i = 0; i < yList.size(); i++){
				world.addObject(idPrefix + "CYLINDER_" + i, new Cylinder(((float) x), 0, 0, yList.get(i).floatValue(), (float) width, yList.get(i).floatValue(), 50, cylColor));
				
				x += width;
			}
			
			break;
		case RIGHT:
			yList = RAMUtil.getList("sqrt(r^2 - x^2)", (-radius) + (width), (radius) + (width), radius, width);
			
			for(int i = 0; i < yList.size(); i++){
				world.addObject(idPrefix + "CYLINDER_" + i, new Cylinder(((float) x), 0, 0, yList.get(i).floatValue(), (float) width, yList.get(i).floatValue(), 50, cylColor));
				
				x += width;
			}
			
			break;
		}
	}
	
	public void update(float delta){
		Input input = Gdx.input;
		
		if(input.isKeyJustPressed(Keys.T)){
			switch(method){
			case LEFT:
				method = Method.MIDDLE;
				break;
			case MIDDLE:
				method = Method.RIGHT;
				break;
			case RIGHT:
				method = Method.LEFT;
				break;
			}
			recreate();
		}
	}
}
