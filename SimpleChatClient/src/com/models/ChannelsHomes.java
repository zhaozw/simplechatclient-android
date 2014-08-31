package com.models;

import java.util.ArrayList;

public class ChannelsHomes {

	private ArrayList<String> channelHomes;
	
	public void add(String channel)
	{
		channelHomes.add(channel);
	}
	
	public void remove(String channel)
	{
		if (channelHomes.contains(channel))
		{
			channelHomes.remove(channelHomes.indexOf(channel));
		}
	}
	
	public void clear()
	{
		channelHomes.clear();
	}
	
	public ArrayList<String> get()
	{
		return channelHomes;
	}
}
