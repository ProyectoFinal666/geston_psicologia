package org.example.gestion_psicologia.util;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "ClinicaPU"; // Actualizado al nombre de tu persistence unit
    private static EntityManagerFactory factory;

    public static EntityManager getEntityManager() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory.createEntityManager();
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}