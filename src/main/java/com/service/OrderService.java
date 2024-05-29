package com.service;

import com.model.Account;
import com.model.Order;
import com.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findOrderById(long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByOrderCode(int orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }



    public List<Order> findByAccount(Account accountId) {
        return orderRepository.findByAccount(accountId);
    }

    public List<Order> findByAccountAndStatus(Account accountId, String status) {
        return orderRepository.findByAccountAndStatus(accountId, status);
    }

}
