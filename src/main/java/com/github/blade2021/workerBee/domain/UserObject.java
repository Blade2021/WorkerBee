package com.github.blade2021.workerBee.domain;

import com.github.philippheuer.credentialmanager.domain.Credential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import static javax.persistence.GenerationType.AUTO;

@Entity(name = "codeBase")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserObject {

    @Id
    @GeneratedValue(
            strategy = AUTO
    )
    private Long id;
    private Long dUID;
    private String twitchAccessToken;
    private String twitchRefreshToken;
    private Timestamp expireDate;
    private String scopes;

    public UserObject(Long dUID, String twitchAccessToken, String twitchRefreshToken, Timestamp expireDate, String scopes){
        this.dUID = dUID;
        this.twitchAccessToken = twitchAccessToken;
        this.twitchRefreshToken = twitchRefreshToken;
        this.expireDate = expireDate;
        this.scopes = scopes;
    }

}
