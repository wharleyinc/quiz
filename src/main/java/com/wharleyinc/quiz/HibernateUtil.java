package com.wharleyinc.quiz;

import com.wharleyinc.quiz.domain.*;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {

    /**
     * Generates database create commands for the specified entities using Hibernate native API, SchemaExport.
     * Creation commands are exported into the create.sql file.
     */
    public static void generateSchema() {
        Map<String, String> settings = new HashMap<>();
        settings.put(Environment.URL, "jdbc:postgresql://localhost:5432/quiz?user=postgres&password=Olawale!@");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Quiz.class);
        metadataSources.addAnnotatedClass(Question.class);
        metadataSources.addAnnotatedClass(QueOption.class);
        metadataSources.addAnnotatedClass(User.class);
        metadataSources.addAnnotatedClass(Authority.class);
        Metadata metadata = metadataSources.buildMetadata();

        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setFormat(true);
        schemaExport.setOutputFile("create.sql");
        schemaExport.createOnly(EnumSet.of(TargetType.SCRIPT), metadata);
    }
}
