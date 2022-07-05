package com.example.vendingMachine.Controllers;

import com.example.vendingMachine.Entities.Product;
import com.example.vendingMachine.Services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductServices productServices;

    @GetMapping("")
    public ResponseEntity findAll() {
        if (productServices.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(productServices.findAll());
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity add(@RequestBody Product product) {
        return ResponseEntity.ok(productServices.add(product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity update(@RequestBody Product product, @PathVariable("id") Long id) {
        Product update = productServices.update(product, id);
        if (update == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("could not find product");
        }
        return ResponseEntity.ok(update);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void delete(@PathVariable("id") long id) {
        productServices.delete(id);
    }


    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") Long id) {
        if (productServices.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("could not find product");
        }
        return ResponseEntity.ok(productServices.findById(id));
    }
}