package com.minyu.knowledge.sea.algorithm;

import java.util.HashMap;
import java.util.Map;

public class LeetCode0002 {

    public int romanToInt(String s) {
        s = s.replace("IV", "a");
        s = s.replace("IX", "b");
        s = s.replace("XL", "c");
        s = s.replace("XC", "d");
        s = s.replace("CD", "e");
        s = s.replace("CM", "f");

        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            res += getValue(s.charAt(i));
        }
        return res;
    }

    public int getValue(char c) {
        switch (c) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            case 'a':
                return 4;
            case 'b':
                return 9;
            case 'c':
                return 40;
            case 'd':
                return 90;
            case 'e':
                return 400;
            case 'f':
                return 900;
        }
        return 0;
    }


    Map<Character, Integer> symbolValues = new HashMap<Character, Integer>() {{
        put('I', 1);
        put('V', 5);
        put('X', 10);
        put('L', 50);
        put('C', 100);
        put('D', 500);
        put('M', 1000);
    }};

    public int romanToInt2(String s) {
        char[] charArray = s.toCharArray();
        int number = 0;
        for (int i = 0; i < charArray.length; i++) {
            int left = symbolValues.get(charArray[i]);
            if (i == charArray.length - 1) {
                number = number + left;
                break;
            }
            int right = symbolValues.get(charArray[i + 1]);
            if (left < right) {
                left = -left;
            }
            number = number + left;
        }
        return number;
    }

}
