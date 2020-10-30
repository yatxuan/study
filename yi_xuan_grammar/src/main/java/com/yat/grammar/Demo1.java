package com.yat.grammar;

import com.yat.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>Description: Stream用法 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 16:31
 */
public class Demo1 {

    private static List<UserEntity> list = new ArrayList<>(16);

    static {
        list.add(new UserEntity("admin", "admin"));
        list.add(new UserEntity("user", "user"));
        list.add(new UserEntity(null, "test"));
    }

    /**
     * <p>stream 说明：</p>
     * <p>Filter（过滤）</p>
     * <p>Map(对元素进行操作)</p>
     * <p>limit(截断)</p>
     * <p>distinct(去重)</p>
     */
    public static void main(String[] args) {
        // filter();
        // map();
        // limit();
        distinct();
        // count();
        // collect();
        // sequenceFlowAndParallelFlow();
    }


    /**
     * 过滤
     */
    private static void filter() {
        // 排除姓名为空的用户
        List<UserEntity> userEntityStream = list.stream()
                .filter(userEntity -> userEntity.getUsername() != null)
                .collect(Collectors.toList());
        System.out.println(userEntityStream);
    }

    /**
     * Map(对元素进行操作)
     */
    private static void map() {
        // 排除姓名为空的用户
        List<String> collect = list.stream()
                .filter(userEntity -> userEntity.getUsername() != null)
                .map(UserEntity::getUsername)
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 截断(对一个Stream进行截断操作，获取其前N个元素，如果原Stream中包含的元素个数小于N，那就获取其所有的元素)
     */
    private static void limit() {
        // 排除姓名为空的用户
        List<UserEntity> limit = list.stream().limit(1).collect(Collectors.toList());
        System.out.println(limit);
    }

    /**
     * 去重(对于Stream中包含的元素进行去重操作（去重逻辑依赖元素的equals方法），新生成的Stream中没有重复的元素)
     */
    private static void distinct() {

        List<String> listStr=new ArrayList<>(16);
        listStr.add("测试");
        listStr.add("测试1");
        listStr.add("测试2");
        listStr.add("测试");

        System.out.println(listStr);

        //去重
        List<String> distinct = listStr.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println(distinct);
    }

    /**
     * 统计(count方法是一个流的终点方法，可使流的结果最终统计，返回int)
     */
    private static void count() {
        // 统计姓名为空的用户
        Stream<UserEntity> userEntityStream = list.stream().filter(userEntity -> userEntity.getUsername() != null);
        System.out.println(userEntityStream.count());
    }

    /**
     * 收集流的结果(collect方法也是一个流的终点方法，可收集最终的结果)
     */
    private static void collect() {
        // 排除姓名为空的用户
        List<UserEntity> limit = list.stream().limit(1).collect(Collectors.toList());
        System.out.println(limit);
    }

    /**
     * 顺序流和并行流 (要使用并行流，只需要.parallel()即可)
     * <p>
     * 并行流原理：
     * List originalList = someData;
     * split1 = originalList(0, mid);//将数据分小部分
     * split2 = originalList(mid,end);
     * new Runnable(split1.process());//小部分执行操作
     * new Runnable(split2.process());
     * List revisedList = split1 + split2;//将结果合并
     * <p>
     * 性能：如果是多核机器，理论上并行流则会比顺序流快上一倍。
     * </p>
     */
    private static void sequenceFlowAndParallelFlow() {
        long t0 = System.nanoTime();

        // 顺序流: 初始化一个范围100万整数流,求能被2整除的数字，toArray（）是终点方法
        int[] a = IntStream.range(0, 1_000_000).filter(p -> p % 2 == 0).toArray();

        long t1 = System.nanoTime();

        // 并行流: 和上面功能一样，这里是用 并行流 来计算
        int[] b = IntStream.range(0, 1_000_000).parallel().filter(p -> p % 2 == 0).toArray();

        long t2 = System.nanoTime();

        // 多核机器的结果是serial: 0.06s, parallel 0.02s，证明并行流确实比顺序流快

        System.out.printf("serial: %.2fs, parallel %.2fs%n", (t1 - t0) * 1e-9, (t2 - t1) * 1e-9);

    }
}
