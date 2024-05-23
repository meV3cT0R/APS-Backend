package com.vector.auto.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String name;
    private String username;
    private Role role;
    private String address;
    private Set<Order> orders;

    public UserData(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.address = user.getAddress();
        this.orders = user.getOrders();
    }
}
