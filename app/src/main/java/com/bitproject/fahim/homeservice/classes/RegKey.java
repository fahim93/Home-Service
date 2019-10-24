package com.bitproject.fahim.homeservice.classes;

public class RegKey {
    private String sk_id, secret_key, email;

    public RegKey() {
    }

    public RegKey(String sk_id, String secret_key, String email) {
        this.sk_id = sk_id;
        this.secret_key = secret_key;
        this.email = email;
    }

    public String getSk_id() {
        return sk_id;
    }

    public void setSk_id(String sk_id) {
        this.sk_id = sk_id;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
