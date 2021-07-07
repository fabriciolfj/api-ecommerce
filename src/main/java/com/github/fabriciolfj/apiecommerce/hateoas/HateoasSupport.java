package com.github.fabriciolfj.apiecommerce.hateoas;

import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

public interface HateoasSupport {
    default UriComponentsBuilder getUriComponentBuilder(@Nullable ServerWebExchange exchange) {
        if (exchange == null) {
            return UriComponentsBuilder.fromPath("/");
        }

        final ServerHttpRequest request = exchange.getRequest();
        final PathContainer contextPath = request.getPath().contextPath();

        return UriComponentsBuilder.fromHttpRequest(request)
                .replacePath(contextPath.toString())
                .replaceQuery("");
    }
}
