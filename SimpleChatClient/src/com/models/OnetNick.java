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

import java.util.List;
import java.util.Map;

public class OnetNick {
    String avatar;
    List<String> channels;
    Map<String, String> channel_modes;
    Map<String, Integer> channel_max_modes;
    Character sex;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public Map<String, String> getChannel_modes() {
        return channel_modes;
    }

    public void setChannel_modes(Map<String, String> channel_modes) {
        this.channel_modes = channel_modes;
    }

    public Map<String, Integer> getChannel_max_modes() {
        return channel_max_modes;
    }

    public void setChannel_max_modes(Map<String, Integer> channel_max_modes) {
        this.channel_max_modes = channel_max_modes;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }
}
