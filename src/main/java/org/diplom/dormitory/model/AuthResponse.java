package org.diplom.dormitory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    @JsonProperty("role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}