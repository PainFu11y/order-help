package org.platform.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.platform.NoAuthorizationRequired;
import org.platform.model.LoginRequest;
import org.platform.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Hidden
public class AuthenticationController {
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @NoAuthorizationRequired
    public @ResponseBody String login (@RequestBody LoginRequest loginRequest){

        return tokenService.getToken(loginRequest);
    }
}
