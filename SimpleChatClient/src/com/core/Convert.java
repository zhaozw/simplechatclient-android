package com.core;

public class Convert {

	private static String[] fonts = {"Arial", "Times", "Verdana", "Tahoma", "Courier"};
	private static String[] colors = {"000000", "623c00", "c86c00", "ff6500", "ff0000", "e40f0f", "990033", "8800ab", "ce00ff", "0f2ab1", "3030ce", "006699", "1a866e", "008100", "959595"};

	public static  String removeColor(String data)
	{
		for (String color : colors) {
			data = data.replace("%C"+color+"%","");
		}
		return data;
	}
	
	public static  String removeFont(String data)
	{
		for (String font : fonts)
		{
			data = data.replaceAll("%Fb?i?:?("+font+")?%", "");
		}

		return data;
	}
	
	public static String simpleConvert(String data)
	{
	    data = removeColor(data);
	    data = removeFont(data);
	    
	    data = data.replaceAll("%I([a-zA-Z0-9_-]+)%", "//$1");

		return data;
	}
}
