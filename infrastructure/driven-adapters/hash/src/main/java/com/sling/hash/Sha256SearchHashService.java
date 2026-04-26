package com.sling.hash;

import com.sling.model.search.Search;
import com.sling.model.search.service.SearchHashServicePort;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@Component
public class Sha256SearchHashService implements SearchHashServicePort {

    private static final String ALGORITHM = "SHA-256";
    private static final String SEPARATOR = "|";

    @Override
    public String generateHash(Search search) {
        String canonical = buildCanonicalString(search);
        return sha256Hex(canonical);
    }

    private String buildCanonicalString(Search search) {
        String ages = search.getAges()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return new StringBuilder()
                .append(search.getHotelId())
                .append(SEPARATOR)
                .append(search.getCheckIn())
                .append(SEPARATOR)
                .append(search.getCheckOut())
                .append(SEPARATOR)
                .append(ages)
                .toString();
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hexByte = Integer.toHexString(0xff & b);
            if (hexByte.length() == 1) {
                hex.append('0');
            }
            hex.append(hexByte);
        }
        return hex.toString();
    }

}
