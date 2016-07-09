package com.somewhatagile.model;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name=Product.QUERY_FIND_ALL_DISCOUNTED, query = "SELECT p FROM Product p WHERE p.discounted = true ORDER BY p.description")
})
public class Product {
    public static final String QUERY_FIND_ALL_DISCOUNTED = "Product.findAllDiscounted";

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private boolean discounted;
    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
