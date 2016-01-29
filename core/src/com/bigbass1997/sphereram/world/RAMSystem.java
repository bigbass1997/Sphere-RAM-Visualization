package com.bigbass1997.sphereram.world;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.bigbass1997.sphereram.fonts.FontManager;
import com.bigbass1997.sphereram.graphics.SystemGUI;
import com.bigbass1997.sphereram.ram.RAMUtil;
import com.bigbass1997.sphereram.skins.SkinManager;

public class RAMSystem {
	
	public static enum Method {
		LEFT, MIDDLE, RIGHT
	}
	
	private final String id, idPrefix;
	
	private World world;
	public float radius;
	public int n, sphereColor, cylColor;
	
	public Method method;

	private SystemGUI gui;
	
	private LinkedList<Double> yList;
	
	private final String ln = "\n";
	
	public RAMSystem(String id, World world, float radius, int n, Method method, int sphereColor, int cylColor){
		this.id = id;
		idPrefix = this.id + "_SYSTEM_";
		
		this.world = world;
		this.radius = radius;
		this.n = n;
		this.method = method;
		this.sphereColor = sphereColor;
		this.cylColor = cylColor;
		
		gui = new SystemGUI();
		
		String[] ids = new String[]{"Radius", "Cylinders", "Method"};
		
		TextField tmpField;
		Label tmpLabel;
		float buf = 5f;
		for(int i = 0; i < ids.length; i++){
			tmpField = new TextField("0", SkinManager.getSkin("fonts/computer.ttf", 26));
			tmpField.setWidth(50);
			tmpField.setPosition(Gdx.graphics.getWidth() - tmpField.getWidth() - buf, Gdx.graphics.getHeight() - ((buf + tmpField.getHeight()) * (i + 1)));
			gui.addActor(idPrefix + "INPUTFIELD_" + ids[i], tmpField);
			
			tmpLabel = new Label(ids[i] + ":", new Label.LabelStyle(FontManager.getFont("fonts/computer.ttf", 28).font, Color.WHITE));
			tmpLabel.setAlignment(Align.right);
			tmpLabel.setPosition(
					tmpField.getX() - tmpLabel.getPrefWidth() - 2,
					tmpField.getY() + ((tmpLabel.getHeight() - tmpLabel.getStyle().font.getLineHeight()) * 2)
			);
			gui.addActor(idPrefix + "LABEL_" + ids[i], tmpLabel);
		}
		
		gui.addActor(idPrefix + "INFOLABEL", new Label("", new Label.LabelStyle(FontManager.getFont("fonts/computer.ttf", 20).font, Color.WHITE)));
		
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
		double min = -radius;
		double max = radius;
		double width = (max - min) / n;
		
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
	
	public void render(){
		gui.render();
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
		
		if(input.isKeyPressed(Keys.X)){
			radius += 1f;
			recreate();
		}else if(input.isKeyPressed(Keys.C)){
			radius -= 1f;
			if(radius < 1) radius = 1;
			recreate();
		}
		
		if(input.isKeyPressed(Keys.LEFT)){
			n -= 1;
			if(n < 2) n = 2;
			recreate();
		}else if(input.isKeyPressed(Keys.RIGHT)){
			n += 1;
			recreate();
		}
		
		gui.update(delta);
		
		String infoData =
				"System Data:" + ln +
				"  radius: " + radius + ln +
				"  n: " + n + ln +
				"  CylDiv: " + ((Cylinder) world.objects.get(idPrefix + "CYLINDER_0")).divisions + ln +
				"  SphDiv: " + ((Sphere) world.objects.get(idPrefix + "SPHERE")).divisions + ln +
				"  TotalVol: ";
		
		BigDecimal bd = BigDecimal.valueOf(RAMUtil.getTotalVolume(yList, (radius + radius) / n));
		infoData += bd.toPlainString();
		
		Label tmpLabel = ((Label) gui.getActor(idPrefix + "INFOLABEL"));
		tmpLabel.setText(infoData);
		tmpLabel.setPosition(10, Gdx.graphics.getHeight() - (tmpLabel.getPrefHeight() / 2) - 5);
	}
	
	public void dispose(){
		gui.dispose();
	}
}