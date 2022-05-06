package com.example.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] numbers = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] ints = solution.calSlidingWindowsArr(numbers, 3);
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }
    }

    public int[] calSlidingWindowsArr(int[] numbers, int k) {
        if (numbers == null || numbers.length < k) {
            int[] result = {};
            return result;
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < numbers.length; i++) {
            if (i > numbers.length - k) {
                break;
            }
            List<Integer> temp = new ArrayList<>();
            for (int j = i; j < k + i; j++) {
                temp.add(numbers[j]);
            }
            Collections.sort(temp);
            list.add(temp.get(temp.size() - 1));
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}