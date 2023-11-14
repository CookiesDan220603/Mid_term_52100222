package com.example.springcommerce.model;

import java.util.Collection;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            columnDefinition = "VARCHAR(255)"
    )
    private String id;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private String image;
    private String brand;
    private String color;

    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "category_id")
    private Category category;

//    @ManyToMany(targetEntity = Cart.class, mappedBy = "products")
//    private Collection<Cart> carts;

    @ManyToMany(targetEntity = Order.class, mappedBy = "products")
    private Collection<Order> orders;

    @OneToMany(mappedBy = "product", targetEntity = CartProduct.class)
    private Collection<CartProduct> cartProducts;
}
