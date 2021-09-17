package com.github.blade2021.workerBee;

import com.github.blade2021.workerBee.events.EventHandler;
import com.github.blade2021.workerBee.events.GoLive;
import com.github.blade2021.workerBee.objects.Dispatcher;
import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import net.dv8tion.jda.internal.JDAImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class WorkerBeeApplication {

	public static String prefix = Config.get("prefix");
	public static JDAImpl jda = null;
	public static Dispatcher dispatcher;
	public static TwitchClient twitchClient = null;
	public static EventManager eventManager = new EventManager();

	public static List<OAuth2Credential> credentialList = new ArrayList<>();
	public static CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
	public static Map<String, String> credMap = new HashMap<>();

	public static void main(String[] args) throws LoginException {
		SpringApplication.run(WorkerBeeApplication.class, args);
/*
		JDA api = JDABuilder.createDefault(Config.get("token"))
				.enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_PRESENCES)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.enableCache(CacheFlag.ACTIVITY)
				.setChunkingFilter(ChunkingFilter.ALL)
				.build();


		try {
			// Wait for discord jda to completely load
			api.awaitReady();
			jda = (JDAImpl) api;

			api.addEventListener(dispatcher = new Dispatcher());

			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}


 */

		credentialManager.registerIdentityProvider(new TwitchIdentityProvider(Config.get("TWITCHCLIENT-ID"), Config.get("TWITCHCLIENT-SECRET"),"http://localhost"));


		OAuth2Credential defaultToken = null;
		if(credentialManager.getOAuth2CredentialByUserId(credMap.get("313832264792539142")).isPresent()){
			defaultToken = credentialManager.getOAuth2CredentialByUserId(credMap.get("313832264792539142")).get();
		}

		eventManager.autoDiscovery();
		eventManager.setDefaultEventHandler(EventHandler.class);

		twitchClient = TwitchClientBuilder.builder()
				.withDefaultAuthToken(defaultToken)
				.withEnableHelix(true)
				.withEventManager(eventManager)
				.withDefaultEventHandler(EventHandler.class)
				.build();

		twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new GoLive());
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}

}
