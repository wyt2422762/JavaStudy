package org.wyt.study.java;

import org.wyt.study.java.domain.MyAnnotation;
import org.wyt.study.java.domain.Person;
import org.wyt.study.java.domain.Student;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * .getXXX 获取本类及父类和继承接口的公开的构造方法、变量、方法
 * .getDeclaredXXX 获取本类声明的任意构造方法、变量、方法
 */
public class ReflectStudy {

    public static void main(String[] args) {
        Person person = new Person();
        Class cls1 = person.getClass();

        Constructor[] constructors1 = cls1.getConstructors();
        for (Constructor constructor: constructors1){
            System.out.println("Constructor: " + constructor);
        }
        System.out.println("--------------------------------------------------");

        Constructor[] constructors2 = cls1.getDeclaredConstructors();
        for (Constructor constructor: constructors2){
            System.out.println("DeclaredConstructors: " + constructor);
        }
        System.out.println("--------------------------------------------------");

        Annotation[] annotations1 = cls1.getAnnotations();
        for(Annotation annotation: annotations1){
            System.out.println("Annotation: " + annotation.toString());
            if(annotation.annotationType() == MyAnnotation.class){
                System.out.println("a1 = " + ((MyAnnotation)annotation).a1());
            }
        }
        System.out.println("--------------------------------------------------");

        Field[] fields1 = cls1.getFields();
        for (Field field : fields1) {
            field.setAccessible(true);//忽略作用域
            System.out.println("Fields: " + field);
        }
        System.out.println("--------------------------------------------------");

        Field[] fields2 = cls1.getDeclaredFields();
        for (Field field : fields2) {
            field.setAccessible(true);//忽略作用域
            System.out.println("DeclaredFields: " + field);
        }
        System.out.println("--------------------------------------------------");

        Method[] methods1 = cls1.getMethods();
        for (Method method: methods1){
            System.out.println("Method: " + method);
        }
        System.out.println("--------------------------------------------------");
        Method[] methods2 = cls1.getDeclaredMethods();
        for (Method method: methods2){
            System.out.println("DeclaredMethod: " + method);
        }

        System.out.println("\n========================================================\n");

        Student student = new Student("12", 123);
        Class cls2 = student.getClass();

        Constructor[] constructors3 = cls2.getConstructors();
        for (Constructor constructor: constructors3){
            System.out.println("Constructor: " + constructor);
        }
        System.out.println("--------------------------------------------------");

        Constructor[] constructors4 = cls2.getDeclaredConstructors();
        for (Constructor constructor: constructors4){
            System.out.println("DeclaredConstructors: " + constructor);
        }
        System.out.println("--------------------------------------------------");

        Annotation[] annotations3 = cls2.getAnnotations();
        for(Annotation annotation: annotations3){
            System.out.println("Annotation: " + annotation.toString());
            if(annotation.annotationType() == MyAnnotation.class){
                System.out.println("a1 = " + ((MyAnnotation)annotation).a1());
            }
        }
        System.out.println("--------------------------------------------------");

        Field[] fields3 = cls2.getFields();
        for (Field field : fields3) {
            field.setAccessible(true);//忽略作用域
            System.out.println("Fields: " + field);
        }
        System.out.println("--------------------------------------------------");

        Field[] fields4 = cls2.getDeclaredFields();
        for (Field field : fields4) {
            field.setAccessible(true);//忽略作用域
            System.out.println("DeclaredFields: " + field);
        }
        System.out.println("--------------------------------------------------");

        Method[] methods3 = cls2.getMethods();
        for (Method method: methods3){
            System.out.println("Method: " + method);
        }
        System.out.println("--------------------------------------------------");

        Method[] methods4 = cls2.getDeclaredMethods();
        for (Method method: methods4){
            System.out.println("DeclaredMethod: " + method);
        }
    }
}
