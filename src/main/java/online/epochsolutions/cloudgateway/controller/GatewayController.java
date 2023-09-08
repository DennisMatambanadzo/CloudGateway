package online.epochsolutions.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @GetMapping("/defaultFallback")
    public String defaultMessage(){
        return "Connection Error";
    }
}
