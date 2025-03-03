package com.rabbitmq.bankService.consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bank_balance_log")
public class BankBalanceLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String prev_data;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String current_data;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String class_method;
    public BankBalanceLogEntity(String prev_data, String current_data, String class_method) {
        this.prev_data = prev_data;
        this.current_data = current_data;
        this.class_method = class_method;
    }

}
