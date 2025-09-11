package com.k_passs.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest {

    @NotBlank
    @Schema(description = "카카오에서 받은 ID 토큰", example = "asdADFads12...")
    @JsonAlias({"id_token", "idToken"})
    private String idToken;
}