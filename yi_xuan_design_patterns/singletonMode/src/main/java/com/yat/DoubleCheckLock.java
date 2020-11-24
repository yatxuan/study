package com.yat;

/**
 * <p>Description: 双检锁:又叫双重校验锁，综合了懒汉式和饿汉式两者的优缺点整合而成。
 * 看上面代码实现中，特点是在synchronized关键字内外都加了一层 if 条件判断，
 * 这样既保证了线程安全，又比直接上锁提高了执行效率，还节省了内存空间。 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 15:58
 */
public class DoubleCheckLock {


    private static DoubleCheckLock instance;

    private DoubleCheckLock() {
    }

    public static DoubleCheckLock getInstance() {
        if (null == instance) {
            synchronized (DoubleCheckLock.class) {
                if (null == instance) {
                    instance = new DoubleCheckLock();
                }
            }
        }
        return instance;
    }
}
