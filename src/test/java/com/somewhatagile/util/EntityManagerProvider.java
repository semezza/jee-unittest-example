package com.somewhatagile.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProvider {

    private EntityManager em;
    private static EntityManagerProvider instance;

    private EntityManagerProvider() {
        em = Persistence.createEntityManagerFactory("testUnit").createEntityManager();
    }

    public static EntityManagerProvider getInstance() {
        if (instance == null) {
            instance = new EntityManagerProvider();
        }
        return instance;
    }

    public EntityManager getEm() {
        return em;
    }
}
