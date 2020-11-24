import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum EnumSingleton {
    INSTANCE;

    public EnumSingleton getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        EnumSingleton singleton1 = EnumSingleton.INSTANCE;
        EnumSingleton singleton2 = EnumSingleton.INSTANCE;
        System.out.println("正常情况下，实例化两个实例是否相同：" + (singleton1 == singleton2));
        Constructor<EnumSingleton> constructor = null;
//        constructor = EnumSingleton.class.getDeclaredConstructor();
        constructor = EnumSingleton.class.getDeclaredConstructor(String.class, int.class);//其父类的构造器
        constructor.setAccessible(true);
        EnumSingleton singleton3 = null;
        //singleton3 = constructor.newInstance();
        singleton3 = constructor.newInstance("testInstance", 66);
        System.out.println(singleton1 + "\n" + singleton2 + "\n" + singleton3);
        System.out.println("通过反射攻击单例模式情况下，实例化两个实例是否相同：" + (singleton1 == singleton3));
    }
}
