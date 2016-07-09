 package com.somewhatagile.dao;

import com.somewhatagile.model.Product;

import javax.persistence.EntityManager;
import java.util.List;

 public class ProductDAO {

    public Product create(EntityManager em, Product product) {
        if (product != null) {
            em.persist(product);
        }
        return product;
    }

    public Product get(EntityManager em, long id) {
        return em.find(Product.class, id);
    }

    public boolean delete(EntityManager em, long id) {
        Product foundProduct = get(em, id);
        if (foundProduct != null) {
            em.remove(foundProduct);
            return true;
        }
        return false;
    }

    public Product update(EntityManager em, Product product) {
        if (product != null) {
            if (product.getId() == null) {
                create(em, product);
            } else {
                Product foundProduct = get(em, product.getId());
                if (foundProduct != null) {
                    em.merge(product);
                }
            }
        }
        return product;
    }

    public List<Product> findAllDiscounted(EntityManager em) {
        return em.createNamedQuery(Product.QUERY_FIND_ALL_DISCOUNTED).getResultList();
    }
}
