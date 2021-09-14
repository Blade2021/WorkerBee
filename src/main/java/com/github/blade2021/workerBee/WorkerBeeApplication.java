package com.github.blade2021.workerBee;

import com.github.blade2021.workerBee.events.GoLive;
import com.github.blade2021.workerBee.objects.Dispatcher;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.philippheuer.credentialmanager.identityprovider.TwitchIdentityProvider;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.JDAImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WorkerBeeApplication {

	public static String prefix = Config.get("prefix");
	public static JDAImpl jda = null;
	public static Dispatcher dispatcher;

	public static void main(String[] args) throws LoginException {
		SpringApplication.run(WorkerBeeApplication.class, args);

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

		// Event Manager

		EventManager eventManager = new EventManager();

		OAuth2IdentityProvider provider = new TwitchIdentityProvider("clientid","secret","redirectURL");
		OAuth2Credential credential = provider.getCredentialByCode("code");

		TwitchClient twitchClient = TwitchClientBuilder.builder()
				.withEnableHelix(true)
				.withEventManager(eventManager)
				.build();

		twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new GoLive());

	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}

}
