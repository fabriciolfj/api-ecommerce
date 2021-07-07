package com.github.fabriciolfj.apiecommerce;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class ApiEcommerceApplicationTests {

	@Test
	void contextLoads() {
		Mono<Integer> mono1 = Mono.just(1);
		Mono<Integer> mono2 = Mono.just(2);

		var result = Mono.zip(mono1, mono2, (v1, v2) -> v1 + v2)
				.then(Mono.just("ok"));
		result.subscribe(System.out::println);
	}

}
