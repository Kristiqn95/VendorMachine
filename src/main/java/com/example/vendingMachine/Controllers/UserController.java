package com.example.vendingMachine.Controllers;

import com.example.vendingMachine.Entities.User;
import com.example.vendingMachine.Services.UserServices;
import com.example.vendingMachine.Utils.BuyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserServices userServices;


    @GetMapping("")
    public ResponseEntity findAll() {
        if (userServices.findAll().isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(userServices.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity add(@RequestBody User user) {
        if (userServices.add(user) == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody User user, @PathVariable("id") Long id) {
        if (userServices.update(user, id) == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("could not find user");
        return ResponseEntity.ok(userServices.update(user, id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        userServices.delete(id);
    }


    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") Long id) {
        if (userServices.findById(id) == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("could not find user");
        return ResponseEntity.ok(userServices.findById(id));
    }


    @PostMapping("/deposit/{id}/{amount}")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity deposit(@PathVariable("amount") int amount, @PathVariable("id") Long id) {
        if (Arrays.asList(5, 10, 20, 50, 100).contains(amount)) {
            User deposit = userServices.deposit(amount, id);
            if (deposit == null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("could not find user");
            return ResponseEntity.ok(deposit);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("you can only deposit coins of 5, 10, 20, 50, 100");
    }

    // reset deposit
    @PostMapping("/resetDeposit")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity resetDeposit() {
        return ResponseEntity.ok(userServices.resetDeposit());
    }

    // buy product
    @PostMapping("/buy/{productId}/{buyerId}/{quantity}")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity buy(@PathVariable("productId") Long productId, @PathVariable("buyerId") Long buyerId, @PathVariable("quantity") int quantity) {
        BuyResponse buyResponse = userServices.buy(productId, buyerId, quantity);
        if (buyResponse == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("you either don't have enough money or product quantity is not enough");
        return ResponseEntity.ok(buyResponse);

    }
}