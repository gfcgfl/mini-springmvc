package cc.gfc.mvc.framework.starter;

import cc.gfc.mvc.framework.beans.BeanFactory;
import cc.gfc.mvc.framework.core.ClassScanner;
import cc.gfc.mvc.framework.handler.MyMappingHandler;
import cc.gfc.mvc.framework.handler.MyMappingHandlerManager;
import cc.gfc.mvc.framework.server.TomcatServer;

import java.util.List;

/**
 * @author xiaoguo
 * @ClassName: MVCApplication
 * @Description:
 * @CreatedAt: 6/18/19 8:45 PM
 **/
public class MVCApplication {
    public static void run(Class<?> cls) {
        System.out.println("=============================start!!!!==========================");

        TomcatServer tomcatServer = new TomcatServer();
        try {
            tomcatServer.startServer();

            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            BeanFactory.initBean(classList);
            MyMappingHandlerManager.resolveMappingHandler(classList);

            for (MyMappingHandler myMappingHandler : MyMappingHandlerManager.mappingHandlerList) {
                System.out.println(myMappingHandler.getUri() + ":" + myMappingHandler.getMethod());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
