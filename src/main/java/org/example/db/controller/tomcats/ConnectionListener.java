package org.example.db.controller.tomcats;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.example.db.dao.connection.PoolConnections;
import org.example.db.dao.connection.TransactionConnection;

@WebListener
public class ConnectionListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        PoolConnections.getInstance().init(new TransactionConnection());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        PoolConnections.getInstance().destroy();
    }
}
