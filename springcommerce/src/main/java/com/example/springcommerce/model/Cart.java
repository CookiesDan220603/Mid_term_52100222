package com.example.springcommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Collection;
import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
@Data
public class Cart {
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

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user_id;

    private Date created_date;

    private Date updated_date;


    @OneToMany(mappedBy = "cart", targetEntity = CartProduct.class)
    private Collection<CartProduct> cartProducts;

}
