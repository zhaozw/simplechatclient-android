/*
 * Simple Chat Client
 *
 *   Copyright (C) 2014 Piotr Łuczko <piotr.luczko@gmail.com>
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

package com.database;

public class DatabaseSetting {

	private int id;
	private String current_profile;
	private String uniuqe_id;
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCurrentProfile() {
		return this.current_profile;
	}
	
	public void setCurrentProfile(String current_profile) {
		this.current_profile = current_profile;
	}
	
	public String getUniqueId() {
		return this.uniuqe_id;
	}
	
	public void setUniqueId(String unique_id) {
		this.uniuqe_id = unique_id;
	}
}
