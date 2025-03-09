package com.pro.model.param;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceLogParam {
    private Map<String, Object> prevData;
    private Map<String, Object> currentData;
    private Map<String, Object> classMethod;


}
