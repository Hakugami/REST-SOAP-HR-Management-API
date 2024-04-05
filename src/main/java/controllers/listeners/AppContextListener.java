package controllers.listeners;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import persistence.manager.DatabaseSingleton;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;


@Slf4j
@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseSingleton.INSTANCE.init();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseSingleton.INSTANCE.closeEntityManagerFactory();

        // Unregister JDBC driver
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                log.error("Error deregistering driver", e);
            }
        }

        AbandonedConnectionCleanupThread.uncheckedShutdown();
    }
}

