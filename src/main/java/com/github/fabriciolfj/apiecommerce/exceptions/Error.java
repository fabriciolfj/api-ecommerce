package com.github.fabriciolfj.apiecommerce.exceptions;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Error implements Serializable {

    private static final long serialVersionUID = -6982936800555728976L;

    private String errorCode;
    private String message;
    private Integer status;
    private String url;
    private String reqMethod;
}
