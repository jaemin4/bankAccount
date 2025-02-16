package com.account.pro.bankService.service.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown=true) // ignore unknown properties when deserialize
@JsonInclude(JsonInclude.Include.NON_NULL) // not null only
public class RestResult {

    @JsonIgnore
    private boolean success = false;

    private Map<String, Object> data;

    private List<?> list;

    private String url;     // ckeditor 5 용으로 url 을 리턴하고 에디터 <img src="여기에 표기하기 위해서"> 에 표기

    private String message;

    private String status;

    public RestResult() { }

    public RestResult(String url) {
        this.url = url;
    }

    public RestResult(Map<String, Object> data) {
        this.data = data;
    }

    public RestResult setData(Map<String, Object> data) {

        this.data = data;
        return this;
    }




    public RestResult(String message, String status, Map<String,Object> data){
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public RestResult(String message, String status){
        this.message = message;
        this.status = status;
    }




}