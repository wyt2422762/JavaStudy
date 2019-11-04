package org.wyt.study.java.domain;

public class Student extends Person implements Eat{

    private Student(){}

    public Student(String name, int age){
    }

    public void study(){
        System.out.println("study");
    }

    private void show(){
        System.out.println("student private");
    }

    @Override
    public void eat() {
        System.out.println("eat");
    }
}
