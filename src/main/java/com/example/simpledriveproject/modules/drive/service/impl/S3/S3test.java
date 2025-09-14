package com.example.simpledriveproject.modules.drive.service.impl.S3;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class S3test {

    private static final String region = "us-east-1";
    private static final String service = "s3";
    private static final String bucket = "simple-upload-drive";
    String endpoint = bucket + ".s3.amazonaws.com";


    public byte[] send(String method, String id, byte[] content) throws Exception {
        String key = id.endsWith(".txt") ? id : id + ".txt";
        String urlPath = "/" + key;

        String payload = "UNSIGNED-PAYLOAD";

        String amzDate = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String dateStamp = amzDate.substring(0, 8);

        if (method.equals("PUT") && content == null) {
            throw new IllegalArgumentException("PUT request must have content");
        }

        // Canonical request
        String canonicalHeaders = "host:" + endpoint + "\n" +
                "x-amz-content-sha256:" + payload + "\n" +
                "x-amz-date:" + amzDate + "\n";
        String signedHeaders = "host;x-amz-content-sha256;x-amz-date";
        String canonicalRequest = method + "\n" +
                urlPath + "\n" +
                "\n" + // empty query string
                canonicalHeaders + "\n" +
                signedHeaders + "\n" +
                payload;

        // String to sign
        String credentialScope = dateStamp + "/" + region + "/" + service + "/aws4_request";
        String stringToSign = "AWS4-HMAC-SHA256" + "\n" +
                amzDate + "\n" +
                credentialScope + "\n" +
                sha256Hex(canonicalRequest);

        // Signature
        byte[] signingKey = getSignatureKey(secretKey, dateStamp, region, service);
        String signature = hmacHex(signingKey, stringToSign);

        String authHeader = "AWS4-HMAC-SHA256 " +
                "Credential=" + accessKey + "/" + credentialScope + ", " +
                "SignedHeaders=" + signedHeaders + ", " +
                "Signature=" + signature;

        // Headers
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("x-amz-date", amzDate);
        headers.set("x-amz-content-sha256", payload);
        headers.set("Host", endpoint);

        String requestUrl = "https://" + endpoint + urlPath;

        if (method.equalsIgnoreCase("PUT")) {
            HttpEntity<byte[]> entity = new HttpEntity<>(content, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );
             return response.getBody().getBytes();
        } else if (method.equalsIgnoreCase("GET")) {
            HttpEntity<String> entity = new HttpEntity<>("", headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );
            return response.getBody();
        } else {
            throw new UnsupportedOperationException("Unsupported method: " + method);
        }
    }
    private static String sha256Hex(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String hmacHex(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return bytesToHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
        byte[] kDate = hmac(("AWS4" + key).getBytes(), dateStamp);
        byte[] kRegion = hmac(kDate, regionName);
        byte[] kService = hmac(kRegion, serviceName);
        return hmac(kService, "aws4_request");
    }

    private static byte[] hmac(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
