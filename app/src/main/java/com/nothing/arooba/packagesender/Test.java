package com.nothing.arooba.packagesender;

import java.util.ArrayList;

public class Test {
    private ArrayList<String> list;

    public Test() {
        list = new ArrayList<>();
        list.add("1");
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.list.add(1, "2");
        System.out.println(test.list.get(1));
    }
}
