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

package com.onet;

public class OnetUtils {
    private static int[] append = {29, 43, 7, 5, 52, 58, 30, 59, 26, 35, 35, 49, 45, 4, 22, 4, 0, 7, 4, 30, 51, 39, 16, 6, 32, 13, 40, 44, 14, 58, 27, 41, 52, 33, 9, 30, 30, 52, 16, 45, 43, 18, 27, 52, 40, 52, 10, 8, 10, 14, 10, 38, 27, 54, 48, 58, 17, 34, 6, 29, 53, 39, 31, 35, 60, 44, 26, 34, 33, 31, 10, 36, 51, 44, 39, 53, 5, 56};
    private static int[] arraycopy = {7, 32, 25, 39, 22, 26, 32, 27, 17, 50, 22, 19, 36, 22, 40, 11, 41, 10, 10, 2, 10, 8, 44, 40, 51, 7, 8, 39, 34, 52, 52, 4, 56, 61, 59, 26, 22, 15, 17, 9, 47, 38, 45, 10, 0, 12, 9, 20, 51, 59, 32, 58, 19, 28, 11, 40, 8, 28, 6, 0, 13, 47, 34, 60, 4, 56, 21, 60, 59, 16, 38, 52, 61, 44, 8, 35, 4, 11};
    private static int[] charAt = {60, 30, 12, 34, 33, 7, 15, 29, 16, 20, 46, 25, 8, 31, 4, 48, 6, 44, 57, 16, 12, 58, 48, 59, 21, 32, 2, 18, 51, 8, 50, 29, 58, 6, 24, 34, 11, 23, 57, 43, 59, 50, 10, 56, 27, 32, 12, 59, 16, 4, 40, 39, 26, 10, 49, 56, 51, 60, 21, 37, 12, 56, 39, 15, 53, 11, 33, 43, 52, 37, 30, 25, 19, 55, 7, 34, 48, 36};
    private static int[] length = {11, 9, 12, 0, 1, 4, 10, 13, 3, 6, 7, 8, 15, 5, 2, 14};
    private static int[] toString = {1, 13, 5, 8, 7, 10, 0, 15, 12, 3, 14, 11, 2, 9, 6, 4};

    public static String transform(String paramString) {
        int[] arrayOfInt1 = new int[16];
        int[] arrayOfInt2 = new int[16];

        if (paramString.length() < 16)
            return null;

        for (int i = 0; i < 16; i++) {
            int c = paramString.charAt(i);
            arrayOfInt1[i] = (c > 57 ? c > 90 ? (c - 97) + 36 : (c - 65) + 10 : c - 48);
        }

        for (int i = 0; i < 16; i++)
            arrayOfInt1[i] = append[(arrayOfInt1[i] + i)];

        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, 16);

        for (int i = 0; i < 16; i++)
            arrayOfInt1[i] = ((arrayOfInt1[i] + arrayOfInt2[length[i]]) % 62);

        for (int i = 0; i < 16; i++)
            arrayOfInt1[i] = arraycopy[(arrayOfInt1[i] + i)];

        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, 16);

        for (int i = 0; i < 16; i++)
            arrayOfInt1[i] = ((arrayOfInt1[i] + arrayOfInt2[toString[i]]) % 62);

        for (int i = 0; i < 16; i++)
            arrayOfInt1[i] = charAt[(arrayOfInt1[i] + i)];

        for (int i = 0; i < 16; i++) {
            int j = arrayOfInt1[i];
            arrayOfInt1[i] = j >= 10 ? j >= 36 ? (97 + j) - 36 : (65 + j) - 10 : 48 + j;
        }

        StringBuffer localStringBuffer = new StringBuffer();
        for (int j = 0; j < 16; j++)
            localStringBuffer.append((char)arrayOfInt1[j]);

        return localStringBuffer.toString();
    }
}
