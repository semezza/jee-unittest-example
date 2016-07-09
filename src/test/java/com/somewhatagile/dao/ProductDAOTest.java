package com.somewhatagile.dao;

import com.somewhatagile.model.Product;
import com.somewhatagile.util.EntityManagerProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

public class ProductDAOTest {
    ProductDAO dao = new ProductDAO();
    EntityManager em;

    @Before
    public void setup() {
        em = EntityManagerProvider.getInstance().getEm();
    }

    @Test
    public void testCreateWithNullDoesReturnNull() {
        assertNull(dao.create(em, null));
    }

    @Test
    public void testCreationOfProduct() {
        em.getTransaction().begin();
        String expectedDescription = "testDescription";
        Product productToCreate = createProduct(expectedDescription);
        Product actualProduct = dao.create(em, productToCreate);
        assertNotNull(actualProduct);
        assertTrue(actualProduct.getId() > 0) ;
        assertEquals(expectedDescription, actualProduct.getDescription());
        em.getTransaction().rollback();
    }

    @Test
    public void testGetWithNonExistingId() {
        long nonExistingId = 43L;
        assertNull(dao.get(em, nonExistingId));
    }

    @Test
    public void testGetWithExistingId() {
        em.getTransaction().begin();
        String expectedDescription = "testDescription";
        Product existingProduct = createProduct(expectedDescription);
        em.persist(existingProduct);
        long existingId = existingProduct.getId();
        Product foundProduct = dao.get(em, existingId);
        assertNotNull(foundProduct);
        assertEquals(expectedDescription, foundProduct.getDescription());
        em.getTransaction().rollback();
    }

    @Test
    public void testDeleteWithNonExistingIdReturnFalse() {
        assertFalse(dao.delete(em, 43L));
    }

    @Test
    public void testDeleteWithExistingIdReturnsTrue() {
        em.getTransaction().begin();
        Product product = new Product();
        em.persist(product);
        long existingid = product.getId();
        em.getTransaction().commit();
        assertNotNull(em.find(Product.class, existingid));
        em.getTransaction().begin();
        assertTrue(dao.delete(em, existingid));
        em.getTransaction().commit();
        assertNull(em.find(Product.class, existingid));
    }

    @Test
    public void testUpdateWithNullReturnsNull() {
        assertNull(dao.update(em, null));
    }

    @Test
    public void testUpdateWithNewProductCreatesProduct() {
        em.getTransaction().begin();
        Product createdProduct = dao.update(em, new Product());
        assertNotNull(createdProduct);
        assertTrue(createdProduct.getId() > 0);
        em.getTransaction().rollback();
    }

    @Test
    public void testUpdatedWithExistingProductUpdatesThatProduct() {
        em.getTransaction().begin();
        String oldDescription = "testDescription";
        Product existingProduct = createProduct(oldDescription);
        em.persist(existingProduct);
        long existingId = existingProduct.getId();
        em.getTransaction().commit();
        String expectedDescription = "newDescription";
        Product product = new Product();
        product.setId(existingId);
        product.setDescription(expectedDescription);
        em.getTransaction().begin();
        dao.update(em, product);
        em.getTransaction().commit();

        Product actualProduct = em.find(Product.class, existingId);
        assertEquals(existingId, actualProduct.getId().longValue());
        assertEquals(expectedDescription, actualProduct.getDescription());
    }

    @Test
    public void testFindAllDiscounted() {
        assertTrue(dao.findAllDiscounted(em).isEmpty());

        em.getTransaction().begin();
        dao.create(em, createProduct("Product 1", false));
        dao.create(em, createProduct("Product 2", true));
        dao.create(em, createProduct("Product 3", true));
        em.getTransaction().commit();

        List<Product> actualDiscountedProducts = dao.findAllDiscounted(em);
        assertEquals(2, actualDiscountedProducts.size());
        assertEquals("Product 2", actualDiscountedProducts.get(0).getDescription());
        assertEquals("Product 3", actualDiscountedProducts.get(1).getDescription());
    }

    private Product createProduct(String description) {
        return createProduct(description, false);
    }

    private Product createProduct(String description, boolean discounted) {
        Product product = new Product();
        product.setDescription(description);
        product.setDiscounted(discounted);
        return product;
    }
}