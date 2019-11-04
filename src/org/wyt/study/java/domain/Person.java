package org.wyt.study.java.domain;


@MyAnnotation(a1=1, value="123", name="XXX")
public class Person {
    public String type = "type";
    private String name;
    private int age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    private void show(){
        System.out.println("person private");
    }

    @Override
    public String toString() {
        return "Person{" + "type='" + type + '\'' + ", name='" + name + '\'' + ", age=" + age + '}';
    }
}
