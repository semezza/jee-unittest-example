package com.somewhatagile.service;

import com.somewhatagile.dao.ProductDAO;
import com.somewhatagile.model.Product;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    private ProductService service = new ProductService();
    private ProductDAO mockedDao = mock(ProductDAO.class);

    @Before
    public void setUp() {
        service.dao = mockedDao;
    }

    @Test
    public void testCreateWithNullReturnsNull() {
        assertNull(service.createOrUpdateProduct(null));
    }

    @Test
    public void testCreateNewProduct() {
        Product productToCreate = new Product();
        when(mockedDao.create(any(EntityManager.class), same(productToCreate))).thenReturn(productToCreate);
        Product actualProduct = service.createOrUpdateProduct(productToCreate);
        verify(mockedDao).create(any(EntityManager.class), same(productToCreate));
        assertSame(productToCreate, actualProduct);
    }

    @Test
    public void testUpdateProduct() {
        Product productToCreate = new Product();
        productToCreate.setId(1L);
        when(mockedDao.update(any(EntityManager.class), same(productToCreate))).thenReturn(productToCreate);
        Product actualProduct = service.createOrUpdateProduct(productToCreate);
        verify(mockedDao).update(any(EntityManager.class), same(productToCreate));
        assertSame(productToCreate, actualProduct);
    }

    @Test
    public void testGetPriceForNonExistingProductReturnsNull() {
        assertNull(service.getPriceForProduct(43L));
    }

    @Test
    public void testGetPriceForNonDiscountedProductReturnsThatPrice() {
        long existingId = 2L;
        double expectedPrice = 9.99D;
        Product product = new Product();
        product.setPrice(expectedPrice);
        when(mockedDao.get(any(EntityManager.class), eq(existingId))).thenReturn(product);
        assertEquals(expectedPrice, service.getPriceForProduct(existingId), 0d);
    }

    @Test
    public void testGetPriceForDiscountedProductReturnsDiscountedPrice() {
        long existingId = 2L;
        double price = 10D;
        double expectedPrice = 8D;
        Product product = new Product();
        product.setPrice(price);
        product.setDiscounted(true);
        when(mockedDao.get(any(EntityManager.class), eq(existingId))).thenReturn(product);
        assertEquals(expectedPrice, service.getPriceForProduct(existingId), 0d);
    }

    @Test
    public void testGetPriceForDiscountedProductReturnsRoundedDiscountedPrice() {
        long existingId = 2L;
        double price = 1.26D;
        double expectedPrice = 1.01D;
        Product product = new Product();
        product.setPrice(price);
        product.setDiscounted(true);
        when(mockedDao.get(any(EntityManager.class), eq(existingId))).thenReturn(product);
        assertEquals(expectedPrice, service.getPriceForProduct(existingId), 0d);
    }
}