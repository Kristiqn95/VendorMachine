package com.example.vendingMachine.Services;

import com.example.vendingMachine.Entities.Product;
import com.example.vendingMachine.Entities.User;
import com.example.vendingMachine.Repositories.UserRepository;
import com.example.vendingMachine.Utils.BuyResponse;
import com.example.vendingMachine.Utils.ChangePasswordVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductServices productServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User add(User user){
        if (userRepository.findByUsername(user.getUsername())==null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }

    public User update(User user, Long ID) {
    if (userRepository.findById(ID).isPresent()){
        User user1 = userRepository.findById(ID).get();
        user1.setDeposit(user.getDeposit());
        user1.setRole(user.getRole());
        user1.setUsername(user.getUsername());

        return userRepository.save(user1);
        }
        return null;
    }

    public void delete(Long ID) {
        userRepository.deleteById(ID);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long user) {
        return userRepository.findById(user).get();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User deposit (int amount, Long ID) {
        User user = userRepository.findById(ID).get();
        if (user != null) {
            user.setDeposit(user.getDeposit() + amount);
            return userRepository.save(user);
        }
        return null;
    }

    public User resetDeposit() {
        final User currentUser = getCurrentUser();
        currentUser.setDeposit(0);
        return userRepository.save(currentUser);
    }

    public BuyResponse buy (Long productID, Long buyerID, int quantity){
        User buyer = userRepository.findById(buyerID).get();
        Product product = productServices.findById(productID);
        int totalAmount = quantity * product.getCost();
        if (buyer.getDeposit() >= totalAmount && product.getAmountAvailable() >= quantity) {
            buyer.setDeposit(buyer.getDeposit() - totalAmount);
            buyer.setTotalSpent(buyer.getTotalSpent() + totalAmount);
            productServices.decreaseAmountAvailable(product,quantity);
            if (!buyer.getBoughtProducts().contains(product))
                buyer.getBoughtProducts().add(product);
            userRepository.save(buyer);
            return new BuyResponse(buyer.getTotalSpent(),buyer.getDeposit(),buyer.getBoughtProducts());
        }
        return null;
    }

    public Boolean changePassword (ChangePasswordVM changePasswordVM) {
        final User currentUser = getCurrentUser();
        if (passwordEncoder.matches(changePasswordVM.getOldPassword(),currentUser.getPassword())){
            currentUser.setPassword(passwordEncoder.encode(changePasswordVM.getNewPassword()));
            userRepository.save(currentUser);
            return true;
        } else return false;
    }

    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User currentUser = findByUsername(authentication.getName());
        return currentUser;
    }
}
