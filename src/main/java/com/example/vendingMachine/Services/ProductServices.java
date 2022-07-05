package com.example.vendingMachine.Services;

import com.example.vendingMachine.Entities.Product;
import com.example.vendingMachine.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServices {

    @Autowired
    private ProductRepository productRepository;
    public Product add(Product product){
        return productRepository.save(product);
    }

    public Product update(Product product, Long ID) {
        if (productRepository.findById(ID).isPresent()) {
            Product product1 = productRepository.findById(ID).get();
            product1.setProductName(product.getProductName());
            product1.setCost(product.getCost());
            product1.setAmountAvailable(product.getAmountAvailable());
            return productRepository.save(product1);
        }
        return null;
    }

    public void delete(Long ID) {
        productRepository.deleteById(ID);
    }

    public List<Product> findAll() { return productRepository.findAll(); }

    public Product findById(Long ID) {
        return productRepository.findById(ID).get();
    }

    public Product decreaseAmountAvailable(Product product, int quantity) {
        if (product.getAmountAvailable() >= quantity) {
            product.setAmountAvailable(product.getAmountAvailable() - quantity);
            return productRepository.save(product);
        }
        return null;
    }
}
