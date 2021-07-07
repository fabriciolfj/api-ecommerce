package com.github.fabriciolfj.apiecommerce.exceptions;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.github.fabriciolfj.apiecommerce.exceptions.ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION;

@Component
@Order(-2)
public class ApiErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ApiErrorWebExceptionHandler(ApiErrorAttributes errorAttributes,
                                       ApplicationContext applicationContext,
                                       ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties().getResources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
        var throwable = converter(request);
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request,
                ErrorAttributeOptions.defaults());

        if (throwable instanceof IllegalArgumentException
                || throwable instanceof DataIntegrityViolationException
                || throwable instanceof ServerWebInputException) {
            return infraException(throwable, errorPropertiesMap);
        }

        if (throwable instanceof BusinessException) {
            return businessException(throwable, errorPropertiesMap);
        }

        return exceptionNotMappead(throwable, errorPropertiesMap);
    }

    private Mono<ServerResponse> exceptionNotMappead(Throwable throwable, Map<String, Object> errorPropertiesMap) {
        errorPropertiesMap.put("status",HttpStatus.SERVICE_UNAVAILABLE);
        errorPropertiesMap.put("code", HttpStatus.SERVICE_UNAVAILABLE);
        errorPropertiesMap.put("error", "");
        errorPropertiesMap.put("message", String
                .format("%s %s", "Fail server",
                        throwable.getMessage()));
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }

    private Mono<ServerResponse> businessException(Throwable throwable, Map<String, Object> errorPropertiesMap) {
        var exception = (BusinessException) throwable;
        errorPropertiesMap.put("status",HttpStatus.BAD_REQUEST);
        errorPropertiesMap.put("code", exception.getErrorCode());
        errorPropertiesMap.put("error", exception.getStackTrace());
        errorPropertiesMap.put("message", String
                .format("%s %s", exception.getErrMsgKey(),
                        throwable.getMessage()));
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }

    private Mono<ServerResponse> infraException(Throwable throwable, Map<String, Object> errorPropertiesMap) {
        errorPropertiesMap.put("status", HttpStatus.BAD_REQUEST);
        errorPropertiesMap.put("code", ILLEGAL_ARGUMENT_EXCEPTION.getErrCode());
        errorPropertiesMap.put("error", ILLEGAL_ARGUMENT_EXCEPTION);
        errorPropertiesMap.put("message", String
                .format("%s %s", ILLEGAL_ARGUMENT_EXCEPTION.getErrMsgKey(),
                        throwable.getMessage()));
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }

    private Throwable converter(ServerRequest request) {
        return (Throwable) request
                .attribute("org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR")
                .orElseThrow(
                        () -> new IllegalStateException("Missing exception attribute in ServerWebExchange"));
    }
}
