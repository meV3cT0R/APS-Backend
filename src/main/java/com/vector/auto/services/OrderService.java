package com.vector.auto.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vector.auto.model.Autopart;
import com.vector.auto.model.CartItem;
import com.vector.auto.model.CartItemBody;
import com.vector.auto.model.CartsBody;
import com.vector.auto.model.Order;
import com.vector.auto.model.Status;
import com.vector.auto.model.User;
import com.vector.auto.repository.CartItemRepo;
import com.vector.auto.repository.OrderRepo;
import com.vector.auto.repository.PartsRepo;
import com.vector.auto.repository.UserRepo;

@Service
public class OrderService {
    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private PartsRepo partsRepo;


    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public Order saveOrder(CartsBody cart,String token) throws Exception {
        List<CartItemBody> cartItems = cart.getCartItems();
        String username = jwtService.extractUsername(token.split(" ")[1]);
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isEmpty()) throw new Exception("No user with username : "+username);

        Set<CartItem> items = new HashSet<>();
        Double totalCost = 0.0;
        Order order = new Order();
        order.setUsers(user.get());
        for(CartItemBody cartItem : cartItems) {
            CartItem item = new CartItem();
            
            Optional<Autopart> part = partsRepo.findById(cartItem.getProductId());
            if(part.isEmpty()) 
                throw new Exception("No Product with id : "+cartItem.getProductId());

            item.setQuantity(cartItem.getQuantity());
            item.setQuantity(cartItem.getQuantity());
            item.setOrders(order);

            cartItemRepo.save(item);
            items.add(item);
            totalCost += part.get().getPrice();
            
            cartItemRepo.save(item);
        };

        order.setCartItems(items);
        order.setStatus(Status.PENDING);
        order.setTotalCost(totalCost);
        order.setCartItems(items);

        orderRepo.save(order);

        return order;
    }
}
