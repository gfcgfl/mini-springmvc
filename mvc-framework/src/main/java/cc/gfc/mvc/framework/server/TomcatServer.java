package cc.gfc.mvc.framework.server;

import cc.gfc.mvc.framework.servlet.MyDispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * @author xiaoguo
 * @ClassName: TomcatServer
 * @Description:
 * @CreatedAt: 6/18/19 9:21 PM
 **/
public class TomcatServer {

    private Tomcat tomcat;

    public TomcatServer() {

    }

    public void startServer() throws LifecycleException {
        this.tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(8080);

        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        MyDispatcherServlet dispatcherServlet = new MyDispatcherServlet();
        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet).setAsyncSupported(true);
        context.addServletMappingDecoded("/", "dispatcherServlet");
        tomcat.getHost().addChild(context);
        tomcat.start();

        Thread awaitThread = new Thread("tomcat_await_thread"){
            @Override
            public void run(){
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

}
