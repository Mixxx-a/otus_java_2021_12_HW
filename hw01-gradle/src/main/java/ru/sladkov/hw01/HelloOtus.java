package ru.sladkov.hw01;

import com.google.common.base.Joiner;

import java.util.Arrays;

public class HelloOtus {
    public static void main(String[] args) {
        System.out.println(Joiner.on(",")
                .skipNulls()
                .join(Arrays.asList(1, 2, 3, null, 5, 6)));
    }
}
