package com.github.blade2021.workerBee.service;

import com.github.blade2021.workerBee.Config;
import com.github.blade2021.workerBee.domain.UserObject;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController

public class TwitchCodeReceiver {

    @GetMapping(path="/auth/twitch", produces = "application/json")
    public void response(@RequestParam(value = "code") String code, @RequestParam(value = "scope") String scope, @RequestParam(value = "state") String identifier){

        System.out.printf("Code: %s\n",code);
        System.out.printf("Scopes: %s\n",scope);

        OAuth2IdentityProvider provider = new TwitchIdentityProvider(Config.get("TWITCHCLIENT-ID"), Config.get("TWITCHCLIENT-SECRET"),"http://localhost");
		OAuth2Credential credential = provider.getCredentialByCode(code);

        Date now = new Date();
        Timestamp timestamp = Timestamp.from(now.toInstant().plusSeconds(credential.getExpiresIn()));

        UserObject userObject = new UserObject(Long.parseLong(identifier), credential.getAccessToken(), credential.getRefreshToken(), timestamp, scope);

        System.out.printf("Access Token: %s\nRefresh Token: %s",credential.getAccessToken(),credential.getRefreshToken());

    }

}
