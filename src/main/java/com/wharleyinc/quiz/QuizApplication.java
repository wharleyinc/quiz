package com.wharleyinc.quiz;

import com.wharleyinc.quiz.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableJpaRepositories
public class QuizApplication {

    public static void main(String[] args) {
        HibernateUtil.generateSchema();
        SpringApplication.run(QuizApplication.class, args);
    }

}
