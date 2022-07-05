package com.example.vendingMachine.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Product implements Serializable {
    @Id
    @Column(name = "ID",unique = true)
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private Long id;

    private String productName;
    private int amountAvailable;
    private int cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellerld")
    @JsonIgnoreProperties({"products", "soldProducts"})
    private User seller;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "buy_transactions", joinColumns = {
            @JoinColumn(name = "buyer_id",unique = true)}, inverseJoinColumns = {
            @JoinColumn(name = "bought_product_id")})
    @JsonIgnoreProperties({"boughtProducts", "soldProducts"})
    private List<User> buyers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", amountAvailable=" + amountAvailable +
                ", cost=" + cost +
                '}';
    }


}
