package com.k_passs.backend.global.oauth2;

import com.k_passs.backend.domain.user.KakaoProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class KakaoIdTokenValidator {

    private static final Logger log = LoggerFactory.getLogger(KakaoIdTokenValidator.class);

    private final Set<String> allowedIds;

    public KakaoIdTokenValidator(KakaoProperties kakaoProperties) {
        // yml에 있는 allowed-ids → List<String> → Set<String> 으로 변환
        this.allowedIds = new HashSet<>(kakaoProperties.getClient().getAllowedIds());
    }

    public boolean validate(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWSHeader header = signedJWT.getHeader();

            // 카카오 공개키
            JWKSet jwkSet = JWKSet.load(new URL("https://kauth.kakao.com/.well-known/jwks.json"));
            RSAKey rsaKey = (RSAKey) jwkSet.getKeyByKeyId(header.getKeyID());

            // 서명 검증
            RSASSAVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
            if (!signedJWT.verify(verifier)) {
                log.warn("idToken 서명 검증 실패");
                return false;
            }

            // 클레임 검증
            var claims = signedJWT.getJWTClaimsSet();
            String issuer = claims.getIssuer();
            Object audClaim = claims.getClaim("aud");
            Date expiration = claims.getExpirationTime();

            if (!"https://kauth.kakao.com".equals(issuer)) {
                log.warn("idToken issuer 불일치: {}", issuer);
                return false;
            }

            // aud 검증 (String or List)
            boolean audOk = false;
            if (audClaim instanceof String s) {
                audOk = allowedIds.contains(s.trim());
            } else if (audClaim instanceof java.util.List<?> list) {
                for (Object o : list) {
                    if (o instanceof String s && allowedIds.contains(s.trim())) {
                        audOk = true; break;
                    }
                }
            }
            if (!audOk) {
                log.warn("idToken audience 불일치. allowed={}, actual={}", allowedIds, audClaim);
                return false;
            }

            if (expiration == null || expiration.before(new Date())) {
                log.warn("idToken 만료됨: {}", expiration);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("idToken 검증 실패: {}", e.getMessage(), e);
            return false;
        }
    }
}