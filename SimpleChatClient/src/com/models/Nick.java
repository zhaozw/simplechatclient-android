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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nick {

    private static Nick instance = new Nick();
    public static synchronized Nick getInstance() { return instance; }

    private Map<String, OnetNick> mNicks = new HashMap<>();

    public static final int FLAG_DEV_INT = 64;
    public static final int FLAG_ADMIN_INT = 32;
    public static final int FLAG_OWNER_INT = 16;
    public static final int FLAG_OP_INT = 8;
    public static final int FLAG_HALFOP_INT = 4;
    public static final int FLAG_MOD_INT = 2;
    public static final int FLAG_SCREENER_INT = 1;
    public static final int FLAG_VOICE_INT = 0;
    public static final int FLAG_UNKNOWN_INT = -1;

    public static final String FLAG_REGISTERED = "r";
    public static final String FLAG_AWAY = "a";
    public static final String FLAG_BUSY = "b";
    public static final String FLAG_CAM_PRIV = "V";
    public static final String FLAG_CAM_PUB = "W";
    public static final String FLAG_VOICE = "+";
    public static final String FLAG_SCREENER = "=";
    public static final String FLAG_MOD = "!";
    public static final String FLAG_HALFOP = "%";
    public static final String FLAG_OP = "@";
    public static final String FLAG_OWNER = "`";
    public static final String FLAG_ADMIN = "o";
    public static final String FLAG_DEV = "O";
    public static final String FLAG_BOT = "B";

    public static final Character USER_SEX_MALE = 'M';
    public static final Character USER_SEX_FEMALE = 'F';
    public static final Character USER_SEX_UNKNOWN = 'U';

    public void add(String nick, String channel, String modes)
    {
        // hide OP flag if owner
        if ((modes.contains(FLAG_OWNER)) && (modes.contains(FLAG_OP))) modes = modes.replace(FLAG_OP, "");

        if (mNicks.containsKey(nick))
        {
            List<String> channels = mNicks.get(nick).getChannels();
            if (!channels.contains(channel))
            {
                channels.add(channel);
                mNicks.get(nick).setChannels(channels);

                Map<String, String> channel_modes = mNicks.get(nick).getChannel_modes();
                channel_modes.put(channel, modes);
                mNicks.get(nick).setChannel_modes(channel_modes);

                Map<String, Integer> channel_max_modes = mNicks.get(nick).getChannel_max_modes();
                Integer current_channel_max_modes = create_max_modes(modes);
                channel_max_modes.put(channel, current_channel_max_modes);
                mNicks.get(nick).setChannel_max_modes(channel_max_modes);
            }
        }
        else
        {
            List<String> channels = new ArrayList<>();
            channels.add(channel);

            Map<String, String> channel_modes = new HashMap<>();
            channel_modes.put(channel, modes);

            Integer current_channel_max_modes = create_max_modes(modes);
            Map<String, Integer> channel_max_modes = new HashMap<>();
            channel_max_modes.put(channel, current_channel_max_modes);

            OnetNick NewOnetNick = new OnetNick();
            NewOnetNick.setChannels(channels);
            NewOnetNick.setChannel_modes(channel_modes);
            NewOnetNick.setChannel_max_modes(channel_max_modes);
            NewOnetNick.setAvatar("");
            NewOnetNick.setSex(USER_SEX_UNKNOWN);
            mNicks.put(nick, NewOnetNick);
        }
    }

    public void remove(String nick, String channel)
    {
        // TODO
    }

    public void clear()
    {
        mNicks.clear();
    }

    public void rename(String nick, String new_nick, String display)
    {
        // TODO
    }

    private void removeFromChannel(String channel)
    {
        // TODO
    }

    public void quit(String nick, String display)
    {
        // TODO
    }

    private Integer create_max_modes(String modes)
    {
        if (modes.contains(FLAG_DEV)) { return FLAG_DEV_INT; }
        else if (modes.contains(FLAG_ADMIN)) { return FLAG_ADMIN_INT; }
        else if (modes.contains(FLAG_OWNER)) { return FLAG_OWNER_INT; }
        else if (modes.contains(FLAG_OP)) { return FLAG_OP_INT; }
        else if (modes.contains(FLAG_HALFOP)) { return FLAG_HALFOP_INT; }
        else if (modes.contains(FLAG_MOD)) { return FLAG_MOD_INT; }
        else if (modes.contains(FLAG_SCREENER)) { return FLAG_SCREENER_INT; }
        else if (modes.contains(FLAG_VOICE)) { return FLAG_VOICE_INT; }
        else return FLAG_UNKNOWN_INT;
    }
}
