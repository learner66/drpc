# drpc
一个rpc框架实现
1. rpc-demo-comon解析
  1.1. ReflectionUtils
    public  class ReflectionUtils {
    /**
     * 根据class创建对象
     * @param clazz 待创建对象
     * @param <T> 对象类型
     * @return 创建好的对象
     */
    public static <T> T newInstance(Class<T> clazz){
        try {
            // 通过类对象来创建一个该类型的对象
            return clazz.newInstance();
        } catch (Exception e) {
           throw new IllegalStateException(e);
        }
    }

    public static Method[] getPublicMethods(Class clazz){
        // 通过getDeclaredMethods()来获取该类型的所有声明的方法
        Method[] methods = clazz.getDeclaredMethods();
        // pmethods中只包含public修饰的方法
        List<Method> pmethods = new ArrayList<>();
        for(Method m : methods){
            if(Modifier.isPublic(m.getModifiers())){
                pmethods.add(m);
            }
        }
        return pmethods.toArray(new Method[0]);
    }

    /**
     * 调用指定对象的指定方法
     * @param obj 被调用的对象
     * @param method 被调用的方法
     * @param args 被调用的参数
     * @return
     */
    public static Object invoke(Object obj,Method method, Object... args){
        try {
           return method.invoke(obj,args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
  
