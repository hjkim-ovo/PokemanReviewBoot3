package com.pokemonreview.api.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private String tokenType = "Bearer ";

    private String username;

    public AuthResponseDto(String accessToken) {
        this.token = accessToken;
    }
}