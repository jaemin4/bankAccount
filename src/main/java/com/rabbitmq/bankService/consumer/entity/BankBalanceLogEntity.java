package com.rabbitmq.bankService.consumer.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private String prevData;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String currentData;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String classMethod;
    public BankBalanceLogEntity(String prevData, String currentData, String classMethod) {
        this.prevData = prevData;
        this.currentData = currentData;
        this.classMethod = classMethod;
    }

}
