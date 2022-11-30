package com.example.springbatchtutorial.job.core.domain.order;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@ToString
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderItem;
    private int price;
    private LocalDateTime orderDate;

    @Builder
    public Order(String orderItem, int price, LocalDateTime orderDate) {
        this.orderItem = orderItem;
        this.price = price;
        this.orderDate = orderDate;
    }
}
