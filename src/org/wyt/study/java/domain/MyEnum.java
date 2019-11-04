package org.wyt.study.java.domain;

public enum MyEnum {

    e1(1, "张三", 3), e2(2, "李四", 4), e3(3, "王五", 5);

    private int id;
    private String name;
    private int age;

    MyEnum(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId(){
       return id;
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

    @Override
    public String toString() {
        return "MyEnum{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
    }

    public static void main(String[] args) {
        System.out.println(MyEnum.e1.toString());
    }

}
