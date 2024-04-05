package utils;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

@Slf4j
public class PersistenceUnitInfoImpl implements PersistenceUnitInfo {
    private final Properties dbProperties;

    @Getter
    private HikariDataSource dataSource;
    private static final int PROCESSORS;
    private static final double SYSTEM_LOAD_AVERAGE;

    static {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        PROCESSORS = Runtime.getRuntime().availableProcessors();
        SYSTEM_LOAD_AVERAGE = osBean.getSystemLoadAverage();
    }

    public PersistenceUnitInfoImpl(){
        dbProperties = new Properties();
        try(var inputStream = getClass().getClassLoader().getResourceAsStream("db.properties")){
            dbProperties.load(inputStream);
        }catch (Exception e){
            log.error("An error occurred while reading db.properties file: " + e.getMessage());
        }

    }
    @Override
    public String getPersistenceUnitName() {
        return "HR";
    }

    @Override
    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        if(dataSource == null){
            dataSource = new HikariDataSource();
            int calculatedPoolSize = calculatePoolSize();
            int minimumIdle = calculateMinimumIdle(calculatedPoolSize);

            dataSource.setJdbcUrl(dbProperties.getProperty("jdbcUrl"));
            dataSource.setUsername(dbProperties.getProperty("username"));
            dataSource.setPassword(dbProperties.getProperty("password"));
            dataSource.setDriverClassName(dbProperties.getProperty("driverClassName"));
            dataSource.setMaximumPoolSize(calculatedPoolSize);
            dataSource.setMinimumIdle(minimumIdle);
            dataSource.setIdleTimeout(Long.parseLong(dbProperties.getProperty("idleTimeout")));
            dataSource.setMaxLifetime(Long.parseLong(dbProperties.getProperty("maxLifetime")));
            dataSource.setConnectionTimeout(Long.parseLong(dbProperties.getProperty("connectionTimeout")));
            dataSource.setPoolName(dbProperties.getProperty("poolName"));
        }
        return dataSource;
    }

    @Override
    public List<String> getMappingFileNames() {
        return null;
    }

    @Override
    public List<URL> getJarFileUrls() {
        return null;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        try {
            return new File(".").toURI().toURL();
        } catch (MalformedURLException e) {
            log.error("An error occurred while getting the root URL: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getManagedClassNames() {
        return List.of(
                "models.entities.Employee",
                "models.entities.Job",
                "models.entities.Vacation",
                "models.entities.Address",
                "models.entities.Attendance",
                "models.entities.Project",
                "models.entities.Department"
        );
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.UNSPECIFIED;
    }

    @Override
    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        return properties;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "3.0";
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        // TODO document why this method is empty
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }

    private static int calculatePoolSize() {
        if (SYSTEM_LOAD_AVERAGE == -1.0) {
            return PROCESSORS;
        } else {
            return (int) Math.max(1, Math.ceil(PROCESSORS * SYSTEM_LOAD_AVERAGE));
        }
    }

    private static int calculateMinimumIdle(int calculatedPoolSize) {
        int minimumIdle = Math.max(1, calculatedPoolSize / 2);
        if (minimumIdle == calculatedPoolSize) {
            minimumIdle--;
        }
        return minimumIdle;
    }
}
