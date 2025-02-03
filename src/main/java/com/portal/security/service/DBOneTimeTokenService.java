package com.portal.security.service;

import com.portal.security.entity.OneTimeTokenEntity;
import com.portal.security.repository.OneTimeTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ott.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;

import java.util.UUID;

@Service
public class DBOneTimeTokenService implements OneTimeTokenService {
    private static final int TOKEN_VALIDITY_MINUTES = 2;

    @Autowired
    private OneTimeTokenRepository oneTimeTokenRepository;

    @NonNull
    @Override
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        OneTimeTokenEntity token = new OneTimeTokenEntity();
        token.setTokenValue(tokenValue);
        token.setUsername(request.getUsername());
        token.setCreatedAt(now);
        token.setExpiresAt(now.plusMinutes(TOKEN_VALIDITY_MINUTES));
        token.setUsed(false);

        oneTimeTokenRepository.save(token);

        return new DefaultOneTimeToken(token.getTokenValue(),token.getUsername(), Instant.now());
    }

    @Override
    public OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        OneTimeTokenEntity token = oneTimeTokenRepository.findByTokenValueAndUsedFalse(authenticationToken.getTokenValue())
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired token"));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Token has expired");
        }
        token.setUsed(true);
        oneTimeTokenRepository.save(token);

        return new DefaultOneTimeToken(token.getTokenValue(), token.getUsername(), Instant.now());
    }


}
