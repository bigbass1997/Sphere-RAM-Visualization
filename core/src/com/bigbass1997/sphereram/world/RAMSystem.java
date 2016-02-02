package com.bigbass1997.sphereram.world;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.bigbass1997.sphereram.graphics.SystemGUI;
import com.bigbass1997.sphereram.ram.RAMUtil;
import com.bigbass1997.sphereram.skins.SkinManager;

public class RAMSystem {
	
	public static enum Method {
		LEFT, MIDDLE, RIGHT
	}
	
	private final String id, idPrefix;
	
	private World world;
	public float radius, oldRadius;
	public int n, oldN, sphereColor, cylColor;

	private TextField tmpField;
	private Label tmpLabel;
	private SelectBox<String> tmpSelectBox;
	
	public Method method, oldMethod;

	private SystemGUI gui;
	private final String[] inputIds = new String[]{"Radius", "Cylinders"};
	
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
		
		oldRadius = this.radius;
		oldN = this.n;
		oldMethod = this.method;
		
		gui = new SystemGUI();
		
		float buf = 5f;
		
		//SYSTEM TEXT FIELDS + LABELS\\
		for(int i = 0; i < inputIds.length; i++){
			tmpField = new TextField("5", SkinManager.getSkin("fonts/computer.ttf", 26));
			tmpField.setWidth(60);
			tmpField.setPosition(Gdx.graphics.getWidth() - tmpField.getWidth() - buf, Gdx.graphics.getHeight() - ((buf + tmpField.getHeight()) * (i + 1)));
			gui.addActor(idPrefix + "INPUTFIELD_" + inputIds[i], tmpField);
			
			tmpLabel = new Label(inputIds[i] + ":", SkinManager.getSkin("fonts/computer.ttf", 28));
			tmpLabel.setColor(Color.WHITE);
			tmpLabel.setAlignment(Align.right);
			tmpLabel.setPosition(
					tmpField.getX() - tmpLabel.getPrefWidth() - 2,
					tmpField.getY() + ((tmpLabel.getHeight() - tmpLabel.getStyle().font.getLineHeight()) * 2)
			);
			gui.addActor(idPrefix + "LABEL_" + inputIds[i], tmpLabel);
		}
		//-------//
		
		//CAMERA TEXT FIELD + LABEL\\
		tmpField = new TextField("20", SkinManager.getSkin("fonts/computer.ttf", 20));
		tmpField.setWidth(35);
		tmpField.setPosition(Gdx.graphics.getWidth() - tmpField.getWidth() - buf, buf);
		gui.addActor(idPrefix + "INPUTFIELD_CamSpeed", tmpField);
		
		tmpLabel = new Label("CamSpeed:", SkinManager.getSkin("fonts/computer.ttf", 20));
		tmpLabel.setColor(Color.WHITE);
		tmpLabel.setAlignment(Align.right);
		tmpLabel.setPosition(
				tmpField.getX() - tmpLabel.getPrefWidth() - 2,
				tmpField.getY() + ((tmpLabel.getHeight() - tmpLabel.getStyle().font.getLineHeight()) * 2)
		);
		gui.addActor(idPrefix + "LABEL_CamSpeed", tmpLabel);
		//-------//
		
		//METHOD SELECT BOX + LABEL\\
		tmpSelectBox = new SelectBox<String>(SkinManager.getSkin("fonts/computer.ttf", 26));
		tmpSelectBox.setWidth(70);
		tmpSelectBox.setPosition(Gdx.graphics.getWidth() - tmpSelectBox.getWidth() - buf, Gdx.graphics.getHeight() - ((buf + tmpSelectBox.getHeight()) * (inputIds.length + 1)) + buf);
		tmpSelectBox.setItems("LEFT", "MIDDLE", "RIGHT");
		gui.addActor(idPrefix + "SELECTBOX_Method", tmpSelectBox);
		
		tmpLabel = new Label("Method:", SkinManager.getSkin("fonts/computer.ttf", 28));
		tmpLabel.setColor(Color.WHITE);
		tmpLabel.setAlignment(Align.right);
		tmpLabel.setPosition(
				tmpSelectBox.getX() - tmpLabel.getPrefWidth() - 2,
				tmpSelectBox.getY() + ((tmpLabel.getHeight() - tmpLabel.getStyle().font.getLineHeight()) * 2)
		);
		gui.addActor(idPrefix + "LABEL_Method", tmpLabel);
		//-------//
		
		//Data Label
		gui.addActor(idPrefix + "INFOLABEL", new Label("", SkinManager.getSkin("fonts/computer.ttf", 24)));
		gui.getActor(idPrefix + "INFOLABEL").setColor(Color.WHITE);
		
		recreate();
	}
	
	public void recreate(){
		//Run checks on current variables in case of errors
		if(radius < 0.1f || n < 2){
			radius = oldRadius;
			n = oldN;
			return;
		}
		
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
		boolean toRecreate = false;
		
		//TextField for CamSpeed
		tmpField = (TextField) gui.getActor(idPrefix + "INPUTFIELD_CamSpeed");
		try {
			world.camSpeed = Float.valueOf(tmpField.getText());
		} catch(NumberFormatException e) {}
		
		//TextField for Radius
		tmpField = (TextField) gui.getActor(idPrefix + "INPUTFIELD_Radius");
		try {
			radius = Float.valueOf(tmpField.getText());
		} catch(NumberFormatException e) {}
		if(oldRadius != radius) toRecreate = true;
		
		//TextField for Cylinders
		tmpField = (TextField) gui.getActor(idPrefix + "INPUTFIELD_Cylinders");
		try {
			n = Integer.valueOf(tmpField.getText());
			if(n > 10000) n = oldN;
		} catch(NumberFormatException e) {}
		if(oldN != n) toRecreate = true;
		
		//SelectBox for Method
		try {
			@SuppressWarnings("unchecked")
			SelectBox<String> tmpbox = (SelectBox<String>) gui.getActor(idPrefix + "SELECTBOX_Method");
			method = Method.valueOf(tmpbox.getSelected());
		} catch(Exception e) {
			method = oldMethod;
		}
		if(!oldMethod.equals(method)) toRecreate = true;
		
		if(toRecreate) recreate();
		oldRadius = radius;
		oldN = n;
		oldMethod = method;
		
		gui.update(delta);
		
		String infoData =
				"System Data:" + ln +
				"  radius: " + radius + ln +
				"  n: " + n + ln +
				"  CylDiv: " + ((Cylinder) world.objects.get(idPrefix + "CYLINDER_0")).divisions + ln +
				"  SphDiv: " + ((Sphere) world.objects.get(idPrefix + "SPHERE")).divisions + ln +
				"  TotalVol: ";
		
		BigDecimal bd = BigDecimal.valueOf(RAMUtil.getTotalVolume(yList, (radius + radius) / n));
		infoData += bd.toPlainString() + ln;
		
		infoData += "  Method: " + method.name();
		
		Label tmpLabel = ((Label) gui.getActor(idPrefix + "INFOLABEL"));
		tmpLabel.setText(infoData);
		tmpLabel.setPosition(10, Gdx.graphics.getHeight() - (tmpLabel.getPrefHeight() / 2) - 5);
	}
	
	public void dispose(){
		gui.dispose();
	}
}