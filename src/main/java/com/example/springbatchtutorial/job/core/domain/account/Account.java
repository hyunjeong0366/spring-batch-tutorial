package com.example.springbatchtutorial.job.core.domain.account;

import com.example.springbatchtutorial.job.core.domain.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@ToString
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderItem;
    private int price;
    private LocalDateTime accountDate;

    public Account(Order order){
        this.id = order.getId();
        this.orderItem = order.getOrderItem();
        this.price = order.getPrice();
        this.accountDate = order.getOrderDate();
    }
}
