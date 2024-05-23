package com.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import com.lib.payos.type.PaymentData;
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

import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/rent-game/payment")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PayOS payOS;
    private final AccountService accountService;
    private final OrderService orderService;

    private static final String DESCRIPTION = "Ma giao dich thu nghiem";
    private static final String RETURN_URL = "http://localhost:3000/success";
    private static final String CANCEL_URL = "http://localhost:3000/cancel";

    @Autowired
    public PaymentController(PayOS payOS, AccountService accountService, OrderService orderService) {
        this.payOS = payOS;
        this.accountService = accountService;
        this.orderService = orderService;
    }

    @PostMapping("/payos_transfer_handler")
    public ResponseEntity<ObjectNode> payosTransferHandler(@RequestBody ObjectNode body) {
        logger.info("Webhook received: {}", body.toString()); // Log incoming request
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            JsonNode data = payOS.verifyPaymentWebhookData(body);
            logger.info("Webhook data verified: {}", data.toString()); // Log verified data

            String description = getNodeText(data, "description");
            if (!DESCRIPTION.equals(description)) {
                return createErrorResponse(response, "Invalid transaction description");
            }

            long accountId = getNodeLong(data, "accountId");
            double amount = getNodeDouble(data, "amount");
            int orderCode = getNodeInt(data, "orderCode");
            String status = getNodeText(data, "status");

            Optional<Account> optionalAccount = accountService.findAccountById(accountId);
            if (optionalAccount.isEmpty()) {
                return createErrorResponse(response, "Account not found");
            }

            Account account = optionalAccount.get();
            account.setBalance(account.getBalance() + amount);
            accountService.saveOrUpdate(account);

            Order order = new Order();
            order.setAccount(account);
            order.setAmount(amount);
            order.setOrderCode(orderCode);
            order.setDescription(description);
            order.setStatus(status);
            order.setCreatedAt(new Date());
            orderService.saveOrder(order);

            response.put("error", 0);
            response.put("message", "Ok");
            response.set("data", null);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Exception occurred while processing webhook: ", e);
            return createErrorResponse(response, "Internal server error: " + e.getMessage());
        }
    }


    @PostMapping(path = "/add-funds")
    public ResponseEntity<ObjectNode> addFunds(@RequestParam long accountId, @RequestParam double amount) {
        try {
            Optional<Account> optionalAccount = accountService.findAccountById(accountId);
            if (optionalAccount.isEmpty()) {
                return createErrorResponse("User not found");
            }
            if (amount <= 0) {
                return createErrorResponse("Amount must be positive");
            }

            // Tạo mã đơn hàng
            String currentTimeString = String.valueOf(new Date().getTime());
            int orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

            // Tạo đối tượng đơn hàng
            Order order = new Order();
            order.setOrderCode(orderCode);
            order.setAmount(amount);
            order.setDescription(DESCRIPTION);
            order.setStatus("Pending");
            order.setAccount(optionalAccount.get());
            order.setCreatedAt(new Date());

            orderService.saveOrder(order);

            PaymentData paymentData = new PaymentData(orderCode, (int) amount, DESCRIPTION, null, CANCEL_URL, RETURN_URL);
            JsonNode data = payOS.createPaymentLink(paymentData);
            String checkoutUrl = getNodeText(data, "checkoutUrl");

            ObjectNode response = new ObjectMapper().createObjectNode();
            response.put("checkoutUrl", checkoutUrl);
            response.put("orderCode", orderCode);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Exception in add-funds", e);
            return createErrorResponse("Failed to create payment link: " + e.getMessage());
        }
    }


    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(@RequestParam("orderCode") int orderCode) {

        try {
            Optional<Order> optionalOrder = orderService.findByOrderCode(orderCode);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();

                // Cập nhật trạng thái của đơn hàng thành "CANCELLED"
                order.setStatus("CANCELLED");
                orderService.saveOrder(order);

                return ResponseEntity.ok("Order has been cancelled.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }


    @GetMapping("/success")
    public ResponseEntity<String> success(@RequestParam("status") String status,
                                          @RequestParam("accountId") long accountId,
                                          @RequestParam("orderCode") int orderCode) {

        try {
            if ("PAID".equals(status)) {
                Optional<Order> optionalOrder = orderService.findByOrderCode(orderCode);
                if (optionalOrder.isPresent()) {
                    Order order = optionalOrder.get();

                    // Check if the order status is already "PAID"
                    if ("PAID".equals(order.getStatus())) {
                        return ResponseEntity.ok("Order is already paid. No update needed.");
                    }

                    double amount = order.getAmount();

                    Optional<Account> optionalAccount = accountService.findAccountById(accountId);
                    if (optionalAccount.isPresent()) {
                        Account account = optionalAccount.get();
                        account.setBalance(account.getBalance() + amount);
                        accountService.saveOrUpdate(account);
                        order.setStatus("PAID");
                        orderService.saveOrder(order);

                        return ResponseEntity.ok("Payment was successful! Balance updated.");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
                }
            } else {
                return ResponseEntity.ok("Payment was not successful.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }


    private String getNodeText(JsonNode node, String fieldName) throws Exception {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull()) {
            throw new Exception("Missing or null field: " + fieldName);
        }
        return field.asText();
    }

    private long getNodeLong(JsonNode node, String fieldName) throws Exception {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull()) {
            throw new Exception("Missing or null field: " + fieldName);
        }
        return field.asLong();
    }

    private double getNodeDouble(JsonNode node, String fieldName) throws Exception {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull()) {
            throw new Exception("Missing or null field: " + fieldName);
        }
        return field.asDouble();
    }

    private int getNodeInt(JsonNode node, String fieldName) throws Exception {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull()) {
            throw new Exception("Missing or null field: " + fieldName);
        }
        return field.asInt();
    }

    private ResponseEntity<ObjectNode> createErrorResponse(ObjectNode response, String message) {
        response.put("error", -1);
        response.put("message", message);
        response.set("data", null);
        return ResponseEntity.badRequest().body(response);
    }

    private ResponseEntity<ObjectNode> createErrorResponse(String message) {
        ObjectNode response = new ObjectMapper().createObjectNode();
        response.put("error", -1);
        response.put("message", message);
        response.set("data", null);
        return ResponseEntity.badRequest().body(response);
    }
}
