package com.bigbass1997.sphereram.world;

import java.util.LinkedList;

/**
 * 
 */
public class RAMSystem {
	
	private final String id, idPrefix;
	
	private World world;
	private float radius;
	private int n, sphereColor, cylColor;
	
	public RAMSystem(String id, World world, float radius, int n, int sphereColor, int cylColor){
		this.id = id;
		idPrefix = id + "_";
		
		this.world = world;
		this.radius = radius;
		this.n = n - 1;
		this.sphereColor = sphereColor;
		this.cylColor = cylColor;
		
		world.addObject(idPrefix + "SPHERE", new Sphere(0, 0, 0, radius, 50, sphereColor));
		
		float width = radius / n;
		LinkedList<Float> yVals = getYVals(-radius, radius, width);
		for(int i = 0; i < yVals.size(); i++){
			float x = (width * i) - (radius / 2) + (width / 2);
			float y = yVals.get(i);
			//System.out.println(i + "=" + y);
			
			world.addObject(idPrefix + "CYLINDER_" + i, new Cylinder(x, 0, 0, y, width, y, 50, cylColor));
		}
	}
	
	/**
	 * https://github.com/bigbass1997/Rectangular-Approximation-Method/blob/master/src/com/bigbass1997/ram/RAMUtil.java
	 */
	private LinkedList<Float> getYVals(float min, float max, float width){
		LinkedList<Float> list = new LinkedList<Float>();
		
		float x = min;
		while(x <= max){
			float range = ((max/2) - (min/2));
			float a = range*range;
			float b = x*x;
			
			System.out.println(x + "=" + a + ", " + b);
			
			list.add( (float) Math.sqrt(a - b) );
			x += (width * 2);
		}
		
		return list;
	}
}
