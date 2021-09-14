package com.github.blade2021.workerBee.events;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.events.ChannelGoLiveEvent;

public class GoLive {

    @EventSubscriber
    public void goLiveEvent(ChannelGoLiveEvent event){
        System.out.println(event.getStream().getTitle());
    }

}
