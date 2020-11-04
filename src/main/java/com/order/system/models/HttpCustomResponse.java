package com.order.system.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;


@Data
public class HttpCustomResponse {

    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Europe/Dublin")
    private Date timeStamp;

    public HttpCustomResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
        this.timeStamp = new Date();
    }
}
