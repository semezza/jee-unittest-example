package com.somewhatagile.service;

import com.somewhatagile.dao.ProductDAO;
import com.somewhatagile.model.Product;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

@LocalBean
@Stateless
public class ProductService {

    @PersistenceContext
    private EntityManager em;

    ProductDAO dao = new ProductDAO();

    public Product createOrUpdateProduct(Product product) {
        if (product == null) {
            return null;
        }
        if (product.getId() != null) {
            return dao.update(em, product);
        }
        return dao.create(em, product);
    }

    public Double getPriceForProduct(long id) {
        Product product = dao.get(em, id);
        if (product != null) {
            if (product.isDiscounted()) {
                BigDecimal discountedPrice = new BigDecimal(product.getPrice());
                discountedPrice = discountedPrice.multiply(new BigDecimal(0.8d));
                return discountedPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            return product.getPrice();
        }
        return null;
    }
}
