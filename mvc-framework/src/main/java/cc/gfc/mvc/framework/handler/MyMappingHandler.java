package cc.gfc.mvc.framework.handler;

import cc.gfc.mvc.framework.beans.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xiaoguo
 * @ClassName: MyMappingHandler
 * @Description:
 * @CreatedBy: fcguo
 * @CreatedAt: 6/19/19 4:36 PM
 **/
public class MyMappingHandler {
    private String uri;
    private Method method;
    private Class<?> controllerClass;
    private String[] requestParams;

    public MyMappingHandler(String uri, Method method, Class<?> controllerClass, String[] requestParams) {
        this.uri = uri;
        this.method = method;
        this.controllerClass = controllerClass;
        this.requestParams = requestParams;
    }

    public String getUri() {
        return uri;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public String[] getRequestParams() {
        return requestParams;
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp)
            throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String[] args = new String[this.requestParams.length];
        for (int i = 0; i < args.length; i++) {
            //根据方法需要的参数的名称去从request中获取对应的参数值
            args[i] = req.getParameter(requestParams[i]);
        }
        Object controller = BeanFactory.getBean(controllerClass);
        //反射的方式执行方法时需要方法所处的对象实例，也就是我们已经创建好的Controller bean
        Object result = this.method.invoke(controller, (Object[]) args);
        String resultString = result.toString();
        //将结果返回。这里返回的方式也很简单，直接写入字符串
        resp.getWriter().print(resultString);
    }
}
