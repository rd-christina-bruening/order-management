package com.greyscale.ordermanagement;

import com.greyscale.ordermanagement.repository.OrderRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureObservability
@Import(AbstractServiceTest.TestConfig.class)
@ActiveProfiles({"service-suite-container"})
public abstract class AbstractServiceTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected OrderRepository orderRepository;

    public static final PostgreSQLContainer POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer().withDatabaseName("postgres");
        POSTGRES.start();
    }

    @Profile("service-suite-container")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public PropertySourcesPlaceholderConfigurer testContainerConfig() {
            final PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
            Properties properties = new Properties();

            properties.setProperty("spring.datasource.url", POSTGRES.getJdbcUrl() + "?useSSL=false&useUnicode=yes&characterEncoding=UTF-8");
            properties.setProperty("spring.datasource.username", POSTGRES.getUsername());
            properties.setProperty("spring.datasource.password", POSTGRES.getPassword());

            placeholderConfigurer.setProperties(properties);
            placeholderConfigurer.setLocalOverride(true);
            return placeholderConfigurer;
        }

    }
}
