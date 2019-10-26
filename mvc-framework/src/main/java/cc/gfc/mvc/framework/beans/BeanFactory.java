package cc.gfc.mvc.framework.beans;


import cc.gfc.mvc.framework.annotation.MyAutoWired;
import cc.gfc.mvc.framework.annotation.MyBean;
import cc.gfc.mvc.framework.annotation.MyController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoguo
 * @ClassName: BeanFactory
 * @Description:
 * @CreatedBy: fcguo
 * @CreatedAt: 6/21/19 10:23 AM
 **/
public class BeanFactory {

    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    public static Object getBean(Class<?> cls) {
        return classToBean.get(cls);
    }

    public static void initBean(List<Class<?>> classList) throws Exception {
        //toCreate存放了所有类的类型
        List<Class<?>> toCreate = new ArrayList<>(classList);
        //用于存放那些还没创建成功的bean，比如按照顺序先要实例化A，但是A依赖B，B却没有被实例化，那么A
        //自然就没法实例化，所以会把A对象暂时存放在这里。 class<A> --> 未创建完成的A对象
        Map<Class<?>, Object> notCreateFinishedBeanMap = new HashMap<>();
        while (toCreate.size() != 0) {
            //在这一轮创建之前，先记录列表里面还有多少个类
            int size = toCreate.size();

            //下面就是遍历toCreate中的每一个class，如果它不需要实例化或者能对其实例化成功就将其移除出队列，
            //不行的话就保留并添加到notCreateFinishedBeanMap中。
            //注意这里涉及到删除操作，所以就不能简单的使用for循环了。
            int i = 0;
            while (toCreate.size() > i) {
                if (notNeedCreateBean(toCreate.get(i)) || finishCreateBean(toCreate.get(i), notCreateFinishedBeanMap)) {
                    toCreate.remove(i);
                } else {
                    // 这个i记录了，没有被实例化的class的数量
                    i++;
                }
            }
            //这一轮创建完成后，查看是否列表中的元素有减少，减少了说明这一轮有bean创建成功，
            //没有减少说明没有bean可以被成功创建 (也就是出现了cycle dependency)
            if (toCreate.size() == size) {
                throw new Exception("cycle dependency");
            }
        }
    }

    /**
     * class没有注解的话，我们就不需要实例化这些对象
     * @param aClass
     * @return
     */
    private static boolean notNeedCreateBean(Class<?> aClass) {
        return !(aClass.isAnnotationPresent(MyBean.class) || aClass.isAnnotationPresent(MyController.class));
    }

    /**
     * 能够实例化成功当前对象，则完成实例化并添加到classToBean容器中，返回true
     * 如果当前类实例依赖于其他对象但是此对象还没有出现在容器中，那么当前类实例添加到notCreateFinishedBeanMap，返回false
     * @param aClass
     * @param notCreateFinishedBeanMap
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static boolean finishCreateBean(Class<?> aClass, Map<Class<?>, Object> notCreateFinishedBeanMap)
            throws IllegalAccessException, InstantiationException {
        //首先检查一下这个类的对象是否出存在于notCreateFinishedBeanMap中
        Object instance = notCreateFinishedBeanMap.get(aClass);
        if (instance == null) {
            instance = aClass.newInstance();
        }
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MyAutoWired.class)) {
                Object bean = BeanFactory.getBean(field.getType());
                //判断容器中是否已有它所依赖的实例
                if (bean == null) {
                    //there not exist the dependent bean in bean factory, then we can not create bean for this aClass
                    //put the not completely-created bean(instance) into notCreateFinishedBeanMap
                    notCreateFinishedBeanMap.put(aClass, instance);
                    //只要出现依赖的bean还未存在，那么就直接返回false
                    return false;
                }
                //如果已经存在它所依赖的bean，那么就注入它
                field.setAccessible(true);
                field.set(instance, bean);

            }
        }
        //将这个类实例化成功的实例对象放入容器
        classToBean.put(aClass, instance);
        return true;
    }
}
