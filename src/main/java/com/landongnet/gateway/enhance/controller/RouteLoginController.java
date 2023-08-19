package com.landongnet.gateway.enhance.controller;

import com.github.snake.rock.common.model.R;
import com.landongnet.gateway.enhance.auth.JwtTokenHelper;
import com.landongnet.gateway.enhance.entity.RouteUser;
import com.landongnet.gateway.enhance.service.RouteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jc
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("route")
public class RouteLoginController {

    private final JwtTokenHelper tokenHelper;
    private final PasswordEncoder passwordEncoder;
    private final RouteUserService routeUserService;

    @GetMapping("login")
    public Mono<ResponseEntity<R>> login(String username, String password) {
        String error = "认证失败，用户名或密码错误";
        ResponseEntity<R<?>> errorResp = new ResponseEntity<>(R.builder().message(error).build(), HttpStatus.INSUFFICIENT_STORAGE);
        Mono<RouteUser> routeUserMono = routeUserService.findByUsername(username);
        routeUserMono.map(routeUser -> {
            if(passwordEncoder.matches(password,routeUser.getPassword())){
                ResponseEntity<R<?>> response = new ResponseEntity<>(R.builder().data(tokenHelper.generateToken(routeUser)).build(), HttpStatus.OK);
                return Mono.just(response);
            }else{
                return Mono.just(errorResp);
            }
        });
       return Mono.empty();
    }
}
