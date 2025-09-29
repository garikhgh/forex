package com.forex.forex.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class CustomerEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String email;
    private BigDecimal balance;

    //this could be discussed if needed to keep the relationship
//    @OneToMany
//    private List<OrderEntity> customerOrder;

    // could be added
    // createdBy
    // createdDate
    // modifiedBy
    // modifiedDAte
}
