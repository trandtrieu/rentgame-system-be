package com.controller;

import com.dto.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.model.Account;
import com.model.Order;
import com.service.AccountService;
import com.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/rent-game/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final AccountService accountService;
    private final OrderService orderService;

    @Autowired
    public OrderController(AccountService accountService, OrderService orderService) {
        this.accountService = accountService;
        this.orderService = orderService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderDTO>> getOrderList(@RequestParam("accountId") Account accountId,
                                                       @RequestParam(value = "status", required = false) String status) {
        try {
            List<Order> orders;
            if (status != null) {
                orders = orderService.findByAccountAndStatus(accountId, status);
            } else {
                orders = orderService.findByAccount(accountId);
            }

            List<OrderDTO> orderDTOs = orders.stream()
                    .map(order -> new OrderDTO(order.getOrderCode(), order.getAmount(), order.getDescription(), order.getStatus(), order.getCreatedAt()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private ResponseEntity<ObjectNode> createErrorResponse(ObjectNode response, String message) {
        response.put("error", -1);
        response.put("message", message);
        response.put("message", message);
        response.set("data", null);
        return ResponseEntity.badRequest().body(response);
    }


}
