package com.repository;

import com.model.Account;
import com.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(int orderCode);

    List<Order> findByAccount(Account accountId);
    List<Order> findByAccountAndStatus(Account accountId, String status);

}
