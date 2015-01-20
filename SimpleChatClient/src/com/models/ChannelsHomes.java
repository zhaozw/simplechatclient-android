/*
 * Simple Chat Client
 *
 *   Copyright (C) 2015 Piotr Łuczko <piotr.luczko@gmail.com>
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
