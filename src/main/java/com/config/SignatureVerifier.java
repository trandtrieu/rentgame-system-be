package com.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SignatureVerifier {
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public boolean verifySignature(String signature, long accountId, double amount) {
        // Đảm bảo rằng bạn có khóa bí mật từ PayOS
        String secretKey = "YOUR_SECRET_KEY_FROM_PAYOS";

        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);
            String data = accountId + "|" + amount; // Dữ liệu để tạo chữ ký
            byte[] hmacBytes = mac.doFinal(data.getBytes());
            String calculatedSignature = Base64.getEncoder().encodeToString(hmacBytes);

            // So sánh chữ ký
            return signature.equals(calculatedSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }
}
