package org.wyt.study.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * lambda表达式基本应用
 */
public class LambdaStudy {

    public static void main(String[] args) {
        demo2();
    }

    public static void demo1() {
        //传统方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("传统方法");
            }
        }, "传统").start();
        //lambda方法
        new Thread(() -> System.out.println("lambda方法"), "lambda").start();
    }

    public static void demo2() {
        //传统方法
        List<String> list1 = Arrays.asList("m", "a", "b", "d", "c", "z");
        Collections.sort(list1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.hashCode() - o2.hashCode();
            }
        });
        System.out.println(list1);
        //lambda方法
        List<String> list2 = Arrays.asList("m", "a", "b", "d", "c", "z");
        Collections.sort(list2, (o1, o2) -> o1.hashCode() - o2.hashCode());
        System.out.println(list2);
    }

    public static void demo3() {
        //传统方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("步骤1");
                System.out.println("步骤2");
                System.out.println("步骤3");
            }
        }, "传统").start();
        //lambda方法
        new Thread(() -> {
            System.out.println("步骤1");
            System.out.println("步骤2");
            System.out.println("步骤3");
        }, "lambda").start();
    }

}


