package com.pro.bankService.service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceLogEntity {
    private Map<String, Object> prevData;
    private Map<String, Object> currentData;
    private Map<String, Object> classMethod;
    @Override
    public String toString() {
        return "BalanceLogEntity{" +
                "prevData=" + formatMap(prevData) +
                ",currentData=" + formatMap(currentData) +
                ",classMethod=" + formatMap(classMethod) +
                '}';
    }
    private String formatMap(Map<String, Object> map) {
        if (map == null || map.isEmpty()) return "{}";
        return map.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + ", " + b)
                .map(s -> "{" + s + "}")
                .orElse("{}");
    }


}
