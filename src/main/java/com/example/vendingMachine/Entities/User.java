package com.example.vendingMachine.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
public class User implements Serializable {
//    @Id
//    @Column(name = "ID",unique = true)
//    @GeneratedValue(strategy=GenerationType.AUTO, generator = "id_Sequence")
//    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private int deposit;

    private int totalSpent;

    private boolean isActive = false;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(int totalSpent) {
        this.totalSpent = totalSpent;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", deposit=" + deposit +
                ", role=" + role +
                ", totalSpent=" + totalSpent +
                '}';
    }


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sellerld")
    @JsonIgnoreProperties({"seller"})
    private List<Product> soldProducts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "buy_transactions", joinColumns = {
            @JoinColumn(name = "buyer_id")}, inverseJoinColumns = {
            @JoinColumn(name = "bought_product_id")})
    @JsonIgnoreProperties({"buyers"})
    private List<Product> boughtProducts;

    public List<Product> getBoughtProducts() {
        return boughtProducts;
    }

}
