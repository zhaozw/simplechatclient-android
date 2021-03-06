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

import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static Settings instance = new Settings();
    public static synchronized Settings getInstance() {
        return instance;
    }

    private Map<String, String> map = new HashMap<>();

    private Settings() {
        map.put("first_run", "true");
    }

    public void set(String k, String v) {
        map.put(k, v);
    }
    
    public String get(String k)
    {
        return map.get(k);
    }

    public void clear()
    {
        map.clear();
    }
}
