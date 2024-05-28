package ru.otus;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class HelloOtus {

    public static void main(String[] args) {
        ArrayList<Integer> objects = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            objects.add(i);
        }

        for (Integer object : objects) {
            System.out.println(object);
        }
    }
}
