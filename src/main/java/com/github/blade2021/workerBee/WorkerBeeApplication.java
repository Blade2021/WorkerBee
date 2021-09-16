package com.github.blade2021.workerBee;

import com.github.blade2021.workerBee.events.GoLive;
import com.github.blade2021.workerBee.objects.Dispatcher;
import com.github.blade2021.workerBee.service.UserObjectService;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.JDAImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WorkerBeeApplication {

	public static String prefix = Config.get("prefix");
	public static JDAImpl jda = null;
	public static Dispatcher dispatcher;
	public static UserObjectService userObjectService;
	public static TwitchClient twitchClient = null;
	public static EventManager eventManager = new EventManager();

	public static OAuth2IdentityProvider provider = null;

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

		provider = new TwitchIdentityProvider(Config.get("TWITCHCLIENT-ID"), Config.get("TWITCHCLIENT-SECRET"),"http://localhost");

		twitchClient = TwitchClientBuilder.builder()
				.withDefaultAuthToken(credential)
				.withEnableHelix(true)
				.withEventManager(eventManager)
				.build();

		twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new GoLive());
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}


	@Bean
	CommandLineRunner run(UserObjectService userObjectService){

		WorkerBeeApplication.userObjectService = userObjectService;

		return args -> {

			String accessToken = userObjectService.getUserObject("313832264792539142").getTwitchAccessToken();
			String refreshToken = userObjectService.getUserObject("313832264792539142").getTwitchRefreshToken();

			Optional<OAuth2Credential> refreshed = provider.refreshCredential(credential);

			twitchClient = TwitchClientBuilder.builder()
					.withDefaultAuthToken()
					.withEnableHelix(true)
					.withEventManager(eventManager)
					.build();

			twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new GoLive());
		};

	}

}
