package cc.gfc.mvc.framework.handler;

import cc.gfc.mvc.framework.annotation.MyController;
import cc.gfc.mvc.framework.annotation.MyRequestMapping;
import cc.gfc.mvc.framework.annotation.MyRequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaoguo
 * @ClassName: MyMappingHandlerManager
 * @Description:
 * @CreatedAt: 6/19/19 4:39 PM
 **/
public class MyMappingHandlerManager {
    /**
     * MappingHandler列表，一个MappingHandler可以认为是一对 请求uri-->执行方法 的封装
     */
    public static List<MyMappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 在解析mapping的时候，只会讲controller进行解析
     * @param classList 是类扫描时扫描到的所有的类。
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for (Class<?> aClass : classList) {
            if (aClass.isAnnotationPresent(MyController.class)) {
                parseMappingHandlerFromController(aClass);
            }
        }
    }

    /**
     *
     * @param controllerClass
     */
    private static void parseMappingHandlerFromController(Class<?> controllerClass) {
        // get all methods
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyRequestMapping.class)) {
                //获取到当前方法上@MyRequestMapping注解中的请求uri
                String mappingUri = method.getDeclaredAnnotation(MyRequestMapping.class).value();
                List<String> requestParamList = new LinkedList<>();
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.isAnnotationPresent(MyRequestParam.class)) {
                        //获取到当前方法中@MyRequestParam所需要的请求参数的名字，并将其添加到当前方法的请求参数列表中
                        requestParamList.add(parameter.getDeclaredAnnotation(MyRequestParam.class).value());
                    } else {
                        //如果没有用@MyRequestParam注解，那使用方法参数的名字
                        requestParamList.add(parameter.getName());
                    }
                }
                String[] requestParams = requestParamList.toArray(new String[requestParamList.size()]);
                //对每一个uri和method的映射封装一个MyMappingHandler对象（其中还包括了方法所述的controller以及方法需要的参数名列表），
                // 并放入mappingHandlerList中。
                MyMappingHandler mappingHandler = new MyMappingHandler(mappingUri, method, controllerClass, requestParams);
                mappingHandlerList.add(mappingHandler);
            }
        }
    }
}
