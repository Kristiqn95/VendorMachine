package com.example.vendingMachine.Utils;

import com.example.vendingMachine.Entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuyResponse implements Serializable {
    private int totalSpent;
    private int depositLeft;
    private List<Integer> coinsLeft;
    @JsonIgnoreProperties({"seller", "buyer", "amountAvailable"})
    private List<Product> boughtProducts;

    public BuyResponse(int totalSpent, int depositLeft, List<Product> boughtProducts) {
        this.totalSpent = totalSpent;
        this.depositLeft = depositLeft;
        this.boughtProducts = boughtProducts;
        this.coinsLeft = new ArrayList<>();

        // Convert depositLeft into coins and put in coinsLeft
        while (depositLeft > 0) {
            if (depositLeft >= 100) {
                coinsLeft.add(100);
                depositLeft -= 100;
            } else if (depositLeft >= 50) {
                coinsLeft.add(50);
                depositLeft -= 50;
            } else if (depositLeft >= 20) {
                coinsLeft.add(20);
                depositLeft -= 20;
            } else if (depositLeft >= 10) {
                coinsLeft.add(10);
                depositLeft -= 10;
            } else if (depositLeft >= 5) {
                coinsLeft.add(5);
                depositLeft -= 5;
            }


        }
    }
}
