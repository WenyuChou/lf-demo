package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wenyu zhou
 * @version 2023-05-17
 */
public class Test {
    public static void main(String[] args) {
        Integer i = 1 << 1;
        Integer b = 1;

        System.out.println(Objects.equals(i,b));
    }
}
