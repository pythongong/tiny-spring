package com.pythongong.test.aop.valid;

import java.util.ArrayList;
import java.util.List;

public class AdviceOrder {
    public final static List<String> ORDER = new ArrayList<>();

    public static void execute(String adivce) {
        System.out.println(adivce);
        ORDER.add(adivce);
    }
}
