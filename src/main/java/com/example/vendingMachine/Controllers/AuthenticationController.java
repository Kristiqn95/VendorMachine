package com.example.vendingMachine.Controllers;

import com.example.vendingMachine.Entities.User;
import com.example.vendingMachine.Security.TokenProvider;
import com.example.vendingMachine.Services.UserServiceDetails;
import com.example.vendingMachine.Services.UserServices;
import com.example.vendingMachine.Utils.ChangePasswordVM;
import com.example.vendingMachine.Utils.JwtResponse;
import com.example.vendingMachine.Utils.LoginModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Import(SecurityConfig.class)
@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping("/auth/")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserServiceDetails userDetailsService;

    @Autowired
    private UserServices userService;


    @PostMapping("login")
    public ResponseEntity<?> authenticate(@RequestBody LoginModel loginModel) {
        User byUsername = userService.findByUsername(loginModel.getUsername());
        if (byUsername.isActive())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already logged in");

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginModel.getUsername(),
                        loginModel.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginModel.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        final String token;
        token = tokenProvider.generateToken(userDetails, 1);
        byUsername.setActive(true);
        userService.update(byUsername, byUsername.getId());
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @PostMapping("changePassword")
    public Boolean changePassword(@RequestBody ChangePasswordVM changePasswordVM) {
        return userService.changePassword(changePasswordVM);
    }

    @PostMapping("logout")
    public User logout() {
        userService.getCurrentUser().setActive(false);
        return userService.update(userService.getCurrentUser(), userService.getCurrentUser().getId());
    }
}