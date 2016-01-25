package com.bigbass1997.sphereram.skins;

import com.bigbass1997.sphereram.fonts.FontID;

public class SkinID {
	
	public FontID fontID;
	
	public SkinID(FontID fontID){
		this.fontID = fontID;
	}
	
	public String toString(){
		return "skin." + fontID.toString();
	}
}
