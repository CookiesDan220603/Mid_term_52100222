package com.example.springcommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Collection;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
@Data
public class Category {
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

    private Date created_date;

    private Date updated_date;

    @OneToMany(
            mappedBy = "category",
            targetEntity = Product.class
    )
    private Collection<Product> products;
}
