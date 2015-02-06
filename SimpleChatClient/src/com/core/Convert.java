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

package com.core;

public class Convert {

    private static String[] fonts = {"arial", "times", "verdana", "tahoma", "courier"};
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
