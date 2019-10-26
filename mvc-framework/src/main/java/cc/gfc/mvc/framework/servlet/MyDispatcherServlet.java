package cc.gfc.mvc.framework.servlet;

import cc.gfc.mvc.framework.handler.MyMappingHandler;
import cc.gfc.mvc.framework.handler.MyMappingHandlerManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author xiaoguo
 */
public class MyDispatcherServlet extends HttpServlet{


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        for (MyMappingHandler mappingHandler : MyMappingHandlerManager.mappingHandlerList) {
            if(requestURI.equals(mappingHandler.getUri())){
                try {
                    mappingHandler.handle(req, resp);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}