package us.dingl.fixedenchants.WS;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebServerHandler {
    private final Server server;

    public WebServerHandler(int port) {
        server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new GetGUIs()), "/hello");
    }

    public void start() throws Exception {
        if (!server.isStarted() && !server.isStarting()) {
            server.start();
        }
    }

    public void stop() throws Exception {
        if (!server.isStopped() && !server.isStopping()) {
            server.stop();
        }
    }

    public boolean isRunning() {
        return server.isStarted();
    }
}