package online.epochsolutions.cloudgateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
//                /auth
                .route(p ->p
                        .path("/auth/me")
                        .filters(f -> f.requestRateLimiter().configure(c-> c.setRateLimiter(redisRateLimiter())))
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/auth/register")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/auth/login")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/auth/verify")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/auth/hostRegister")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/auth/adminRegister")
                        .filters(f -> f.circuitBreaker((c-> c.setName("eTicketor").setFallbackUri("/defaultFallback"))))
                        .uri("http://localhost:8081"))

//                eTicketor/event


                .route(p ->p
                        .path("/eTicketor/event/save")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/eTicketor/event/events/user")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/eTicketor/event/getList")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/eTicketor/event/getEvent/{id}")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/eTicketor/event/delete/{id}")
                        .uri("http://localhost:8081"))
                .route(p ->p
                        .path("/eTicketor/event/update/{id}")
                        .uri("http://localhost:8081"))

//                eTicketor/ticket
                .route(p ->p
                        .path("/eTicketor/ticket/buyTicket")
                        .uri("http://localhost:8081"))
                .route(p -> p
                        .path("/eTicketor/ticket/viewTicket")
                        .uri("http://localhost:8081"))
                .build();
    }
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer(){
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(2))
                        .build()).build());
    }

    @Bean
    public RedisRateLimiter redisRateLimiter(){
        return new RedisRateLimiter(0,2);
    }

    @Bean
    KeyResolver userKeyResolver(){
        return exchange -> Mono.just("1");
    }
}
