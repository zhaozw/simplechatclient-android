/*
 * Simple Chat Client
 *
 *   Copyright (C) Piotr Łuczko <piotr.luczko@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.models;

import java.util.ArrayList;

public class ChannelsFavourites {

	private ArrayList<String> channelFavourites = new ArrayList<String>();

    private static ChannelsFavourites instance = new ChannelsFavourites();
    public static synchronized ChannelsFavourites getInstance() { return instance; }

    public ChannelsFavourites()
    {
    }
	
	public void add(String channel)
	{
		channelFavourites.add(channel);
	}
	
	public void remove(String channel)
	{
		if (channelFavourites.contains(channel))
		{
			channelFavourites.remove(channelFavourites.indexOf(channel));
		}
	}
	
	public void clear()
	{
		if (channelFavourites != null)
			channelFavourites.clear();
	}

	public ArrayList<String> get()
	{
		return channelFavourites;
	}
}
