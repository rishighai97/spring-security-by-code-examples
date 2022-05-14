package com.chapter.seventeen.security;

import com.chapter.seventeen.repository.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

@Component
public class JpaClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientRespository clientRespository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        var clientOptional = clientRespository.findClientByClientId(clientId);
        return clientOptional
                .map(SecurityClient::new)
                .orElseThrow(() -> new ClientRegistrationException(":("));
    }

}
