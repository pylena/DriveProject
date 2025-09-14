package com.example.simpledriveproject.modules.drive.service.impl.S3;


import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service

public class S3Config {
    private final WebClient webClient;

    public S3Config() {
        this.webClient = WebClient.builder().build();
    }


    private static final String region = "us-east-1";
    private static final String service = "s3";
    private static final String bucket = "simple-upload-drive";
    String endpoint = bucket + ".s3.amazonaws.com";



    public HttpHeaders  sigV4(String method, String id, byte[] content) throws Exception {
        String key = id + ".txt";
        String urlPath = "/" + key;
        String payload = "UNSIGNED-PAYLOAD";

        String amzDate = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());

        String dateStamp = amzDate.substring(0, 8);


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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("x-amz-date", amzDate);
        headers.set("x-amz-content-sha256", payload);
        headers.set("Host", endpoint );
        headers.set("Content-Type", "text/plain");


        return headers;





        }


    public void sendPutRequest(String id, byte[] content) throws Exception {

        HttpHeaders headers = sigV4("PUT", id, content);
        String urlPath = "/" + id + ".txt";
        String requestUrl = "https://" + endpoint + urlPath;

        webClient.put()
                .uri(requestUrl)
                .headers(h -> h.addAll(headers))
                .bodyValue(content)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

//    public byte[] getFileContent(String id) throws Exception {
//        String url = "https://" + endpoint + "/" + id + ".txt";
//        String payload = "UNSIGNED-PAYLOAD"; //
//
//        //HttpHeaders headers = sigV4("GET", id, payload);
//
//        return webClient.get()
//                .uri(url)
//                .headers(h -> h.addAll(headers))
//                .retrieve()
//                .bodyToMono(byte[].class)
//                .block();
//    }



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
