package org.swordapp.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.swordapp.server.servlets.CollectionServletDefault;
import org.swordapp.server.servlets.ServiceDocumentServletDefault;

public class SwordTest {

    protected SwordTestImpl testImpl;
    protected Server server;

    public void startJetty() throws Exception {
        server = new Server(SwordTestData.TEST_PORT);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(SwordTestData.TEST_PORT);
        server.setConnectors(new Connector[] {connector});

        WebAppContext context = new WebAppContext();
        context.setClassLoader(this.getClass().getClassLoader());
        context.setContextPath("/");
        context.setInitParameter("service-document-impl", SwordTestImpl.class.getName());
        context.setInitParameter("config-impl", SwordTestImpl.class.getName());
        context.setInitParameter("container-impl", SwordTestImpl.class.getName());
        context.setInitParameter("statement-impl", SwordTestImpl.class.getName());
        context.setInitParameter("media-resource-impl", SwordTestImpl.class.getName());
        context.setInitParameter("collection-deposit-impl", SwordTestImpl.class.getName());
        context.setInitParameter("collection-list-impl", SwordTestImpl.class.getName());
        context.setResourceBase("./");

        ServletHandler servletHandler = new ServletHandler();

        final ServletHolder sdServlet = new ServletHolder("SD", ServiceDocumentServletDefault.class);
        servletHandler.addServletWithMapping(sdServlet,"/sword/sd");
        final ServletHolder colServlet = new ServletHolder("COL", CollectionServletDefault.class);
        servletHandler.addServletWithMapping(colServlet,"/sword/col/*");

        context.setServletHandler(servletHandler);
        server.setHandler(context);
        server.start();

        while (server.isStarting()){
            Thread.sleep(1000);
        }
    }

    protected void stopJetty() throws Exception {
        server.stop();
        while (server.isStopping()){
            Thread.sleep(1000);
        }
    }

    public Server getServer() {
        return server;
    }

    public SwordTestImpl getTestImpl() {
        return testImpl;
    }
}
