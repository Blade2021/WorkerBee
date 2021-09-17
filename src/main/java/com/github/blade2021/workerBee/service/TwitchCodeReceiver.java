package com.github.blade2021.workerBee.service;

import com.github.blade2021.workerBee.Config;
import com.github.philippheuer.credentialmanager.domain.Credential;
import com.github.philippheuer.credentialmanager.domain.IdentityProvider;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.github.blade2021.workerBee.WorkerBeeApplication.*;

@RestController

public class TwitchCodeReceiver {

    @GetMapping(path="/auth/twitch", produces = "application/json")
    public void response(@RequestParam(value = "code") String code, @RequestParam(value = "scope") String scope, @RequestParam(value = "state") String identifier){

        System.out.printf("Code: %s\n",code);
        System.out.printf("Scopes: %s\n",scope);

        //provider = new TwitchIdentityProvider(Config.get("TWITCHCLIENT-ID"), Config.get("TWITCHCLIENT-SECRET"),"http://localhost");

        Optional<OAuth2IdentityProvider> provider = credentialManager.getOAuth2IdentityProviderByName("twitch");
        if(provider.isPresent()){

            OAuth2Credential credential = provider.get().getCredentialByCode(code);

            String userid = credential.getUserId();

            if(credMap.containsKey(identifier)){
                credMap.replace(identifier,userid);
            } else {
                credMap.put(identifier,userid);
            }

            credentialManager.addCredential("twitch",credential);
            credentialManager.save();

            credentialManager.getCredentials().forEach(item -> {
                System.out.println(item.getUserId());
            });

            System.out.printf("Access Token: %s\nRefresh Token: %s",credential.getAccessToken(),credential.getRefreshToken());
        }


    }

}
