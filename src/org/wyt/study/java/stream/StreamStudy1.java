package org.wyt.study.java.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/*
    java.util.stream.Stream 学习
    jdk1.8 之后新增的集合处理方法
 */
public class StreamStudy1 {
    public static void main(String[] args) throws Exception{
        stream_filter();
        System.out.println("--------------------------------------------");
        stream_map();
        System.out.println("--------------------------------------------");
        stream_count();
        System.out.println("--------------------------------------------");
        stream_limit();
        System.out.println("--------------------------------------------");
        stream_concat();
    }

    /**
     * <R> Stream<R> map(Function<? super T, ? extends R> mapper);
     * Stream.map 方法可以转换相应的数据为另一种格式（映射）
     * Function R apply(T t); 将T类型转换为R类型
     */
    private static void stream_map() {
        List<String> nums = Arrays.asList("5", "4", "3", "2", "1", "0");
        Stream<List<String>> nums1 = Stream.of(nums);
        Stream<String> stream = nums.stream();
        nums1.map(list->list.toString()).forEach(str->System.out.println(str));
        nums.stream().map(str->Integer.parseInt(str) - 1).forEach(num-> System.out.println(num));
    }

    /**
     * Stream<T> filter(Predicate<? super T> predicate);
     * Stream.filter 方法可以筛选出符合条件的数据
     * Predicate boolean test(T t);
     */
    private static void stream_filter() {
        List<String> names = Arrays.asList("张无忌", "周芷若", "谢逊", "张三", "王五", "赵匡胤", "张00");
        names.stream()
                .filter(name -> name.startsWith("张"))//筛选以张开头的字符串
                .filter(name -> name.length() == 3)//筛选长度为3的字符串
                .forEach(name -> System.out.println(name));//打印筛选后的字符串
    }

    /**
     * long count();
     * Stream.count 返回流中内容的个数
     * count()是一个终结方法
     */
    private static void stream_count(){
        List<String> names = Arrays.asList("张无忌", "周芷若", "谢逊", "张三", "王五", "赵匡胤", "张三丰");
        long count_filtered = names.stream().filter(name -> name.startsWith("张"))//筛选以张开头的字符串
                .filter(name -> name.length() == 3)//筛选长度为3的字符串
                .count();//打印筛选后的结合的长度
        System.out.println("筛选后的集合个数：" + count_filtered);
    }

    /**
     * Stream<T> limit(long maxSize);
     * Stream.limit 截取流中的前n个元素
     */
    private static void stream_limit(){
        List<String> nums = Arrays.asList("5", "4", "3", "2", "1", "0");
        Stream.of("5", "4", "3", "2", "1", "0").limit(3).forEach(str->System.out.println(str));
    }

    /**
     * public static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b)
     * Stream.concat(静态方法) 可以合并两个流
     */
    private static void stream_concat(){
        Stream<String> stream1 = Stream.of("张三");
        Stream<String> stream2 = Stream.of("李四");
        Stream<String> concatedStream = Stream.concat(stream1, stream2);
        concatedStream.forEach(str->System.out.println(str));
    }

}
