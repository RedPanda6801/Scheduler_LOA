package com.example.loa.Dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@ToString
public class ResponseDto {

    private String message;

    private Object data;

    private HttpStatus status;

    public void setResponse(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

    public void setResponse(String message, Object data, HttpStatus status){
        this.message = message;
        this.data = data;
        this.status = status;
    }
}
