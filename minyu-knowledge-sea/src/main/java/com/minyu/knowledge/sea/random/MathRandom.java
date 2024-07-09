package com.minyu.knowledge.sea.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class MathRandom {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Random random = new Random(12345);
        SecureRandom instance = SecureRandom.getInstanceStrong();
        int num = 0;
        for (int i = 0; i < 100000; i++) {
            int seed = random.nextInt(100);
            if (seed < 50){
                num = num + 1;
            }
        }
        System.out.println(num);
    }
}
