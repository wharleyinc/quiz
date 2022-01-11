package com.wharleyinc.quiz;

import com.wharleyinc.quiz.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableJpaRepositories
public class QuizApplication {

    private static final Logger log = LoggerFactory.getLogger(QuizApplication.class);

    private final Environment env;

    public QuizApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QuizApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.debug("This server is running on with profile: {}, \n  with this port; {}", env.getActiveProfiles(), env.getProperty("server.port"));

    }

}
