package com.bigbass1997.sphereram.skins;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigbass1997.sphereram.fonts.FontID;
import com.bigbass1997.sphereram.fonts.FontManager;

public class SkinManager {
	
	private static Hashtable<String, Skin> skins;
	
	public static Skin getSkin(SkinID skinID){
		if(skins == null) skins = new Hashtable<String, Skin>();
		
		if(skins.get(skinID.toString()) == null){
			addSkin(skinID.fontID);
			return getSkin(skinID);
		}
		
		return skins.get(skinID.toString());
	}

	public static Skin getSkin(String name, int size){
		return getSkin(new SkinID(new FontID(name, size)));
	}
	
	public static void addSkin(FontID fontID){
		//TODO Allow specifying the path to the skin files.
		Skin skin = new Skin();
		skin.add("default-font", FontManager.getFont(fontID).font, BitmapFont.class);
		FileHandle fileHandle = Gdx.files.internal("skins/default/skin.json");
		FileHandle atlasFile = Gdx.files.internal("skins/default/skin.atlas");
		skin.addRegions(new TextureAtlas(atlasFile));
		skin.load(fileHandle);
		
		skins.put(new SkinID(fontID).toString(), skin);
	}
}